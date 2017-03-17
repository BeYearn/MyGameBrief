package com.emagroup.briefsdk;

import android.app.Activity;
import android.util.Log;

import org.json.JSONObject;

import java.util.Map;

/**
 * Created by Administrator on 2017/3/17.
 */

public class EMASDK {
    private static EMASDK mInstance;
    private Activity mActivity;

    private EMASDK() {
    }

    public static EMASDK getInstance() {
        if (null == mInstance) {
            mInstance = new EMASDK();
        }
        return mInstance;
    }

    public void init(Activity activity) {
        this.mActivity = activity;

        Map<String, String> deviceInfo = DeviceInfoManager.getInstance(mActivity).deviceInfoGather();
        UCommUtil.logMap(deviceInfo);


        JSONObject jsonObject = new JSONObject(deviceInfo);
        String s = jsonObject.toString();
        Log.e("deviceInfo", s);

        UCommUtil.writeFile("deviceInfo.txt",s,ConfigManager.getInstance(mActivity).getSdDir());

    }


}
