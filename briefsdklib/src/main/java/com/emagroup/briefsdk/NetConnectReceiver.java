package com.emagroup.briefsdk;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetConnectReceiver extends BroadcastReceiver {

    private ConnectivityManager connectivityManager;

    // 网络状态信息的实例
    private NetworkInfo info;
    /**
     * 当前处于的网络
     * 0 ：null
     * 1 ：2G/3G
     * 2 ：wifi
     */
    public static int networkStatus;


    @Override
    public void onReceive(Context context, Intent intent) {

        String action = intent.getAction(); //当前接受到的广播的标识(行动/意图) // 当当前接受到的广播的标识(意图)为网络状态的标识时做相应判断

        if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            // 获取网络连接管理器
            connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            // 获取当前网络状态信息
            info = connectivityManager.getActiveNetworkInfo();

            if (info != null && info.isAvailable()) { //当NetworkInfo不为空且是可用的情况下，获取当前网络的Type状态

                L.e(info.toString());

                //do something
                netValidNext(context);

                String name = info.getTypeName();// 根据NetworkInfo.getTypeName()判断当前网络类型
                if (name.equals("WIFI")) {
                    networkStatus = 2;
                } else {
                    networkStatus = 1;
                }
            } else { // NetworkInfo为空或者是不可用的情况下
                networkStatus = 0;
                L.e("没有可用网络!");
            }

            L.e("netState:"+networkStatus);
        }
    }



    private void netValidNext(Context context) {

        NetedDoService.upLoadInfo(context);
        NetedDoService.registWeakAccount(context);
    }


}
