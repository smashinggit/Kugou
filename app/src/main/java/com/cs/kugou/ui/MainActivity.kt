package com.cs.kugou.ui

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import com.cs.framework.base.BaseActivity
import com.cs.framework.mvp.kt.bind
import com.cs.framework.mvp.kt.unbind
import com.cs.kugou.R
import com.cs.kugou.mvp.contract.MainContract
import com.cs.kugou.mvp.presenter.MainPresenter
import com.cs.kugou.mvp.view.MainView
import com.cs.kugou.service.PlayerService

class MainActivity : BaseActivity() {

    lateinit var mPresenter: MainContract.Presenter

    var mBinder: PlayerService.MyBinder? = null
    var connection = object : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName?) {
        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            mBinder = service as PlayerService.MyBinder
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        var mView = MainView(this)
        mPresenter = MainPresenter(this)
        mPresenter.bind(mView)
        mPresenter.readDataFromDB()

        var intent = Intent(this, PlayerService::class.java)
        bindService(intent, connection, Context.BIND_AUTO_CREATE)
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    override fun onRestart() {
        super.onRestart()
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.unbind()
        unbindService(connection)
    }


    override fun getLayoutId(): Int = R.layout.activity_main
}
