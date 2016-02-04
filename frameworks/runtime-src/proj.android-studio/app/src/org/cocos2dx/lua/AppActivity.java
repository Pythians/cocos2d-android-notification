/****************************************************************************
 * Copyright (c) 2015 Chukong Technologies Inc.
 * <p/>
 * http://www.cocos2d-x.org
 * <p/>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p/>
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * <p/>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 ****************************************************************************/
package org.cocos2dx.lua;


import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import org.cocos2dx.lib.Cocos2dxActivity;

import java.io.InputStream;
import java.util.ArrayList;


public class AppActivity extends Cocos2dxActivity {

    static String hostIPAdress = "0.0.0.0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (nativeIsLandScape()) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
        }

        //2.Set the format of window

        // Check the wifi is opened when the native is debug.
        if (nativeIsDebug()) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            if (!isNetworkConnected()) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Warning");
                builder.setMessage("Please open WIFI for debuging...");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                        finish();
                        System.exit(0);
                    }
                });

                builder.setNegativeButton("Cancel", null);
                builder.setCancelable(true);
                builder.show();
            }
            hostIPAdress = getHostIpAddress();
        }
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();
            ArrayList networkTypes = new ArrayList();
            networkTypes.add(ConnectivityManager.TYPE_WIFI);
            try {
                networkTypes.add(ConnectivityManager.class.getDeclaredField("TYPE_ETHERNET").getInt(null));
            } catch (NoSuchFieldException nsfe) {
            } catch (IllegalAccessException iae) {
                throw new RuntimeException(iae);
            }
            if (networkInfo != null && networkTypes.contains(networkInfo.getType())) {
                return true;
            }
        }
        return false;
    }

    public String getHostIpAddress() {
        WifiManager wifiMgr = (WifiManager) getSystemService(WIFI_SERVICE);
        WifiInfo wifiInfo = wifiMgr.getConnectionInfo();
        int ip = wifiInfo.getIpAddress();
        return ((ip & 0xFF) + "." + ((ip >>>= 8) & 0xFF) + "." + ((ip >>>= 8) & 0xFF) + "." + ((ip >>>= 8) & 0xFF));
    }

    public static String getLocalIpAddress() {
        return hostIPAdress;
    }


    public static boolean isForeground = false;

    @Override
    protected void onResume() {
        super.onResume();
        isForeground = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        isForeground = false;
    }

    @Override
    protected void onStart() {
        AppNotification.cancel(AppActivity.getContext());

        _matchParent = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        _frameLayout = new FrameLayout(getContext());
        addContentView(_frameLayout, _matchParent);
        super.onStart();
    }

    @Override
    protected void onStop() {
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        LocalNotificationManager notificationManager = new LocalNotificationManager(
                PreferenceManager.getDefaultSharedPreferences(this));
        // 获取最早的通知
        LocalNotificationManager.Notice notice = notificationManager.getFirstNotice();

        if (notice != null) {
            Intent intent = new Intent(this, AppBroadcast.class);
            intent.putExtra(AppBroadcast.TITLE, notice.getTitle());
            intent.putExtra(AppBroadcast.CONTNT, notice.getContent());
            intent.putExtra(AppBroadcast.SOUND, notice.getSound());
            intent.putExtra(AppBroadcast.SDKVER, getPackageName());
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
            // 定时发送
            manager.set(AlarmManager.RTC_WAKEUP, notice.getWhen(), pendingIntent);
            // 发送后删除这条通知
            notificationManager.removeNotice(notice);
        }
        // 启动服务，服务已存在，执行服务中的任务
        Intent intent = new Intent(this, AppService.class);
        intent.putExtra(AppBroadcast.SDKVER, getPackageName());
//        startService(intent);

        super.onStop();
    }

    public static int getLocalNotificationNum() {
        LocalNotificationManager manager = new LocalNotificationManager(
                PreferenceManager.getDefaultSharedPreferences(AppActivity.getContext()));
        return manager.getLocalNoticeNum();
    }

    public static boolean pushLocalNotification(int time,
                                                String key,
                                                String title,
                                                String content,
                                                String sound) {
        LocalNotificationManager manager = new LocalNotificationManager(
                PreferenceManager.getDefaultSharedPreferences(AppActivity.getContext()));
        return manager.addLocalNotice(key, time, content, title, sound);
    }

    public static int clearNoticeByKey(String key) {
        LocalNotificationManager manager = new LocalNotificationManager(
                PreferenceManager.getDefaultSharedPreferences(AppActivity.getContext()));
        return manager.cleanLocalNotice(key);
    }

    public static void clearAllNotices() {
        new LocalNotificationManager(
                PreferenceManager.getDefaultSharedPreferences(
                        AppActivity.getContext())
        ).cleanLocalNotice();
        AppNotification.cancel(AppActivity.getContext());
    }

    public static String getAllNotices() {
        return new LocalNotificationManager(
                PreferenceManager.getDefaultSharedPreferences(
                        AppActivity.getContext())).allNoticeToString();
    }

    public static void showWebView(String url, String img, float x, float y) {
        img = "res/" + img;
        if (_webView == null) {
            _webView = new WebView(AppActivity.getContext());
        }
        _webView.getSettings().setJavaScriptEnabled(true);
        _webView.setLayoutParams(_matchParent);
        _webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        _webView.loadUrl(url);
        _frameLayout.addView(_webView);

        if (_button == null) {
            _button = new ImageButton(AppActivity.getContext());
        }

        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        layoutParams.rightMargin = (int)x;
        layoutParams.topMargin = (int)y;
        layoutParams.gravity = Gravity.RIGHT | Gravity.TOP;
        _button.setLayoutParams(layoutParams);
        _button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppActivity.removeWebView();
            }
        });
        AssetManager manager = getContext().getResources().getAssets();
        try {
            InputStream asset = manager.open(img);
            _button.setImageBitmap(BitmapFactory.decodeStream(asset));
        } catch (Exception e) {
            e.printStackTrace();
        }

        _frameLayout.addView(_button);
    }

    public static void removeWebView() {
        _frameLayout.removeAllViews();
    }

    public static void playSound(String sound){
        try
        {
            MediaPlayer player = new MediaPlayer();
            AssetFileDescriptor descriptor = getContext().getAssets().openFd(sound);
            player.setDataSource(descriptor.getFileDescriptor());
            player.prepare();
            player.start();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private static FrameLayout.LayoutParams _matchParent;
    private static FrameLayout _frameLayout = null;
    private static WebView _webView = null;
    private static ImageButton _button = null;

    private static native boolean nativeIsLandScape();

    private static native boolean nativeIsDebug();

}
