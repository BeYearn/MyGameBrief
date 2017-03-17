package com.emagroup.briefsdk;

public class Url {

    //三个环境 常量
    public static final String PRODUCTION_SERVER_URL = "https://platform.lemonade-game.com";
    public static final String STAGING_SERVER_URL = "https://staging-platform.lemonade-game.com";
    public static final String TESTING_SERVER_URL = "https://testing-platform.lemonade-game.com:8443";

    //默认正式
    public static String mServerUrl = "https://platform.lemonade-game.com";

    public static void setServerUrl(String url) {
        mServerUrl = url;
    }
    public static String getServerUrl() {
        return mServerUrl;
    }

    /**
     * 获取第一步登录请求接口
     *
     * @return
     */
    public static String getFirstLoginUrl() {

        return mServerUrl + "/ema-platform/member/pfLogin";
    }


}
