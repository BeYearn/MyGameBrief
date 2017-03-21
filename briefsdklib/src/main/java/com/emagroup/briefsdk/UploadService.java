package com.emagroup.briefsdk;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2017/3/20.
 */

public class UploadService extends Service {

    String TAG="UploadService";
    private final int period =20 * 1000;
    private Timer mTimer;

    @Override
    public void onCreate() {
        super.onCreate();
        mTimer = new Timer();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        HashMap<String, String> params = new HashMap<>();

        String dataInfo = intent.getStringExtra(Constants.INFO_DATA);

        if (null == dataInfo) {
            dataInfo = UCommUtil.readFile(ConfigManager.getInstance(this).getSdDir() + Constants.GAME_INFO);
        }

        Log.e(TAG,UCommUtil.readFile(ConfigManager.getInstance(this).getSdDir() + Constants.GAME_INFO));
        params.put("info", dataInfo);


        mTimer.schedule(new MyTimerTask(params), 0, 20*1000);

        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public void onDestroy() {
        mTimer.cancel();
        super.onDestroy();
    }

    private class MyTimerTask extends TimerTask {

        private final Map<String, String> map;

        private MyTimerTask(Map<String, String> map) {
            this.map = map;
        }

        @Override
        public void run() {
            Log.e(TAG,"我是五秒一次");
            new HttpInvoker().postAsync(Url.getServerUrl(), map, new HttpInvoker.OnResponsetListener() {
                @Override
                public void OnResponse(String result) {

                    //当请求上传成功后
                    //UploadService.this.stopSelf();
                }
            });
        }
    }


    public void upLoadMsg() {
        new AsyncTask<String, Integer, String>() {
            @Override
            protected String doInBackground(String... params) {
//                new HttpInvoker().post();
                return null;
            }

            @Override
            protected void onPostExecute(String s) {   //运行在ui的对结果的处理
                super.onPostExecute(s);
            }

            @Override
            protected void onPreExecute() {   // ui 开始前
                super.onPreExecute();
            }

            @Override
            protected void onProgressUpdate(Integer... values) {   // 泛型的第二个参数  ui 在doInBackground方法当中，，每次调用publishProgress方法都会触发onProgressUpdate执行
                super.onProgressUpdate(values);
            }
        };
    }

}
