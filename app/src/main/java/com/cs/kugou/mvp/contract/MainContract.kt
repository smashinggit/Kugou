package com.cs.kugou.mvp.contract

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
    }

    interface View : KView<Presenter, View> {
        fun play()
        fun pause()
        fun next()
        fun pre()
    }


}