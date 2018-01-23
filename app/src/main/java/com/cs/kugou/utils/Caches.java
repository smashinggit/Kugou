package com.cs.kugou.utils;

import android.preference.PreferenceManager;

import com.cs.kugou.App;

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
}
