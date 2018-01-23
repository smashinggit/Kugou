package com.cs.kugou.mvp.presenter

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import com.cs.framework.Android
import com.cs.framework.mvp.kt.KBasePresenter
import com.cs.kugou.R
import com.cs.kugou.audio.Audio
import com.cs.kugou.db.Music
import com.cs.kugou.db.User
import com.cs.kugou.module.MusicModule
import com.cs.kugou.mvp.contract.MainContract
import com.cs.kugou.ui.MainActivity
import com.raizlabs.android.dbflow.kotlinextensions.*

/**
 *
 * author : ChenSen
 * data : 2018/1/19
 * desc:
 */
class MainPresenter(var context: Context) : KBasePresenter<MainContract.Presenter, MainContract.View>(), MainContract.Presenter {

    override fun readDataFromDB() {
        MusicModule.readLocalList()
        MusicModule.readPlayList()
        MusicModule.readLikeList()
        MusicModule.readDownloadList()
        MusicModule.readRecentList()
    }

    override fun popFragment() {
        (context as MainActivity).supportFragmentManager.popBackStack()
    }

    override fun addFragment(fragment: Fragment, tag: String) {
        (context as MainActivity).supportFragmentManager
                .beginTransaction()
                .setCustomAnimations(R.anim.slide_right_in,R.anim.slide_right_in)
              //  .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(R.id.flContent, fragment, tag)
                .addToBackStack(tag)
                .commit()
    }


    override fun play() {
        Audio.play()
        ui?.showPause()
    }

    override fun pause() {
        Audio.pause()
        ui?.showPlay()
    }

    override fun next() {
        ui?.showPause()
    }

    override fun pre() {
    }

    override fun init() {
    }

    override fun destory() {
    }
}