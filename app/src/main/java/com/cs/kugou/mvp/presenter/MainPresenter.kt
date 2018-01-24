package com.cs.kugou.mvp.presenter

import android.content.Context
import android.support.v4.app.Fragment
import com.cs.framework.mvp.kt.KBasePresenter
import com.cs.kugou.R
import com.cs.kugou.module.MusicModule
import com.cs.kugou.mvp.contract.MainContract
import com.cs.kugou.service.PlayerService
import com.cs.kugou.ui.MainActivity
import org.greenrobot.eventbus.EventBus

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
        sendMusicEvent(PlayerService.ACTION_PLAY)
        ui?.showPause()
    }

    override fun pause() {
        sendMusicEvent(PlayerService.ACTION_PAUSE)
        ui?.showPlay()
    }

    override fun next() {
        sendMusicEvent(PlayerService.ACTION_NEXT)
        ui?.showPause()
    }

    override fun pre() {
    }

    override fun init() {
    }

    override fun destory() {
    }


    /**
     * 向PlayerService发送指令，控制音乐
     */
    fun sendMusicEvent(action: Int) {
        var event = PlayerService.MusicEvent(action)
        EventBus.getDefault().post(event)
    }
}