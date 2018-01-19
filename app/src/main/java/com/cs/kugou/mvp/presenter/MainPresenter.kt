package com.cs.kugou.mvp.presenter

import android.view.View
import com.cs.framework.mvp.kt.KBasePresenter
import com.cs.framework.mvp.kt.KPresenter
import com.cs.kugou.audio.Audio
import com.cs.kugou.mvp.contract.MainContract
import com.cs.kugou.mvp.view.MainView

/**
 *
 * author : ChenSen
 * data : 2018/1/19
 * desc:
 */
class MainPresenter : KBasePresenter<MainContract.Presenter, MainContract.View>(), MainContract.Presenter {
    override fun play() {
        Audio.play()
    }

    override fun pause() {
        Audio.pause()
    }

    override fun next() {
    }

    override fun pre() {
    }

    override fun init() {
    }

    override fun destory() {
    }
}