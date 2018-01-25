package com.cs.kugou

import android.app.Application
import com.cs.framework.Android
import com.raizlabs.android.dbflow.config.FlowManager


/**
 * ************************************************************************
 * **                              _oo0oo_                               **
 * **                             o8888888o                              **
 * **                             88" . "88                              **
 * **                             (| -_- |)                              **
 * **                             0\  =  /0                              **
 * **                           ___/'---'\___                            **
 * **                        .' \\\|     |// '.                          **
 * **                       / \\\|||  :  |||// \\                        **
 * **                      / _ ||||| -:- |||||- \\                       **
 * **                      | |  \\\\  -  /// |   |                       **
 * **                      | \_|  ''\---/''  |_/ |                       **
 * **                      \  .-\__  '-'  __/-.  /                       **
 * **                    ___'. .'  /--.--\  '. .'___                     **
 * **                 ."" '<  '.___\_<|>_/___.' >'  "".                  **
 * **                | | : '-  \'.;'\ _ /';.'/ - ' : | |                 **
 * **                \  \ '_.   \_ __\ /__ _/   .-' /  /                 **
 * **            ====='-.____'.___ \_____/___.-'____.-'=====             **
 * **                              '=---='                               **
 * ************************************************************************
 * **                        佛祖保佑      镇类之宝                        **
 * ************************************************************************
 */

/**
 *
 * author : ChenSen
 * data : 2018/1/22
 * desc:
 */
class App : Application() {

    companion object {
        lateinit var self: App
    }

    override fun onCreate() {
        super.onCreate()
        self = this
        initDB()
    }

    private fun initDB() {
        Android.log("初始化数据库")
        FlowManager.init(this)// 初始化
    }
}