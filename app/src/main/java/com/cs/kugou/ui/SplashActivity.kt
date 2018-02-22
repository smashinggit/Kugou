package com.cs.kugou.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import com.cs.framework.Android
import com.cs.framework.base.BaseActivity
import com.cs.kugou.R
import com.cs.kugou.db.Music
import com.cs.kugou.mvp.moudle.MusicMoudle
import com.cs.kugou.utils.Caches
import com.cs.kugou.utils.MediaUtils

/**
 *
 * author : ChenSen
 * data : 2018/2/6
 * desc:
 */
class SplashActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Caches.isFirst())
            scanLocalMusic()
        else {
            gotoHome(2000)
        }
    }

    private fun scanLocalMusic() {
        var list = arrayListOf<Music>()
        var temp = System.currentTimeMillis()
        //扫描本地歌曲
        MediaUtils.scanLocalMusicByContentResolver(this, object : MediaUtils.ForeachListener {
            override fun foreach(music: Music) {
                list.add(music)
            }

            override fun filter(hash: String): Boolean {
                return MusicMoudle.isExistMusic(hash)
            }

            override fun onComplete() {
                //保存到数据库
                if (list.size > 0) {
                    MusicMoudle.insert(list)
                    MusicMoudle.localList = list
                }

                Caches.setIsFirst(false)
                Caches.save("localCount", "${list.size}")

                var def = System.currentTimeMillis() - temp
                if (def >= 2000) gotoHome(0) else gotoHome(2000 - def)
                Android.log("扫描本地歌曲 共${list.size} 首   用时 $def 毫秒")
            }
        })
    }

    private fun gotoHome(delay: Long) {
        Handler().postDelayed({
            var intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, delay)
    }

    override fun getLayoutId() = R.layout.activity_splash
}