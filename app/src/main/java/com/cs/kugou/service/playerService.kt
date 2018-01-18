package com.cs.kugou.service

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import com.cs.framework.Android

/**
 *
 * author : ChenSen
 * data : 2018/1/18
 * desc:播放音乐的service
 */
class playerService : Service() {

    lateinit var mPlayer: MediaPlayer


    override fun onCreate() {
        super.onCreate()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        Android.log("音频播放器销毁")
        releasePlayer()
        super.onDestroy()
    }

    private fun releasePlayer() {

    }

}