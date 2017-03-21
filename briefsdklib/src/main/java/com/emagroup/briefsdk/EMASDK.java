package com.emagroup.briefsdk;

import android.app.Activity;
import android.content.Intent;
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
        Log.e(Constants.GAME_INFO, s);

        UCommUtil.writeFile(ConfigManager.getInstance(mActivity).getSdDir()+Constants.GAME_INFO,s);

        Intent intent = new Intent(mActivity, UploadService.class);
        intent.putExtra(Constants.INFO_DATA,s);
        mActivity.startService(intent);

    }


}
