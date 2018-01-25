package com.cs.kugou.mvp.presenter

import android.content.Context
import android.support.v4.app.Fragment
import android.widget.Toast
import com.cs.framework.Android
import com.cs.framework.mvp.kt.KBasePresenter
import com.cs.kugou.R
import com.cs.kugou.module.MusicModule
import com.cs.kugou.mvp.contract.MainContract
import com.cs.kugou.service.PlayerService
import com.cs.kugou.ui.MainActivity
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

/**
 *
 * author : ChenSen
 * data : 2018/1/19
 * desc:
 */
class MainPresenter(var context: Context) : KBasePresenter<MainContract.Presenter, MainContract.View>(), MainContract.Presenter {

    override fun readDataFromDB() {
        MusicModule.readMusicFromDB(MusicModule.PLAY)
        MusicModule.readMusicFromDB(MusicModule.LOCAL)
        MusicModule.readMusicFromDB(MusicModule.LIKE)
        MusicModule.readMusicFromDB(MusicModule.DOWNLOAD)
        MusicModule.readMusicFromDB(MusicModule.RECENT)
    }

    override fun popFragment() {
        (context as MainActivity).supportFragmentManager.popBackStack()
    }

    override fun addFragment(fragment: Fragment, tag: String) {
        (context as MainActivity).supportFragmentManager
                .beginTransaction()
                .setCustomAnimations(R.anim.slide_right_in, R.anim.slide_right_in)
                //  .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(R.id.flContent, fragment, tag)
                .addToBackStack(tag)
                .commit()
    }


    override fun play() {
        Android.log("play ${PlayerService.mCurrentState}")
        //就绪状态和暂停状态可以播放
        if (PlayerService.mCurrentState == PlayerService.STATE_PREPRAED || PlayerService.mCurrentState == PlayerService.STATE_PAUSE) {
            sendMusicEvent(PlayerService.ACTION_PLAY)
            ui?.showPause()
        }else {
            Toast.makeText(context, "请选择一首音乐", Toast.LENGTH_SHORT).show()
        }
    }

    override fun pause() {
        //播放状态可以暂停
        if (PlayerService.mCurrentState == PlayerService.STATE_PLAYING) {
            sendMusicEvent(PlayerService.ACTION_PAUSE)
            ui?.showPlay()
        }
    }

    override fun next() {
        sendMusicEvent(PlayerService.ACTION_NEXT)
        ui?.showPause()
    }

    override fun pre() {
    }

    override fun init() {
        EventBus.getDefault().register(this)
    }

    override fun destory() {
        EventBus.getDefault().unregister(this)
    }

    //更新播放进度
    @Subscribe()
    fun onProgressEnvent(progressEvent: PlayerService.ProgressEvent) {
        ui?.setProgress(progressEvent.progress)
    }

    //更新音乐信息
    @Subscribe()
    fun onMusicEvent(event: PlayerService.MusicEvent) {
        Android.log("更新音乐信息")
        ui?.shwoMusicInfo(event.music)
    }

    /**
     * 向PlayerService发送指令，控制音乐
     */
    fun sendMusicEvent(action: Int) {
        var event = PlayerService.MusicEvent()
        event.action = action
        EventBus.getDefault().post(event)
    }
}