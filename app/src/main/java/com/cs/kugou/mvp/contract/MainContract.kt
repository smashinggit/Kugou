package com.cs.kugou.mvp.contract

import android.content.Intent
import android.support.v4.app.Fragment
import com.cs.framework.mvp.kt.KPresenter
import com.cs.framework.mvp.kt.KView

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
        fun addFragment(fragment: Fragment,tag:String)
        fun popFragment()
    }

    interface View : KView<Presenter, View> {
        fun showPlay()
        fun showPause()
        fun showFragment(isShow: Boolean)
    }


}