package com.cs.kugou

import android.app.Application
import com.cs.framework.Android
import com.cs.kugou.utils.FileUtils
import com.raizlabs.android.dbflow.config.FlowManager
import java.io.File


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
        var isFirstSacanLocal: Boolean = true  //第一次扫描本地音乐(每次启动应用第一次进入本地音乐时扫描一次)
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