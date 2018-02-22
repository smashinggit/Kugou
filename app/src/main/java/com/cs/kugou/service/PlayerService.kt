package com.cs.kugou.service

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.MediaPlayer
import android.net.Uri
import android.os.Handler
import android.os.IBinder
import android.os.Message
import android.support.v4.app.NotificationCompat
import android.view.View
import android.widget.Filter
import android.widget.RemoteViews
import android.widget.Toast
import com.cs.framework.Android
import com.cs.kugou.R
import com.cs.kugou.db.Music
import com.cs.kugou.mvp.moudle.MusicMoudle
import com.cs.kugou.mvp.view.MainView
import com.cs.kugou.ui.MainActivity
import com.cs.kugou.utils.Caches
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import java.io.FileNotFoundException
import java.io.IOException

/**
 *
 * author : ChenSen
 * data : 2018/1/18
 * desc:播放音乐的service
 */
class PlayerService : Service(), MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener {

    companion object {
        const val FOREGROUND_ID = 10
        const val REQUEST_ID = 1

        const val REMOTEVIEW_PLAY = "play"
        const val REMOTEVIEW_PAUSE = "pause"
        const val REMOTEVIEW_NEXT = "next"
        const val REMOTEVIEW_LYRIC = "lyric"

        const val ACTION_PLAY = 0       //播放
        const val ACTION_PAUSE = 1      //暂停
        const val ACTION_NEXT = 2       //下一首
        const val ACTION_LOAD = 4       //加载音乐
        const val ACTION_LOAD_PLAY = 5  //加载并播放

        const val STATE_PAUSE = 0       //暂停
        const val STATE_PLAYING = 1     //播放中
        const val STATE_PREPRAED = 2    //就绪
        const val STATE_LOADING = 3     //加载中
        const val STATE_IDLE = 4        //空闲状态
        var mCurrentState = STATE_IDLE

        const val MODE_SEQUENCE = 0     //顺序播放
        const val MODE_LOOP = 1         //循环播放
        const val MODE_RANDOM = 1       //随机播放
        var mCurrentMode = MODE_SEQUENCE

        var mPlayList = arrayListOf<Music>()//播放列表
        var mPlayingIndex: Int = 0 //正在播放的音乐下标
    }

    var receiver = RemoteViewReceiver()  //此广播用于接收通知栏的操作信息
    var mPlayer: MediaPlayer? = null
    var mHandler = @SuppressLint("HandlerLeak")
    object : Handler() {
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            if (mCurrentState == STATE_PLAYING) {
                EventBus.getDefault().post(PlayingInfoEvent(mPlayList[mPlayingIndex], getPlayPosition()))//向Activity发送播放进度
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
                update(event)
                if (!mPlayList.isEmpty()) {
                    loadMusic(mPlayList[mPlayingIndex], false)
                }
            }
            ACTION_LOAD_PLAY -> {
                update(event)
                if (!mPlayList.isEmpty()) {
                    loadMusic(mPlayList[mPlayingIndex], true)
                }
            }
        }
    }

    private fun update(event: MusicActionEvent) {
        mPlayList = MusicMoudle.playList
        mPlayingIndex = event.position
        if (!mPlayList.isEmpty()){
            startForeground(FOREGROUND_ID, getNotification(0, mPlayList[mPlayingIndex]))
            Caches.saveInt("playingIndex", mPlayingIndex)
        }
    }

    //播放进度
    @Subscribe
    fun onSeekExevt(event: MainView.SeekEvent) {
        seekToPosition(event.position)
    }

    override fun onCreate() {
        super.onCreate()
        EventBus.getDefault().register(this)

        var filter = IntentFilter()
        filter.addAction(REMOTEVIEW_PLAY)
        filter.addAction(REMOTEVIEW_PAUSE)
        filter.addAction(REMOTEVIEW_NEXT)
        filter.addAction(REMOTEVIEW_LYRIC)
        registerReceiver(receiver, filter)

        Android.log("PlayerService  onCreate")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Android.log("service 构建前台服务")
        startForeground(FOREGROUND_ID, getNotification(0, null))
        return super.onStartCommand(intent, flags, startId)
    }

    private fun getNotification(flag: Int, music: Music?): Notification {
        var remoteView = RemoteViews(packageName, R.layout.layout_notification)

        music?.let {
            remoteView.setTextViewText(R.id.tvSingerNameRemote, music.singerName)
            remoteView.setTextViewText(R.id.tvSongNameRemote, music.musicName)
        }

        var intentPlay = Intent(REMOTEVIEW_PLAY)
        var penddingIntentPlay = PendingIntent.getBroadcast(this, REQUEST_ID, intentPlay, PendingIntent.FLAG_UPDATE_CURRENT)
        remoteView.setOnClickPendingIntent(R.id.ivPlayRemote, penddingIntentPlay)

        var intentPause = Intent(REMOTEVIEW_PAUSE)
        var penddingIntentPause = PendingIntent.getBroadcast(this, REQUEST_ID, intentPause, PendingIntent.FLAG_UPDATE_CURRENT)
        remoteView.setOnClickPendingIntent(R.id.ivPauseRemote, penddingIntentPause)

        var intentNext = Intent(REMOTEVIEW_NEXT)
        var penddingIntentNext = PendingIntent.getBroadcast(this, REQUEST_ID, intentNext, PendingIntent.FLAG_UPDATE_CURRENT)
        remoteView.setOnClickPendingIntent(R.id.ivNextRemote, penddingIntentNext)

        var intentLyric = Intent(REMOTEVIEW_LYRIC)
        var penddingIntentLyric = PendingIntent.getBroadcast(this, REQUEST_ID, intentLyric, PendingIntent.FLAG_UPDATE_CURRENT)
        remoteView.setOnClickPendingIntent(R.id.ivLyricRemote, penddingIntentLyric)

        if (flag == 0) {
            remoteView.setViewVisibility(R.id.ivPlayRemote, View.VISIBLE)
            remoteView.setViewVisibility(R.id.ivPauseRemote, View.GONE)
        } else {
            remoteView.setViewVisibility(R.id.ivPlayRemote, View.GONE)
            remoteView.setViewVisibility(R.id.ivPauseRemote, View.VISIBLE)
        }

        var intentMain = Intent(this, MainActivity::class.java)
        var penddingIntentMain = PendingIntent.getActivity(this, 1, intentMain, PendingIntent.FLAG_UPDATE_CURRENT)

        var builder = NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("酷狗音乐")
                .setContentIntent(penddingIntentMain)
                .setContent(remoteView)
        return builder.build()
    }

    //加载音乐
    private fun loadMusic(music: Music, isPlay: Boolean = false) {
        try {
            var temp = System.currentTimeMillis()
            mCurrentState = STATE_LOADING
            mHandler.removeMessages(0)
            sendStateChangeEvent(mCurrentState) //更新播放状态
            EventBus.getDefault().post(PlayingInfoEvent(music))//更新播放栏信息

            mPlayer?.let { releasePlayer() }
            mPlayer = MediaPlayer()
            mPlayer?.reset()
            mPlayer?.setDataSource(this, Uri.parse(music.url))
            mPlayer?.setOnErrorListener(this)
            mPlayer?.setOnCompletionListener(this)
            mPlayer?.prepareAsync()
            mPlayer?.setOnPreparedListener {
                mCurrentState = STATE_PREPRAED
                sendStateChangeEvent(mCurrentState) //更新播放状态
                if (isPlay) {
                    //两首音乐播放间隔为500毫秒
                    var time = System.currentTimeMillis() - temp
                    if (time < 500) {
                        Handler().postDelayed({ playMusic() }, 500 - time)
                    } else {
                        playMusic()
                    }
                }
                Caches.saveInt("playingIndex", mPlayingIndex)
                Android.log("音乐加载完成")
            }

        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(this, "读取文件失败", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "播放错误", Toast.LENGTH_SHORT).show()
        }
    }

    //播放音乐
    private fun playMusic() {
        mPlayer?.start()
        mCurrentState = STATE_PLAYING
        sendStateChangeEvent(mCurrentState) //更新播放状态
        mHandler.sendEmptyMessage(0)
        Android.log("播放音乐")
    }

    private fun play() {
        if (mCurrentState == STATE_PREPRAED || mCurrentState == STATE_PAUSE) {
            mCurrentState = STATE_PLAYING
            sendStateChangeEvent(mCurrentState) //更新播放状态
            startForeground(FOREGROUND_ID, getNotification(1, mPlayList[mPlayingIndex]))//更新通知栏
            mPlayer?.start()
            mHandler.sendEmptyMessage(0)
            Android.log("播放音乐")
        }
    }

    private fun pause() {
        if (mCurrentState == STATE_PLAYING) {
            mCurrentState = STATE_PAUSE
            sendStateChangeEvent(mCurrentState) //更新播放状态
            startForeground(FOREGROUND_ID, getNotification(0, mPlayList[mPlayingIndex]))//更新通知栏
            mPlayer?.pause()
            mHandler.removeMessages(0)
            Android.log("暂停音乐")
        }
    }

    private fun next() {
        if (!mPlayList.isEmpty()) {
            when (mCurrentMode) {
                MODE_RANDOM -> {
                }
                MODE_LOOP -> {
                    if (mPlayingIndex < (mPlayList.size - 1)) {
                        mPlayingIndex++
                    } else {
                        mPlayingIndex = 0
                    }
                    loadMusic(mPlayList[mPlayingIndex], true)
                }
                MODE_SEQUENCE -> {
                    if (mPlayingIndex == (mPlayList.size - 1)) {
                        mPlayingIndex = -1
                        playComplete()
                    } else {
                        mPlayingIndex++
                        loadMusic(mPlayList[mPlayingIndex], true)
                    }
                }
            }
            startForeground(FOREGROUND_ID, getNotification(1, mPlayList[mPlayingIndex]))//更新通知栏
        }
    }

    //播放完成
    private fun playComplete() {
        mCurrentState = STATE_IDLE
        releasePlayer()
        sendStateChangeEvent(mCurrentState) //更新播放状态
        Caches.saveInt("playingIndex", 0)
    }

    //播放完成的回调
    override fun onCompletion(mp: MediaPlayer?) {
        next()
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

    private fun seekToPosition(position: Int) {
        try {
            mPlayer?.seekTo(position)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun releasePlayer() {
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
        stopForeground(true)
        unregisterReceiver(receiver)
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
        var position: Int = 0
    }

    class PlayingInfoEvent(var music: Music?, var progress: Int = 0)//播放信息

    class StateChangeEvent(var state: Int)//播放状态改变事件

    inner class RemoteViewReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            intent?.let {
                when (it.action) {
                    REMOTEVIEW_PLAY -> {
                        play()
                    }
                    REMOTEVIEW_PAUSE -> {
                        pause()
                    }
                    REMOTEVIEW_NEXT -> {
                        next()
                    }
                    REMOTEVIEW_LYRIC -> {
                        Android.log("REMOTEVIEW_LYRIC")
                    }
                }
            }
        }

    }
}