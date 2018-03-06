package com.cs.kugou.ui

import android.os.Bundle
import com.cs.framework.base.BaseActivity
import com.cs.framework.mvp.kt.bind
import com.cs.kugou.R
import com.cs.kugou.mvp.contract.MusicContract
import com.cs.kugou.mvp.presenter.MusicPresenter
import com.cs.kugou.mvp.view.MusicView

/**
 *
 * author : ChenSen
 * data : 2018/2/22
 * desc: 播放音乐页面
 */
class MusicActivity : BaseActivity() {
    lateinit var mPresenter: MusicContract.Presenter
    lateinit var mView: MusicContract.View

    override fun getLayoutId(): Int = R.layout.activity_music

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mView = MusicView(this)
        mPresenter = MusicPresenter(this)
        mPresenter.bind(mView)
    }
}