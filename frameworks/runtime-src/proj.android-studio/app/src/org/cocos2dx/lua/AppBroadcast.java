package org.cocos2dx.lua;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.List;


public class AppBroadcast extends BroadcastReceiver {

    public static final String TITLE = "title";
    public static final String CONTNT = "content";
    public static final String SOUND = "sound";
    public static final String SDKVER = "version";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("Notice", "onReceive " + System.currentTimeMillis());

        LocalNotificationManager notificationManager = new LocalNotificationManager(
                PreferenceManager.getDefaultSharedPreferences(context));
        LocalNotificationManager.Notice notice = notificationManager.getFirstNotice();
        if (notice != null){
            Intent it = new Intent(context,AppBroadcast.class);
            it.putExtra(TITLE, notice.getTitle());
            it.putExtra(CONTNT, notice.getContent());
            it.putExtra(SOUND, notice.getSound());
            it.putExtra(SDKVER, intent.getStringExtra(SDKVER));
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, it, 0);

            AlarmManager manager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
            manager.set(AlarmManager.RTC_WAKEUP, notice.getWhen(), pendingIntent);
            notificationManager.removeNotice(notice);
        }


        boolean foreground = false;
//        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH){
            ActivityManager activityManager = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningAppProcessInfo> apps = activityManager.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo app : apps){
                if(app.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND
                        && app.processName.equals(intent.getStringExtra(SDKVER))){
                    foreground = true;
                }
            }
//        }else {
//            ActivityManager activityManager = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
//            ActivityManager.RunningTaskInfo info = activityManager.getRunningTasks(1).get(0);
//            String name = info.topActivity.getClassName();
//            if (name.equals(intent.getStringExtra(SDKVER)))
//                foreground = true;
//        }

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
