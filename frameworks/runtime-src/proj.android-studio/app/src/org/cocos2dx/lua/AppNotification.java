package org.cocos2dx.lua;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import org.cocos2dx.notice.R;



public class AppNotification {

    private static String NOTIFICATION_TAG = "App";

    public static void notify(final Context context,
                              final String ticker,
                              final String title,
                              final String text,
                              final String sound) {
//        final Resources res = context.getResources();
//        final Bitmap picture = BitmapFactory.decodeResource(res, R.drawable.example_picture);

        final NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
//                .setDefaults(Notification.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setSmallIcon(R.mipmap.ic_launcher)
//                .setLargeIcon(picture)
                .setContentTitle(title)
                .setContentText(text)
                .setTicker(ticker)
                .setContentIntent(
                        PendingIntent.getActivity(
                                context,
                                0,
                                new Intent(context, AppActivity.class),
                                PendingIntent.FLAG_UPDATE_CURRENT))
                .setAutoCancel(true);
        if (sound.equals("")) {
            builder.setDefaults(Notification.DEFAULT_SOUND);
        } else if (sound.equals("NoSound")) {
            Log.d("Notice:", "No play sound");
        } else {
            Log.d("Notice:", sound);
        }
        notify(context, builder.build());
    }

    @TargetApi(Build.VERSION_CODES.ECLAIR)
    private static void notify(final Context context, final Notification notification) {
        final NotificationManager nm = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        NOTIFICATION_TAG += "1";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
            nm.notify(NOTIFICATION_TAG, 0, notification);
        } else {
            nm.notify(NOTIFICATION_TAG.hashCode(), notification);
        }
    }

    @TargetApi(Build.VERSION_CODES.ECLAIR)
    public static void cancel(final Context context) {
        final NotificationManager nm = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        nm.cancelAll();
        // if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
        //     nm.cancel(NOTIFICATION_TAG, 0);
        // } else {
        //     nm.cancel(NOTIFICATION_TAG.hashCode());
        // }
    }
}
