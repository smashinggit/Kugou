package com.cs.kugou.service

import android.annotation.SuppressLint
import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Handler
import android.os.IBinder
import android.os.Message
import com.cs.framework.Android
import com.cs.kugou.db.Music
import com.cs.kugou.module.MusicModule
import com.cs.kugou.utils.Caches
import com.google.gson.Gson
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

/**
 *
 * author : ChenSen
 * data : 2018/1/18
 * desc:播放音乐的service
 */
class PlayerService : Service(), MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener {
    companion object {
        const val ACTION_PLAY = 0
        const val ACTION_PAUSE = 1
        const val ACTION_NEXT = 2
        const val ACTION_READY = 3
        const val ACTION_LOAD = 4
    }

    val STATE_PAUSE = 0  //暂停
    val STATE_PLAYING = 1 //播放中
    val STATE_PREPRAED = 2//就绪
    val STATE_LOADING = 3 //加载中
    var mCurrentState = STATE_LOADING

    var mPlayList = arrayListOf<Music>()
    var mPlayer: MediaPlayer? = null
    var mPlayingMusic: Music? = null //当前正在播放的音乐
    var mHandler = @SuppressLint("HandlerLeak")
    object : Handler() {
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            if (mCurrentState == STATE_PLAYING) {
                sendEmptyMessageDelayed(0, 1000)
            }
        }
    }

    @Subscribe()
    fun onMusicEvent(event: MusicEvent) {
        when (event.action) {
            ACTION_PLAY -> {
                play()
            }
            ACTION_PAUSE -> pause()
            ACTION_NEXT -> next()
            ACTION_READY -> {
                mPlayList = MusicModule.playList
            }
            ACTION_LOAD -> {
                mPlayingMusic = event.music
                mPlayingMusic?.let {
                    loadMusic(it, true)
                }
            }
        }
    }

    private fun next() {
    }

    override fun onCreate() {
        super.onCreate()
        EventBus.getDefault().register(this)
        Caches.getLastPlaying()?.let {
            mPlayingMusic = Gson().fromJson(it, Music::class.java)
            mPlayingMusic?.let {
                loadMusic(it, false)
            }
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)
    }

    fun loadMusic(music: Music, isPlay: Boolean = false) {
        mHandler.removeMessages(0)

        mPlayer?.let {
            releasePlayer()
        }
        mPlayer = MediaPlayer()
        mPlayer?.reset()
        mPlayer?.setDataSource(this, Uri.parse(music.url))
        //发送消息，更新ui
        mPlayer?.setOnErrorListener(this)
        mPlayer?.setOnCompletionListener(this)
        mPlayer?.prepareAsync()
        mPlayer?.setOnPreparedListener {
            mCurrentState = STATE_PREPRAED
            Caches.saveLastPlaying(music)
            if (isPlay) {
                mPlayer?.start()
                mCurrentState = STATE_PLAYING
            }
        }
    }

    fun play() {
        if (mPlayingMusic == null) return
        if (mCurrentState == STATE_PREPRAED || mCurrentState == STATE_PAUSE) {
            mCurrentState = STATE_PLAYING
            mPlayer?.start()
            mHandler.sendEmptyMessage(0)
        }
    }

    fun pause() {
        if (mCurrentState == STATE_PLAYING) {
            mCurrentState = STATE_PAUSE
            mPlayer?.pause()
            mHandler.removeMessages(0)
        }
    }


    override fun onCompletion(mp: MediaPlayer?) {
        mPlayingMusic = null
    }

    override fun onError(mp: MediaPlayer?, what: Int, extra: Int): Boolean {
        return true
    }


    fun getPlayPosition(): Int {
        return try {
            if (mPlayer == null) 0 else mPlayer!!.currentPosition
        } catch (e: Exception) {
            0
        }
    }

    fun seekToPositon(position: Int) {
        try {
            mPlayer?.seekTo(position)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun releasePlayer() {
        mHandler.removeMessages(0)
        mPlayer?.stop()
        mPlayer?.release()
        mPlayer = null
    }

    override fun onDestroy() {
        EventBus.getDefault().unregister(this)
        Android.log("音频播放器销毁")
        releasePlayer()
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    class MusicEvent(var action: Int) {
        var music: Music? = null
    }


}