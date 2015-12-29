package org.cocos2dx.lua;

import android.content.SharedPreferences;
import java.util.LinkedList;

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
    SharedPreferences preferences;

    public boolean addLocalNotice(String key, int time, String content, String title, String sound){
        Notice notice = new Notice(key,time,content,title,sound);
        if (notifications.add(notice)){
            saveNoticeToFile();
            return true;
        }else
            return false;
    }

    public String allNoticeToString(){
        String string = "{";

        int size = notifications.size();
        for (int i = 0; i <size; i++) {
            Notice notice = notifications.get(i);
            string += "{";
            string += "key='" + notice._key + "',";
            string += "time=" + notice._time + ",";
            string += "content='" + notice._content + "',";
            string += "title='" + notice._title + "',";
            string += "sound='" + notice._sound + "',";
            string += "},";
        }

        string += "}";
        saveNoticeToFile();
        return string;
    }

    public int getLocalNoticeNum(){
        saveNoticeToFile();
        return notifications.size();
    }

    public void cleanLocalNotice(){
        notifications.clear();
        saveNoticeToFile();
    }

    public boolean cleanLocalNotice(String key){
        int size = notifications.size();
        for (int i = 0; i < size; i++) {
            Notice notice = notifications.get(i);
            if (notice._key.equals(key)){
                notifications.remove(notice);
                saveNoticeToFile();
                return true;
            }
        }
        return false;
    }

    public void saveNoticeToFile(){
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
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
        }
        editor.apply();
    }

    public LocalNotificationManager(SharedPreferences pre){
        preferences = pre;
        notifications = new LinkedList<>();

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
