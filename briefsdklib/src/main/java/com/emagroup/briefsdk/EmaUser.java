package com.emagroup.briefsdk;

/**
 * Created by beyearn on 2017/7/31.
 */

public class EmaUser {

    private static EmaUser mInstance;
    private String accountType;
    private String allianceUid;
    private String uid;
    private String nickName;
    private String token;
    public boolean isLogin = false;

    public static EmaUser getInstance() {
        if (mInstance == null) {
            mInstance = new EmaUser();
        }
        return mInstance;
    }


    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public void setUid(String mUid) {
        this.uid = mUid;
    }

    public void setAllianceUid(String allianceUid) {
        this.allianceUid = allianceUid;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public void setToken(String token) {
        this.token = token;
        this.isLogin = true;
    }
}
