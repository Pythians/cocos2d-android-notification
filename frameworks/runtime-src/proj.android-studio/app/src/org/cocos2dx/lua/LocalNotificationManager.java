package org.cocos2dx.lua;

import android.content.SharedPreferences;
import java.util.LinkedList;

public class LocalNotificationManager {

     class Notice {
         String _key;
         int _time;
         long _when;
         String _content;
         String _title;
         String _sound;

         public Notice(){
             _when = Long.MAX_VALUE;
         }
         public Notice(String key, int time, String content, String title, String sound){
             _key = key;
             _time = time;
             _content = content;
             _title = title;
             _sound = sound;
             _when = System.currentTimeMillis() + 1000 * _time;
         }

         public Notice(String key,int time, long when, String content, String title, String sound){
             _key = key;
             _time = time;
             _when = when;
             _content = content;
             _title = title;
             _sound = sound;
         }

         public int getTime() {
             return _time;
         }

         public String getKey() {
             return _key;
         }

         public long getWhen() {
             return _when;
         }

         public String getContent() {
             return _content;
         }

         public String getSound() {
             return _sound;
         }

         public String getTitle() {
             return _title;
         }

         public boolean isDisuse(){
            return System.currentTimeMillis() > _when;
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

    public Notice getFirstNotice() {
        if (notifications.size() == 0)
            return null;
        Notice tem = new Notice();
        for (Notice notice :
                notifications) {
            if (notice.getWhen() < tem.getWhen())
            {
                tem = notice;
            }
        }
        return tem;
    }

    public String allNoticeToString(){
        String string = "{";

        for (Notice notice : notifications){
            string += "{";
            string += "key='" + notice.getKey() + "',";
            string += "time=" + notice.getTime() + ",";
            string += "content='" + notice.getContent() + "',";
            string += "title='" + notice.getTitle() + "',";
            string += "sound='" + notice.getSound() + "',";
            string += "},";
        }

        string += "}";
        return string;
    }

    public int getLocalNoticeNum(){
        return notifications.size();
    }

    public void cleanLocalNotice(){
        notifications.clear();
        saveNoticeToFile();
    }

    public int cleanLocalNotice(String key){
        LinkedList<Notice> notices = new LinkedList<Notice>();
        int t = 0;
        for (Notice notice : notifications) {
            if (notice.getKey().equals(key)) {
                t+=1;
                notices.add(notice);
            }
        }
        for (Notice notice : notices){
            removeNotice(notice);
        }
        return t;
    }

    public void removeNotice(Notice notice){
        notifications.remove(notice);
        saveNoticeToFile();
    }

    public void saveNoticeToFile(){
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        if (!notifications.isEmpty()){
            int size = notifications.size();
            editor.putInt("size", size);
            for (int i = 0; i < size; i++) {
                Notice notice = notifications.get(i);
                editor.putString("key"+ i, notice.getKey());
                editor.putInt("time"+ i, notice.getTime());
                editor.putLong("when"+ i, notice.getWhen());
                editor.putString("content" + i, notice.getContent());
                editor.putString("title" + i, notice.getTitle());
                editor.putString("sound" + i, notice.getSound());
            }
        }
        editor.apply();
    }

    public LocalNotificationManager(SharedPreferences pre){
        preferences = pre;
        notifications = new LinkedList<Notice>();

        int size = preferences.getInt("size", 0);
        for (int i = 0; i < size; i++) {
            Notice notice = new Notice(
                    preferences.getString("key" + i, ""),
                    preferences.getInt("time" + i, 0),
                    preferences.getLong("when"+i,0),
                    preferences.getString("content" + i,""),
                    preferences.getString("title"+i, ""),
                    preferences.getString("sound"+i,""));
            if (!notice.isDisuse())
                notifications.add(notice);
        }
        saveNoticeToFile();
    }
}
