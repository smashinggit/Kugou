package com.cs.kugou.mvp.contract

import android.support.v4.app.Fragment
import com.cs.framework.mvp.kt.KPresenter
import com.cs.framework.mvp.kt.KView
import com.cs.kugou.db.Music

/**
 *
 * author : ChenSen
 * data : 2018/1/19
 * desc:
 */
interface MainContract {


    interface Presenter : KPresenter<Presenter, View> {
        fun play()
        fun pause()
        fun next()
        fun pre()
        fun addFragment(fragment: Fragment, tag: String)
        fun popFragment()
        fun readDataFromDB()
    }

    interface View : KView<Presenter, View> {
        fun showPlay()
        fun showPause()
        fun updateMusicInfo(music: Music?)
        fun showFragment(isShow: Boolean)
        fun setProgress(progress: Int)
    }


}