package com.cs.kugou.ui

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import com.cs.framework.base.BaseActivity
import com.cs.framework.mvp.kt.bind
import com.cs.framework.mvp.kt.unbind
import com.cs.kugou.R
import com.cs.kugou.db.Music
import com.cs.kugou.mvp.contract.MainContract
import com.cs.kugou.mvp.presenter.MainPresenter
import com.cs.kugou.mvp.view.MainView
import com.cs.kugou.service.PlayerService
import com.cs.kugou.utils.Caches
import com.google.gson.Gson
import org.greenrobot.eventbus.EventBus

class MainActivity : BaseActivity() {

    lateinit var mPresenter: MainContract.Presenter
    lateinit var mView: MainView
    var topCount = 0    //用于标识MainActivity上Fragemnt的数量


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var intent = Intent(this, PlayerService::class.java)
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            startForegroundService(intent)
//        } else {
//            startService(intent)
//        }

        startService(intent)

        mView = MainView(this)
        mPresenter = MainPresenter(this)
        mPresenter.bind(mView)
        mPresenter.getPlayList()
    }

    override fun onBackPressed() {

        if (topCount == 0) {     // 跳转到桌面
            val intent = Intent(Intent.ACTION_MAIN)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            intent.addCategory(Intent.CATEGORY_HOME)
            startActivity(intent)
        } else {
            mPresenter.popFragment()
        }

    }

    override fun onDestroy() {
        mPresenter.unbind()
        super.onDestroy()
    }


    override fun getLayoutId(): Int = R.layout.activity_main

}
