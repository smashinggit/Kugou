package com.cs.framework

import android.content.Context
import android.content.Context.WINDOW_SERVICE
import android.util.DisplayMetrics
import android.util.Log
import android.view.WindowManager

/**
 *
 * author : ChenSen
 * data : 2018/1/18
 * desc:
 */
object Android {
    public val DEV = true

    fun log(message: Any) {
        if (DEV) Log.i("test", message.toString())
    }

    fun log(tag: String, message: Any) {
        if (DEV) Log.i(tag, message.toString())
    }


    fun getScreenWidth(context: Context): Int {
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        var metrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(metrics)
        return metrics.widthPixels
    }

    fun getScreenHeight(context: Context): Int {
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        var metrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(metrics)
        return metrics.heightPixels
    }

    /**
     * 获取cup核数
     */
    fun cupCores(): Int {
        return Runtime.getRuntime().availableProcessors()
    }


}