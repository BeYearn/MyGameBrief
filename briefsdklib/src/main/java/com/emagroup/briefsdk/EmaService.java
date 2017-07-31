package com.emagroup.briefsdk;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.IBinder;

/**
 * Created by Administrator on 2017/3/20.
 * <p>
 * 这个service 首先改名字应该
 * 先判断有没有网,有网就帮注册,没网就利用启动这个服务时注册的接收联网广播来注册
 */

public class EmaService extends Service {

    private NetConnectReceiver mNetConnectReceiver;
    /**
     * 首次创建服务时，系统将调用此方法来执行一次性设置程序
     * （在调用 onStartCommand() 或 onBind() 之前）。如果服务已在运行，则不会调用此方法
     */
    @Override
    public void onCreate() {
        super.onCreate();
    }


    /**
     * 另一个组件调用 bindService() 与服务绑定（例如执行 RPC）时，系统将调用此方法
     * 如果您并不希望允许绑定，则应返回 null
     */
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    /**
     * 另一个组件通过调用 startService() 请求启动服务时，系统将调用此方法
     * 服务即会启动并可在后台无限期运行
     * 调用 stopSelf() 或 stopService() 来停止服务
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        LoginManager.getInstance().registOrLogin(this);
        registNetStateBCReceiver();

        return super.onStartCommand(intent, flags, startId);
    }


    private void registNetStateBCReceiver() {
        mNetConnectReceiver = new NetConnectReceiver();

        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        this.registerReceiver(mNetConnectReceiver, filter);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mNetConnectReceiver);
    }

}
