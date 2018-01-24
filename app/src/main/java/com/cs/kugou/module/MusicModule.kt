package com.cs.kugou.module

import com.cs.framework.Android
import com.cs.kugou.db.Music
import com.cs.kugou.db.Music_Table
import com.cs.kugou.service.PlayerService
import com.raizlabs.android.dbflow.kotlinextensions.*
import org.greenrobot.eventbus.EventBus

/**
 *
 * author : ChenSen
 * data : 2018/1/23
 * desc:
 */
object MusicModule {
    const val LOCAL = "isLoacl"
    const val LIKE = "isLike"
    const val DOWNLOAD = "isDownload"
    const val RECENT = "isRecent"
    const val PLAY = "isPlay"

    var playList = arrayListOf<Music>() //播放列表
    var localList = arrayListOf<Music>() //本地列表
    var likeList = arrayListOf<Music>() //喜欢列表
    var downloadList = arrayListOf<Music>() //下载播放列表
    var recentList = arrayListOf<Music>() //最近播放列表


    /**
     * 从数据库读取音乐
     */
    fun readMusicFromDB(flag: String) {
        try {
            when (flag) {
                LOCAL -> {
                    var temp = System.currentTimeMillis()
                    (select from Music::class where (Music_Table.isLocal eq true))
                            .async()
                            .list { _, mutableList ->
                                localList = mutableList as ArrayList<Music>
                                Android.log("读取数据库本地列表，共${localList.size}条数据,耗时 ${System.currentTimeMillis() - temp} 毫秒 ")
                            }
                }
                LIKE -> {
                    var temp = System.currentTimeMillis()
                    (select from Music::class where (Music_Table.isLike eq true))
                            .async()
                            .list { _, mutableList ->
                                likeList = mutableList as ArrayList<Music>
                                Android.log("读取数据库喜欢列表，共${likeList.size}条数据,耗时 ${System.currentTimeMillis() - temp} 毫秒 ")
                            }
                }
                DOWNLOAD -> {
                    var temp = System.currentTimeMillis()
                    (select from Music::class where (Music_Table.isDownload eq true))
                            .async()
                            .list { _, mutableList ->
                                downloadList = mutableList as ArrayList<Music>
                                Android.log("读取数据库下载列表，共${downloadList.size}条数据,耗时 ${System.currentTimeMillis() - temp} 毫秒 ")
                            }
                }
                RECENT -> {
                    var temp = System.currentTimeMillis()
                    (select from Music::class where (Music_Table.isRecent eq true))
                            .async()
                            .list { _, mutableList ->
                                recentList = mutableList as ArrayList<Music>
                                Android.log("读取数据库最近播放列表，共${recentList.size}条数据,耗时 ${System.currentTimeMillis() - temp} 毫秒 ")
                            }
                }

                PLAY -> {
                    var temp = System.currentTimeMillis()
                    (select from Music::class where (Music_Table.isPlay eq true))
                            .async()
                            .list { _, mutableList ->
                                playList = mutableList as ArrayList<Music>
                                Android.log("读取数据库播放列表，共${playList.size}条数据,耗时 ${System.currentTimeMillis() - temp} 毫秒 ")

                                var event = PlayerService.MusicEvent(PlayerService.ACTION_READY)
                                EventBus.getDefault().post(event)
                            }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Android.log("读取数据库失败 + $e")
        }
    }


    /**
     * 保存音乐到数据库
     */
    fun saveMusicToDB(list: ArrayList<Music>, flag: String): Boolean {
        if (list.isEmpty()) return false
        var saveCount = 0
        var updateCount = 0
        try {
            for (i in 0 until list.size) {
                val music = list[i]

                when (flag) {
                    LOCAL -> music.isLocal = true
                    LIKE -> music.isLike = true
                    DOWNLOAD -> music.isDownload = true
                    RECENT -> music.isRecent = true
                    PLAY -> music.isPlay = true
                    PLAY -> music.isPlay = true
                }

                if (isExistInDb(music)) {
                    music.update()
                    updateCount++
                } else {
                    music.save()
                    saveCount++
                    Android.log("写入数据库，id为 ${music.id}")
                }
            }
            Android.log("写入数据库${saveCount}条数据")
            Android.log("更新数据库${updateCount}条数据")
            return true
        } catch (e: Exception) {
            e.printStackTrace()
            Android.log("写入数据库失败 + $e")
            return false
        }
    }


    /**
     * 判断一个音乐是否已经存在
     */
    private fun isExistInDb(music: Music): Boolean {
        var result = false
        try {
            (select from (Music::class) where (Music_Table.id eq music.id))
                    .result
                    ?.let {
                        result = true
                    }
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
//        if (result)
//            Android.log("数据库中已经存在此歌曲，id为 ${music.id}")
        return result

    }



}