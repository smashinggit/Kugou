package com.cs.kugou.utils;

import android.preference.PreferenceManager;
import android.util.Log;

import com.cs.kugou.App;
import com.cs.kugou.db.Music;
import com.google.gson.Gson;

import org.json.JSONObject;

/**
 * author : ChenSen
 * data : 2018/1/22
 * desc:本地缓存类
 */

public class Caches {

    public static void save(String key, String value) {
        PreferenceManager.getDefaultSharedPreferences(App.self)
                .edit()
                .putString(key, value)
                .apply();
    }

    public static String query(String key) {
        return PreferenceManager.getDefaultSharedPreferences(App.self)
                .getString(key, "");
    }

    public static void saveBoolean(String key, Boolean value) {
        PreferenceManager.getDefaultSharedPreferences(App.self)
                .edit()
                .putBoolean(key, value)
                .apply();
    }

    public static boolean queryBoolean(String key) {
        return PreferenceManager.getDefaultSharedPreferences(App.self)
                .getBoolean(key, false);
    }

    public static boolean queryBoolean(String key, boolean defaultValue) {
        return PreferenceManager.getDefaultSharedPreferences(App.self)
                .getBoolean(key, defaultValue);
    }

    public static void saveInt(String key, int value) {
        PreferenceManager.getDefaultSharedPreferences(App.self)
                .edit()
                .putInt(key, value)
                .apply();
    }

    public static int queryInt(String key) {
        return PreferenceManager.getDefaultSharedPreferences(App.self)
                .getInt(key, 0);
    }

    public static int queryInt(String key, int defaultValue) {
        return PreferenceManager.getDefaultSharedPreferences(App.self)
                .getInt(key, defaultValue);
    }


    //是否第一次启动
    public static boolean isFirst() {
        return queryBoolean("first", true);
    }

    public static void setIsFirst(boolean isFirst) {
        saveBoolean("first", isFirst);
    }


    public static String getLastPlaying() {
        return Caches.query("lastPlaying");
    }

    public static void saveLastPlaying(Music music) {
        Caches.save("lastPlaying", new Gson().toJson(music));
    }
}
