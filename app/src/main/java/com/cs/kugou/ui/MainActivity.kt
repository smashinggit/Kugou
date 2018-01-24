package com.cs.kugou.ui

import android.content.Intent
import android.os.Bundle
import com.cs.framework.Android
import com.cs.framework.base.BaseActivity
import com.cs.framework.mvp.kt.bind
import com.cs.framework.mvp.kt.unbind
import com.cs.kugou.R
import com.cs.kugou.mvp.contract.MainContract
import com.cs.kugou.mvp.presenter.MainPresenter
import com.cs.kugou.mvp.view.MainView
import com.cs.kugou.service.PlayerService
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

class MainActivity : BaseActivity() {

    lateinit var mPresenter: MainContract.Presenter
    lateinit var mView: MainView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        EventBus.getDefault().register(this)

        var intent = Intent(this, PlayerService::class.java)
        startService(intent)

        mView = MainView(this)
        mPresenter = MainPresenter(this)
        mPresenter.bind(mView)
        mPresenter.readDataFromDB()

    }

    @Subscribe
    fun onProgressEvent(event: ProgressEvent) {
        mView.setProgress(event.progress)
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }


    override fun onDestroy() {
        EventBus.getDefault().unregister(this)
        mPresenter.unbind()
        super.onDestroy()
    }


    override fun getLayoutId(): Int = R.layout.activity_main

    class ProgressEvent(var progress: Int) {}
}
