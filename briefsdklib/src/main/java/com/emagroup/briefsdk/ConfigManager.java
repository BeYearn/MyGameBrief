package com.emagroup.briefsdk;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;

import java.io.File;

public class ConfigManager {

    private static final String TAG = "ConfigManager";

    private static ConfigManager mInstance;
    private Context mContext;

    private String mAppid;
    private String mChannel;


    private String mSdDir = Environment.getExternalStorageDirectory() + File.separator + "EMASDK" + File.separator;


    private static final Object synchron = new Object();
    private String channelTag;

    public static ConfigManager getInstance(Context context) {
        if (mInstance == null) {
            synchronized (synchron) {
                if (mInstance == null) {
                    mInstance = new ConfigManager(context);
                }
            }
        }
        return mInstance;
    }

    private ConfigManager(Context context) {
        mContext = context;
        mAppid = null;
        mChannel = null;
    }

    /**
     * 清空配置信息
     */
    public void clear() {
        mAppid = null;
        mChannel = null;
    }

    public String getSdDir(){
        return mSdDir;
    }

    /**
     * 获取appid
     */
    public String getAppId() {
        if (mAppid == null) {
            mAppid = getStringFromMetaData(mContext, "EMA_APP_ID").substring(1);
        }
        return mAppid;
    }


    /**
     * 获取渠道号
     */
    public String getChannel() {
        if (mChannel == null) {
            mChannel = getStringFromMetaData(mContext, "EMA_CHANNEL_ID").substring(1);
        }
        return mChannel;
    }

    /**
     * 获取渠道号tag
     */
    public String getChannelTag() {
        if (channelTag == null) {
            channelTag = getStringFromMetaData(mContext, "EMA_CHANNEL_TAG").substring(1);
        }
        return channelTag;
    }


    /**
     * 初始化服务器地址，在sdk初始化的时候做
     */
    public void initServerUrl() {
        File sDir = new File(mSdDir);
        if (sDir.exists() && sDir.listFiles().length != 0) {
            Url.setServerUrl(UCommUtil.getFileContent(sDir.listFiles()[0]));
        } else {
            String emaEnvi = getStringFromMetaData(mContext, "EMA_WHICH_ENVI");
            if ("staging".equals(emaEnvi)) {
                Url.setServerUrl(Url.STAGING_SERVER_URL);
            } else if ("testing".equals(emaEnvi)) {
                Url.setServerUrl(Url.TESTING_SERVER_URL);
            } else {
                Url.setServerUrl(Url.PRODUCTION_SERVER_URL);
            }
        }

    }


    /**
     * 根据key获取metaData的Integer类型的数据
     *
     * @param context
     * @param key
     * @return
     */
    private int getIntegerFromMetaData(Context context, String key) {
        ApplicationInfo ai;
        int value = 0;
        try {
            ai = context.getPackageManager().getApplicationInfo(
                    context.getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle = ai.metaData;
            value = bundle.getInt(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }

    /**
     * 根据key获取metaData的string类型的数据
     *
     * @param context
     * @param key
     * @return
     */
    public String getStringFromMetaData(Context context, String key) {
        ApplicationInfo ai;
        String value = null;
        try {
            ai = context.getPackageManager().getApplicationInfo(
                    context.getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle = ai.metaData;
            value = bundle.getString(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }

    /**
     * 获取versioncode 整数
     *
     * @param context
     * @return
     */
    public int getVersionCode(Context context) {
        PackageManager packageManager = context.getPackageManager();
        PackageInfo packageInfo;
        int versionCode = 0;
        try {
            packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            versionCode = packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }


}
