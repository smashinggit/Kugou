package com.cs.kugou.mvp.presenter

import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment
import com.cs.framework.mvp.kt.KBasePresenter
import com.cs.kugou.R
import com.cs.kugou.audio.Audio
import com.cs.kugou.mvp.contract.MainContract
import com.cs.kugou.ui.MainActivity

/**
 *
 * author : ChenSen
 * data : 2018/1/19
 * desc:
 */
class MainPresenter(var context: Context) : KBasePresenter<MainContract.Presenter, MainContract.View>(), MainContract.Presenter {


    override fun showFragment(fragment: Fragment) {
        (context as MainActivity).supportFragmentManager
                .beginTransaction()
                .replace(R.id.flContent, fragment).commit()
        ui?.showFragment(true)
    }

    override fun hideFragment() {
        ui?.showFragment(false)
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