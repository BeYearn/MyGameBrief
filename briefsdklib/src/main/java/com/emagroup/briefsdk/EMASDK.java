package com.emagroup.briefsdk;

import android.app.Activity;
import android.content.Intent;

import org.json.JSONObject;

import java.util.Map;

/**
 * Created by Administrator on 2017/3/17.
 */

public class EMASDK {

    private static EMASDK mInstance;

    private Activity mActivity;
    private String mAppKey;

    private EMASDK() {
    }

    public static EMASDK getInstance() {
        if (null == mInstance) {
            mInstance = new EMASDK();
        }
        return mInstance;
    }

    public void onCreat(String appKey,Activity activity) {
        this.mAppKey =appKey;
        this.mActivity = activity;

        //初始化环境
        ConfigManager.getInstance(activity).initServerUrl();

        //获取手机信息
        Map<String, String> deviceInfo = DeviceInfoManager.getInstance(mActivity).deviceInfoGather();
        JSONObject jsonObject = new JSONObject(deviceInfo);
        String s = jsonObject.toString();
        L.e(Constants.GAME_INFO, s);
        UCommUtil.writeFile(ConfigManager.getInstance(mActivity).getSdDir()+Constants.GAME_INFO,s);

        //启动后台服务
        Intent intent = new Intent(mActivity, EmaService.class);
        //intent.putExtra(Constants.INFO_DATA,s);
        mActivity.startService(intent);

    }

    public void onDestory(){
        Intent intent = new Intent(mActivity, EmaService.class);
        mActivity.stopService(intent);
    }


    public String getAppKey() {
        return mAppKey;
    }
}
