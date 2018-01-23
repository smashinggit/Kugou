package com.cs.kugou.module

import com.cs.framework.Android
import com.cs.kugou.db.Music
import com.cs.kugou.db.Music_Table
import com.raizlabs.android.dbflow.kotlinextensions.*

/**
 *
 * author : ChenSen
 * data : 2018/1/23
 * desc:
 */
object MusicModule {

    var playList = arrayListOf<Music>() //播放列表
    var loaclList = arrayListOf<Music>() //本地列表
    var likeList = arrayListOf<Music>() //喜欢列表
    var downloadList = arrayListOf<Music>() //下载播放列表
    var recentList = arrayListOf<Music>() //最近播放列表


    fun readPlayList() {
        try {
            (select from Music::class where (Music_Table.isPlay eq true))
                    .async()
                    .list { _, mutableList ->
                        playList = mutableList as ArrayList<Music>
                        Android.log("读取数据库播放列表 " + playList.size)
                    }
        } catch (e: Exception) {
            e.printStackTrace()
            Android.log("读取数据库失败 + $e")
        }
    }

    fun readLocalList() {
        try {
            (select from (Music::class) where (Music_Table.isLocal eq true))
                    .async()
                    .list { _, list ->
                        Android.log("读取数据库本地列表 " + list.size)
                        loaclList = list as ArrayList<Music>
                    }
        } catch (e: Exception) {
            e.printStackTrace()
            Android.log("读取数据库失败 + $e")
        }
    }


    fun readLikeList() {
        try {
            (select from (Music::class) where (Music_Table.isLike eq true))
                    .async()
                    .list { _, list ->
                        Android.log("读取数据库喜欢列表 " + list.size)
                        likeList = list as ArrayList<Music>
                    }
        } catch (e: Exception) {
            e.printStackTrace()
            Android.log("读取数据库失败 + $e")
        }
    }

    fun readDownloadList() {
        try {
            (select from (Music::class) where (Music_Table.isDownload eq true))
                    .async()
                    .list { _, list ->
                        Android.log("读取数据库下载列表 " + list.size)
                        downloadList = list as ArrayList<Music>
                    }
        } catch (e: Exception) {
            e.printStackTrace()
            Android.log("读取数据库失败 + $e")
        }
    }

    fun readRecentList() {
        try {
            (select from (Music::class) where (Music_Table.isRecent eq true))
                    .async()
                    .list { _, list ->
                        Android.log("读取数据库最近播放列表 " + list.size)
                        recentList = list as ArrayList<Music>
                    }
        } catch (e: Exception) {
            e.printStackTrace()
            Android.log("读取数据库失败 + $e")
        }
    }

    fun saveLocalMusic(list: ArrayList<Music>): Boolean {
        if (list.isEmpty()) return false
        try {
            for (i in 0 until list.size) {
                val music = list[i]
                music.isLocal = true
                music.save()
            }
            Android.log("写入数据库本地列表${list.size}条数据")
            return true

        } catch (e: Exception) {
            e.printStackTrace()
            Android.log("写入数据库失败 + $e")
            return false
        }

    }
}