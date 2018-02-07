package com.cs.kugou.mvp.moudle

import com.cs.framework.Android
import com.cs.kugou.db.*
import com.raizlabs.android.dbflow.kotlinextensions.*
import com.raizlabs.android.dbflow.sql.language.*

/**
 * author :  chensen
 * data  :  2018/2/6
 * desc :
 */
object MusicMoudle {
    var localList = ArrayList<Music>() //本地列表
    var playList = ArrayList<Music>() //播放列表

    //读取本地列表
    fun getLoacalList(listener: onReadCompletedListener) {

        (select from Music::class
                where (Music_Table.location eq 0))
                .async() list { _, list ->
            list?.let {
                listener.onReadComplete(it)
            }
            Android.log("查询本地音乐- ${list.size}")
        }
    }

    //读取播放列表
    fun getPlayList(listener: onReadCompletedListener) {

        SQLite.select(Music::class.allProperty())
                .from(Music::class)
                .join(MusicType::class.java, Join.JoinType.INNER)
                .on((MusicType_Table.tid eq Type.PLAY)
                        and (MusicType_Table.mHash eq Music_Table.hash))
                .async() list { _, list ->
            listener.onReadComplete(list)
            Android.log("查询播放列表- ${list.size}")
        }
    }

    //更新播放列表
    fun savePlayListToDB(list: ArrayList<Music>) {

        clearPlayList(object : onDeletCompletedListener {
            override fun onDeletComplete() {

                //如果播放列表没有此音乐，则保存
                list.filterNot { isExistType(it.hash, Type.PLAY) }
                        .forEach { insert(MusicType(it.hash, Type.PLAY)) }
                //如果数据库列表没有此音乐，则保存
                list.filterNot { isExistMusic(it.hash) }
                        .forEach { insert(it) }
                Android.log("保存播放列表 ${list.size}")
            }
        })
    }

    //清空播放列表
    private fun clearPlayList(listener: onDeletCompletedListener) {
        //删除旧的播放列表
        SQLite.delete(MusicType::class.java)
                .where(MusicType_Table.tid eq Type.PLAY)
                .async() list { _, _ ->
            Android.log("清空播放列表")
            listener.onDeletComplete()
        }
    }


    fun insert(music: Music) {
        music.async().save()
        Android.log("插入一条数据 ${music.hash}")
    }

    fun insert(type: MusicType) {
        type.async().save()
        Android.log("插入一条类型 ${type.tid}")
    }

    fun insert(list: ArrayList<Music>) {
        for (item in list) {
            insert(item)
        }
    }


    //判断是否存在
    fun isExistMusic(hash: String): Boolean {

        (select from Music::class
                where (Music_Table.hash eq hash)
                ).queryList().let {

            if (it.size > 0) {
                Android.log("数据库已存在Music $hash")
                return true
            }
        }
        return false
    }

    //判断是否存在
    fun isExistType(hash: String, type: Int): Boolean {

        (select from MusicType::class
                where (MusicType_Table.mHash eq hash
                and (MusicType_Table.tid eq type))
                ).queryList().let {

            if (it.size > 0) {
                Android.log("数据库已存在Type $hash")
                return true
            }
        }
        return false
    }

    interface onReadCompletedListener {
        fun onReadComplete(list: List<Music>)
    }

    interface onDeletCompletedListener {
        fun onDeletComplete()
    }
}