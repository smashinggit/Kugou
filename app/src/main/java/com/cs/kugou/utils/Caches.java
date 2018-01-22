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

    //第一次扫描本地歌曲
    public static void firstScan() {
        Caches.save("scan", "true");
    }

    public static boolean isFirstScan() {
        return Caches.query("scan").equals("true");
    }

}
