package com.cs.kugou.mvp.contract

import com.cs.framework.mvp.kt.KPresenter
import com.cs.framework.mvp.kt.KView
import com.cs.kugou.bean.Lyric
import com.cs.kugou.db.Music

/**
 *
 * author : ChenSen
 * data : 2018/2/22
 * desc:
 */
interface MusicContract {

    interface Presenter : KPresenter<Presenter, MusicContract.View> {
        fun play()
        fun pause()
        fun next()
        fun pre()
    }

    interface View : KView<Presenter, View> {
        fun showPlay()
        fun showPause()
        fun updateMusicInfo(music: Music?)
        fun setProgress(progress: Int)
        fun setLyric(lyric: Lyric)
        fun resetLyric()
    }

}