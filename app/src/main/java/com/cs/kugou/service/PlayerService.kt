package com.cs.kugou.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import com.cs.framework.Android
import com.cs.kugou.audio.Audio

/**
 *
 * author : ChenSen
 * data : 2018/1/18
 * desc:播放音乐的service
 */
class PlayerService : Service() {

    var mBinder = MyBinder()

    override fun onCreate() {
        super.onCreate()
        Android.log("加载音乐")
        Audio.loadingMusic(this)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return mBinder
    }

    override fun onUnbind(intent: Intent?): Boolean {
        return super.onUnbind(intent)
    }

    override fun onDestroy() {
        Android.log("音频播放器销毁")
        Audio.releasePlayer()
        super.onDestroy()
    }


    inner class MyBinder : Binder() {
        fun play() {
            Audio.play()
        }

        fun pause() {
            Audio.pause()
        }

        fun next() {}

        fun pre() {}


    }


}