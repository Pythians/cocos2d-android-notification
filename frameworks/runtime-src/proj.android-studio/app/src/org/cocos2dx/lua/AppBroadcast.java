package org.cocos2dx.lua;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import java.util.List;


public class AppBroadcast extends BroadcastReceiver {

    public static final String TITLE = "title";
    public static final String CONTNT = "content";
    public static final String SOUND = "sound";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("Notice", "onReceive");

        boolean foreground = false;
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH){
            ActivityManager activityManager = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningAppProcessInfo> apps = activityManager.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo app : apps){
                if(app.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND
                        && app.processName.equals("")){
                    foreground = true;
                }
            }
        }else {
            ActivityManager activityManager = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
            ActivityManager.RunningTaskInfo info = activityManager.getRunningTasks(1).get(0);
            String name = info.topActivity.getClassName();
            if (name.equals(""))
                foreground = true;
        }

        if (!foreground) {
            AppNotification.notify(
                    context,
                    "Cocos2d",
                    intent.getStringExtra(TITLE),
                    intent.getStringExtra(CONTNT),
                    intent.getStringExtra(SOUND));
        }
    }
}
