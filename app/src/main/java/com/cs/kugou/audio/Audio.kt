package com.cs.kugou.audio

import android.annotation.SuppressLint
import android.content.Context
import android.media.MediaPlayer
import android.os.Handler
import android.os.Message
import com.cs.framework.Android

/**
 *
 * author : ChenSen
 * data : 2018/1/19
 * desc:
 */
object Audio {

    val STATE_PAUSE = 0  //暂停
    val STATE_PLAYING = 1 //播放中
    val STATE_PREPRAED = 2//就绪
    val STATE_Loading = 3 //加载中

    var mCurrentState = STATE_Loading


    var mPlayer: MediaPlayer = MediaPlayer()
    var mListener: OnStateChangedListener? = null
    var mHandler = @SuppressLint("HandlerLeak")
    object : Handler() {
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            if (mListener != null) {
                mListener?.onProgressChanged(getPlayPosition())
                sendEmptyMessageDelayed(0, 1000)
            }
        }
    }

    fun play() {
        if (mCurrentState == STATE_PREPRAED || mCurrentState == STATE_PAUSE) {
            mCurrentState = STATE_PLAYING
            mPlayer.start()
        }
    }

    fun pause() {
        if (mCurrentState == STATE_PLAYING) {
            mCurrentState = STATE_PAUSE
            mPlayer.pause()
        }
    }


    fun loadingMusic(context: Context) {
        mHandler.removeMessages(0)
        val fileDescriptor = context.assets.openFd("好可惜.mp3")
        mPlayer.reset()
        mPlayer.setDataSource(fileDescriptor.fileDescriptor, fileDescriptor.startOffset, fileDescriptor.length)
        mPlayer.prepareAsync()
        mPlayer.setOnPreparedListener {
            mCurrentState = STATE_PREPRAED
            mListener?.onPrepared()
            mHandler.sendEmptyMessage(0)
        }
    }


    fun getDuration(): Int {
        return try {
            mPlayer.duration
        } catch (e: Exception) {
            0
        }
    }


    fun getPlayPosition(): Int {
        return try {
            mPlayer.currentPosition
        } catch (e: Exception) {
            0
        }
    }

    fun seekToPositon(position: Int) {
        try {
            mPlayer.seekTo(position)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun releasePlayer() {
        mHandler.removeMessages(0)
        mPlayer.stop()
        mPlayer.release()
    }

    interface OnStateChangedListener {
        fun onPrepared()
        fun onProgressChanged(progress: Int)
    }

    fun setOnStateChangedListener(listener: OnStateChangedListener) {
        this.mListener = listener
    }


}

