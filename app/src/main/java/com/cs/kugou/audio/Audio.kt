package com.cs.kugou.audio

import android.annotation.SuppressLint
import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.os.Handler
import android.os.Message
import com.cs.kugou.db.Music

/**
 *
 * author : ChenSen
 * data : 2018/1/19
 * desc: 此类相关代码已移到PlayerService中
 */
object Audio : MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener {

    val STATE_PAUSE = 0  //暂停
    val STATE_PLAYING = 1 //播放中
    val STATE_PREPRAED = 2//就绪
    val STATE_LOADING = 3 //加载中

    var mCurrentState = STATE_LOADING

    var mPlayer: MediaPlayer? = null
    var mListener: OnStateChangedListener? = null
    var mHandler = @SuppressLint("HandlerLeak")
    object : Handler() {
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            if (mListener != null && mCurrentState == STATE_PLAYING) {
                mListener?.onProgressChanged(getPlayPosition())
                sendEmptyMessageDelayed(0, 1000)
            }
        }
    }

    fun play() {
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


    fun loadMusic(context: Context, music: Music) {
        mHandler.removeMessages(0)

        mPlayer?.let {
            releasePlayer()
        }
        mPlayer = MediaPlayer()
        mPlayer?.reset()
        mPlayer?.setDataSource(context, Uri.parse(music.url))
        //发送消息，更新ui
        mPlayer?.setOnErrorListener(this)
        mPlayer?.setOnCompletionListener(this)
        mPlayer?.prepareAsync()
        mPlayer?.setOnPreparedListener {
            mCurrentState = STATE_PREPRAED
            mListener?.onPrepared()
            mListener?.onProgressChanged(getPlayPosition())
            mPlayer?.start()
        }
    }


    override fun onCompletion(mp: MediaPlayer?) {
        mListener?.let {
            it.onCompletion()
        }
    }

    //播放错误
    override fun onError(mp: MediaPlayer?, what: Int, extra: Int): Boolean {
        mListener?.let {
            it.onError()
        }
        return true
    }

    fun getDuration(): Int? {
        return try {
            mPlayer?.duration
        } catch (e: Exception) {
            0
        }
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

    interface OnStateChangedListener {
        fun onPrepared()
        fun onProgressChanged(progress: Int)
        fun onCompletion()
        fun onError()
    }

    fun setOnStateChangedListener(listener: OnStateChangedListener) {
        this.mListener = listener
    }


}

