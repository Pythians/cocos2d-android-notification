package org.cocos2dx.lua;

import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Created by wjdev02 on 15/12/28.
 */
public class LocalNotificationManager {

    class Notice {
        String _key;
        int _time;
        long _now;
        String _content;
        String _title;
        String _sound;

        public Notice(String key, int time, String content, String title, String sound){
            _key = key;
            _time = time;
            _content = content;
            _title = title;
            _sound = sound;
            _now = System.currentTimeMillis();
        }

        public Notice(String key,int time, long now, String content, String title, String sound){
            _key = key;
            _time = time;
            _now = now;
            _content = content;
            _title = title;
            _sound = sound;
        }

        public boolean isDisuse(){
            return System.currentTimeMillis() > _now + _time * 1000;
        }
    }

    LinkedList<Notice> notifications;

    public boolean addLocalNotice(Notice notice){
        return notifications.add(notice);
    }

    public int getLocalNoticeNum(){
        return notifications.size();
    }

    public void cleanLocalNotice(){
        notifications.clear();
    }

    public boolean cleanLocalNotice(String key){
        int size = notifications.size();
        for (int i = 0; i < size; i++) {
            Notice notice = notifications.get(i);
            if (notice._key.equals(key)){
                return notifications.remove(notice);
            }
        }
        return false;
    }

    public void saveNoticeToFile(SharedPreferences preferences){
        SharedPreferences.Editor editor = preferences.edit();
        if (!notifications.isEmpty()){
            int size = notifications.size();
            editor.putInt("size", size);
            for (int i = 0; i < size; i++) {
                Notice notice = notifications.get(i);
                editor.putString("key"+ i, notice._key);
                editor.putInt("time"+ i, notice._time);
                editor.putLong("now"+ i, notice._now);
                editor.putString("content" + i, notice._content);
                editor.putString("title" + i, notice._title);
                editor.putString("sound" + i, notice._sound);
            }
        }else{
            editor.clear();
        }
        editor.apply();
    }

    public LocalNotificationManager(){
        notifications = new LinkedList<Notice>();
    }
    public LocalNotificationManager(SharedPreferences preferences){
        notifications = new LinkedList<Notice>();

        int size = preferences.getInt("size", 0);
        for (int i = 0; i < size; i++) {
            Notice notice = new Notice(
                    preferences.getString("key" + i, ""),
                    preferences.getInt("time" + i, 0),
                    preferences.getLong("now"+i,0),
                    preferences.getString("content" + i,""),
                    preferences.getString("title"+i, ""),
                    preferences.getString("sound"+i,""));
            if (!notice.isDisuse())
                notifications.add(notice);
        }
    }
}
