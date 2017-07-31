package com.emagroup.briefsdk;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import java.util.HashMap;

import static com.emagroup.briefsdk.Constants.ACTION_REGIST_ACCOUNT;
import static com.emagroup.briefsdk.Constants.ACTION_UPLOAD_INFO;

/**
 * Created by beyearn on 2017/7/28.
 */

public class NetedDoService extends IntentService {

    /**
     * A constructor is required, and must call the super IntentService(String)
     * constructor with a name for the worker thread.
     */
    public NetedDoService() {
        super("NetedDoService");
    }

    /**
     * The IntentService calls this method from the default worker thread with
     * the intent that started the service. When this method returns, IntentService
     * stops the service, as appropriate.
     */
    @Override
    protected void onHandleIntent(Intent intent) {

        if (null != intent) {

            final String action = intent.getAction();

            if (ACTION_UPLOAD_INFO.equals(action)) {

                HashMap<String, String> params = new HashMap<>();
                String dataInfo = UCommUtil.readFile(ConfigManager.getInstance(this).getSdDir() + Constants.GAME_INFO);
                params.put("info", dataInfo);

                new HttpInvoker().post(Url.getServerUrl(), params, new HttpInvoker.OnResponsetListener() {   //无需再切换至子线程
                    @Override
                    public void OnResponse(String result) {
                        //成功的话

                    }
                });

            } else if (ACTION_REGIST_ACCOUNT.equals(action)) {

                LoginManager.getInstance().registOrLogin(this);

            }
        }

    }

    public static void upLoadInfo(Context context) {
        Intent intent = new Intent(context, NetedDoService.class);
        intent.setAction(ACTION_UPLOAD_INFO);
        context.startService(intent);
    }

    public static void registWeakAccount(Context context) {
        Intent intent = new Intent(context, NetedDoService.class);
        intent.setAction(ACTION_REGIST_ACCOUNT);
        context.startService(intent);
    }
}
