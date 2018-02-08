package com.cs.kugou.mvp.presenter

import android.content.Context
import android.os.Handler
import android.support.v4.app.Fragment
import android.widget.Toast
import com.cs.framework.Android
import com.cs.framework.mvp.kt.KBasePresenter
import com.cs.kugou.R
import com.cs.kugou.db.Music
import com.cs.kugou.mvp.contract.MainContract
import com.cs.kugou.mvp.moudle.MusicMoudle
import com.cs.kugou.service.PlayerService
import com.cs.kugou.ui.MainActivity
import com.cs.kugou.utils.Caches
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

/**
 *
 * author : ChenSen
 * data : 2018/1/19
 * desc:
 */
class MainPresenter(var context: Context) : KBasePresenter<MainContract.Presenter, MainContract.View>(), MainContract.Presenter {

    override fun getPlayList() {
        MusicMoudle.getPlayList(object : MusicMoudle.onReadCompletedListener {
            override fun onReadComplete(list: List<Music>) {
                MusicMoudle.playList = list as ArrayList<Music>
                //播放列表准备就绪
                Handler().postDelayed({
                    var event = PlayerService.MusicActionEvent()
                    event.action = PlayerService.ACTION_LOAD
                    event.position = Caches.queryInt("playingIndex")
                    EventBus.getDefault().post(event)
                }, 300)
            }
        })
    }

    override fun popFragment() {
        (context as MainActivity).supportFragmentManager.popBackStack()
    }

    override fun addFragment(fragment: Fragment, tag: String) {
        (context as MainActivity).supportFragmentManager
                .beginTransaction()
                .setCustomAnimations(R.anim.slide_right_in, R.anim.slide_right_in)
                //  .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .add(R.id.flContent, fragment, tag)
                .addToBackStack(tag)
                .commit()
    }


    override fun play() {
        //就绪状态和暂停状态可以播放
        if (PlayerService.mCurrentState == PlayerService.STATE_PREPRAED || PlayerService.mCurrentState == PlayerService.STATE_PAUSE) {
            sendMusicActionEvent(PlayerService.ACTION_PLAY)
        } else {
            Toast.makeText(context, "请选择一首音乐", Toast.LENGTH_SHORT).show()
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

    override fun init() {
        EventBus.getDefault().register(this)
    }

    override fun destory() {
        EventBus.getDefault().unregister(this)
    }

    //更新播放栏信息 (发送源PlayerService)
    @Subscribe()
    fun onPlayingInfoEvent(event: PlayerService.PlayingInfoEvent) {
        ui?.updateMusicInfo(event.music)
        ui?.setProgress(event.progress)
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
                ui?.hidePlay()
            }
            PlayerService.STATE_LOADING -> {
                ui?.setProgress(0)
                ui?.showPlay()
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

    //读取数据库完毕
    @Subscribe
//    fun onReadFromDBExevt(event: MusicModule.ReadFromDBExevt) {
//        // mPlayList = MusicModule.playList //播放列表
//    }

            /**
             * 向PlayerService发送指令，控制音乐
             */
    fun sendMusicActionEvent(action: Int) {
        var event = PlayerService.MusicActionEvent()
        event.action = action
        EventBus.getDefault().post(event)
    }
}