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
import com.cs.kugou.mvp.view.MainView
import com.cs.kugou.utils.Caches
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
        const val ACTION_LOAD_PLAY = 5

        const val STATE_PAUSE = 0  //暂停
        const val STATE_PLAYING = 1 //播放中
        const val STATE_PREPRAED = 2//就绪
        const val STATE_LOADING = 3 //加载中
        const val STATE_IDLE = 4 //空闲状态
        var mCurrentState = STATE_IDLE
    }


    var mPlayList = arrayListOf<Music>()
    var mPlayer: MediaPlayer? = null
    var mPlayingMusic: Music? = null //当前正在播放的音乐
    var mHandler = @SuppressLint("HandlerLeak")
    object : Handler() {
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            if (mCurrentState == STATE_PLAYING) {
                EventBus.getDefault().post(ProgressEvent(getPlayPosition()))//向Activity发送播放进度
                sendEmptyMessageDelayed(0, 1000)
            }
        }
    }

    //控制音乐
    @Subscribe()
    fun onMusicActionEvent(event: MusicActionEvent) {
        when (event.action) {
            ACTION_PLAY -> play()
            ACTION_PAUSE -> pause()
            ACTION_NEXT -> next()
            ACTION_LOAD -> {
                mPlayingMusic = event.music
                mPlayingMusic?.let {
                    loadMusic(it, false)
                }
            }
            ACTION_LOAD_PLAY -> {
                mPlayingMusic = event.music
                mPlayingMusic?.let {
                    loadMusic(it, true)
                }
            }
        }
    }

    //读取数据库完毕
//    @Subscribe
//    fun onReadFromDBExevt(event: MusicModule.ReadFromDBExevt) {
//        if (event.type.equals(MusicModule.PLAY))
//            mPlayList = MusicModule.playList //播放列表
//    }

    //播放进度
    @Subscribe
    fun onSeekExevt(event: MainView.SeekEvent) {
        seekToPositon(event.position)
    }

    private fun next() {
    }

    override fun onCreate() {
        super.onCreate()
        EventBus.getDefault().register(this)
        Android.log("onCreate")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)
    }

    fun loadMusic(music: Music, isPlay: Boolean = false) {
        var temp = System.currentTimeMillis()
        mCurrentState = STATE_LOADING
        EventBus.getDefault().post(PlayingInfoEvent(music))//更新播放栏信息
        sendStateChangeEvent(mCurrentState) //更新播放状态
        mHandler.removeMessages(0)
        mPlayer?.let {
            releasePlayer()
        }
        mPlayer = MediaPlayer()
        mPlayer?.reset()
        mPlayer?.setDataSource(this, Uri.parse(music.url))
        mPlayer?.setOnErrorListener(this)
        mPlayer?.setOnCompletionListener(this)
        mPlayer?.prepareAsync()
        mPlayer?.setOnPreparedListener {
            mCurrentState = STATE_PREPRAED
            sendStateChangeEvent(mCurrentState) //更新播放状态
            Caches.saveLastPlaying(music)
            if (isPlay) {
                //两首音乐播放间隔为500毫秒
                var time = System.currentTimeMillis() - temp
                if (time < 500) {
                    Handler().postDelayed({ playMusic() }, 500 - time)
                } else {
                    playMusic()
                }
            }
            Android.log("音乐加载完成")
        }
    }

    private fun playMusic() {
        mPlayer?.start()
        mCurrentState = STATE_PLAYING
        sendStateChangeEvent(mCurrentState) //更新播放状态
        mHandler.sendEmptyMessage(0)
        Android.log("播放音乐")
    }

    fun play() {
        if (mPlayingMusic == null) return
        if (mCurrentState == STATE_PREPRAED || mCurrentState == STATE_PAUSE) {
            mCurrentState = STATE_PLAYING
            sendStateChangeEvent(mCurrentState) //更新播放状态
            mPlayer?.start()
            mHandler.sendEmptyMessage(0)
            Android.log("播放音乐")
        }
    }

    fun pause() {
        if (mCurrentState == STATE_PLAYING) {
            mCurrentState = STATE_PAUSE
            sendStateChangeEvent(mCurrentState) //更新播放状态
            mPlayer?.pause()
            mHandler.removeMessages(0)
            Android.log("暂停音乐")
        }
    }


    override fun onCompletion(mp: MediaPlayer?) {
        mCurrentState = STATE_IDLE
        sendStateChangeEvent(mCurrentState) //更新播放状态
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
        mCurrentState = STATE_IDLE
        sendStateChangeEvent(mCurrentState) //更新播放状态
        EventBus.getDefault().unregister(this)
        Android.log("音频播放器销毁")
        releasePlayer()
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    //发送播放状态改变事件
    fun sendStateChangeEvent(state: Int) {
        EventBus.getDefault().post(StateChangeEvent(state))
    }

    //控制播放
    class MusicActionEvent() {
        var action: Int = ACTION_LOAD
        var music: Music? = null
    }

    class ProgressEvent(var progress: Int)

    class PlayingInfoEvent(var music: Music) //播放信息

    class StateChangeEvent(var state: Int)//播放状态改变事件
}