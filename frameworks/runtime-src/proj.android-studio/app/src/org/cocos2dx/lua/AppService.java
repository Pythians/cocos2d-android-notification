package org.cocos2dx.lua;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import java.util.Random;

/**
 * Created by wjdev02 on 16/1/3.
 */
public class AppService extends Service {

    private String sdk;

    private class Data{
        private String mTitle;
        private String mContent;
        private String mSound;

        public String getTitle() {
            return mTitle;
        }

        public void setTitle(String mTitle) {
            this.mTitle = mTitle;
        }

        public String getContent() {
            return mContent;
        }

        public void setContent(String mContent) {
            this.mContent = mContent;
        }

        public String getSound() {
            return mSound;
        }

        public void setSound(String mSound) {
            this.mSound = mSound;
        }
    }

    private void pushNotice(Data data){

        Intent it = new Intent(this,AppBroadcast.class);
        it.putExtra(AppBroadcast.SDKVER,sdk);
        it.putExtra(AppBroadcast.TITLE,data.getTitle());
        it.putExtra(AppBroadcast.CONTNT, data.getContent());
        it.putExtra(AppBroadcast.SOUND, data.getSound());
        sendBroadcast(it);
    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {

        Log.d("Notice", "onStartCommand: start service");

        sdk = intent.getStringExtra(AppBroadcast.SDKVER);
        final Data data = new Data();
        data.setContent("Content");
        data.setSound("");
        data.setTitle("Title");

        new Thread(new Runnable() {
            @Override
            public void run() {

                Random random = new Random();

                for (int i = 0; i < 3; i++) {
                    int t = random.nextInt(10);
                    data.setContent(data.getContent()+t);
                    Log.d("Notice", "Random time: " + t);
                    try {
                        Thread.sleep(t * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    pushNotice(data);
                }
            }
        }).start();

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this,"Service stop",Toast.LENGTH_SHORT).show();
        super.onDestroy();
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
