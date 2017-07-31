package com.emagroup.briefsdk;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by beyearn on 2017/7/31.
 */

public class LoginManager {
    private static LoginManager mInstance;
    private EmaUser mEmaUser;


    public static LoginManager getInstance() {
        if (null == mInstance) {
            mInstance = new LoginManager();
        }
        return mInstance;
    }

    public void registOrLogin(Context context) {
        mEmaUser = EmaUser.getInstance();
        if (mEmaUser.isLogin) {
            return;
        }

        ConfigManager mConfigManager = ConfigManager.getInstance(context);
        DeviceInfoManager mDeviceManager = DeviceInfoManager.getInstance(context);

        Map<String, String> params = new HashMap<>();
        params.put("accountType", "0");
        params.put("deviceType", "android");
        params.put("allianceId", mConfigManager.getChannel());
        params.put("channelTag", mConfigManager.getChannelTag());
        params.put("appId", mConfigManager.getAppId());
        params.put("deviceKey", mDeviceManager.getDEVICE_ID());

        String sign = 0 + mConfigManager.getChannel() + mConfigManager.getAppId() + mConfigManager.getChannelTag() + mDeviceManager.getDEVICE_ID() + "android" + EMASDK.getInstance().getAppKey();
        sign = UCommUtil.MD5(sign);
        params.put("sign", sign);

        new HttpInvoker().postAsync(Url.getFirstLoginUrl(), params,
                new HttpInvoker.OnResponsetListener() {
                    @Override
                    public void OnResponse(String result) {
                        try {
                            JSONObject json = new JSONObject(result);
                            JSONObject data = json.getJSONObject("data");
                            int resultCode = json.getInt("status");
                            if (resultCode == 0) {
                                doResult(data);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void doResult(JSONObject data) {
        try {
            String uid = data.getString("uid");
            String allianceUid = data.getString("allianceUid");
            mEmaUser.setUid(uid);
            mEmaUser.setAllianceUid(allianceUid);

            String nickname = data.getString("nickname");
            mEmaUser.setNickName(nickname);

            String allianceId = data.getString("allianceId");

            String authCode = data.getString("authCode");

            String callbackUrl = data.getString("callbackUrl");

            loginAuth(data.toString(), callbackUrl);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void loginAuth(String data, String callbackUrl) {
        Map<String, String> params = new HashMap<>();
        params.put("data", data);
        new HttpInvoker().post(callbackUrl, params,
                new HttpInvoker.OnResponsetListener() {
                    @Override
                    public void OnResponse(String result) {
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            int resultCode = jsonObject.getInt("status");

                            if (resultCode == 0) {
                                JSONObject data = jsonObject.getJSONObject("data");
                                String token = data.getString("token");

                                mEmaUser.setToken(token);
                                mEmaUser.setAccountType("0");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }


}
