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
    private int mAppKey;

    private EMASDK() {
    }

    public static EMASDK getInstance() {
        if (null == mInstance) {
            mInstance = new EMASDK();
        }
        return mInstance;
    }

    public void onCreat(Activity activity) {
        this.mActivity = activity;

        Map<String, String> deviceInfo = DeviceInfoManager.getInstance(mActivity).deviceInfoGather();
        UCommUtil.logMap(deviceInfo);


        JSONObject jsonObject = new JSONObject(deviceInfo);
        String s = jsonObject.toString();
        L.e(Constants.GAME_INFO, s);

        UCommUtil.writeFile(ConfigManager.getInstance(mActivity).getSdDir()+Constants.GAME_INFO,s);

        Intent intent = new Intent(mActivity, EmaService.class);
        //intent.putExtra(Constants.INFO_DATA,s);
        mActivity.startService(intent);

    }

    public void onDestory(){
        Intent intent = new Intent(mActivity, EmaService.class);
        mActivity.stopService(intent);
    }


    public int getAppKey() {
        return mAppKey;
    }
}
