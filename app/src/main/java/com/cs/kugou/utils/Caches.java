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


    //是否扫描本地歌曲
    public static boolean isScaned() {
        return Caches.query("scan").equals("true");
    }

    public static void scaned() {
        Caches.save("scan", "true");
    }


    public static String  getLastPlaying() {
        return Caches.query("lastPlaying");
    }

    public static void saveLastPlaying(Music music) {
        Caches.save("lastPlaying", new Gson().toJson(music));
    }
}
