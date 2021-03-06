package com.cs.kugou.mvp.presenter

import android.widget.Toast
import com.cs.framework.Android
import com.cs.framework.mvp.kt.KBasePresenter
import com.cs.kugou.R
import com.cs.kugou.bean.Lyric
import com.cs.kugou.db.Music
import com.cs.kugou.mvp.contract.MusicContract
import com.cs.kugou.service.PlayerService
import com.cs.kugou.ui.MusicActivity
import com.cs.kugou.utils.LyricUtils
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

/**
 *
 * author : ChenSen
 * data : 2018/2/22
 * desc:
 */
class MusicPresenter(var mContext: MusicActivity) : KBasePresenter<MusicContract.Presenter, MusicContract.View>(), MusicContract.Presenter {


    override fun play() {
        //就绪状态和暂停状态可以播放
        if (PlayerService.mCurrentState == PlayerService.STATE_PREPRAED || PlayerService.mCurrentState == PlayerService.STATE_PAUSE) {
            sendMusicActionEvent(PlayerService.ACTION_PLAY)
        } else {
            Toast.makeText(mContext, mContext.resources.getString(R.string.tip_select_music), Toast.LENGTH_SHORT).show()
        }
    }

    override fun pause() {
        //播放状态可以暂停
        if (PlayerService.mCurrentState == PlayerService.STATE_PLAYING) {
            sendMusicActionEvent(PlayerService.ACTION_PAUSE)
        }
    }

    override fun next() {
        if (!PlayerService.mPlayList.isEmpty()) {
            sendMusicActionEvent(PlayerService.ACTION_NEXT)
        }
    }

    override fun pre() {
    }


    //更新播放栏信息 (发送源PlayerService)
    @Subscribe()
    fun onPlayingInfoEvent(event: PlayerService.PlayingInfoEvent) {
        ui?.updateMusicInfo(event.music)
        ui?.setProgress(event.progress)
    }

    //更新歌词 (发送源PlayerService)
    @Subscribe
    fun onLyricChangedEvent(event: PlayerService.LyricChangeEvent) {
        ui?.setLyric(event.lyric)
    }

    //更新播放状态 (发送源PlayerService)
    @Subscribe
    fun onStateChangeEvent(event: PlayerService.StateChangeEvent) {
        when (event.state) {
            PlayerService.STATE_IDLE -> {
                var default = Music()
                default.musicName = "酷狗音乐"
                default.singerName = "hello kugou"
                default.duration = 100
                ui?.setProgress(0)
                ui?.updateMusicInfo(default)
                ui?.showPlay()
            }
            PlayerService.STATE_LOADING -> {
                ui?.setProgress(0)
                ui?.showPlay()
                ui?.resetLyric()
            }
            PlayerService.STATE_PREPRAED -> {
                ui?.showPlay()
            }
            PlayerService.STATE_PLAYING -> {
                ui?.showPause()
            }
            PlayerService.STATE_PAUSE -> {
                ui?.showPlay()
            }
        }
    }

    /**
     * 向PlayerService发送指令，控制音乐
     */
    private fun sendMusicActionEvent(action: Int) {
        var event = PlayerService.MusicActionEvent()
        event.action = action
        EventBus.getDefault().post(event)
    }

    override fun init() {
        EventBus.getDefault().register(this)
        if (PlayerService.mCurrentState == PlayerService.STATE_PLAYING) {
            ui?.showPause()
        }
        PlayerService.mLyric?.let {
            ui?.setLyric(it)
        }
    }

    override fun destory() {
        EventBus.getDefault().unregister(this)
    }
}