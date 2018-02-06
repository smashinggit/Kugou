package com.cs.kugou.utils

import com.cs.framework.Android
import com.cs.kugou.db.Music
import com.cs.kugou.db.Music_Table
import com.raizlabs.android.dbflow.kotlinextensions.*

/**
 *
 * author : ChenSen
 * data : 2018/2/6
 * desc:
 */
object DBUtils {


    fun insert(music: Music) {
        music.async().save()
        Android.log("插入一条数据")
    }

    fun insert(list: ArrayList<Music>) {
        for (item in list) {
            insert(item)
        }
    }

    fun delete() {

    }

    fun undate() {
    }

    //判断是否存在
    fun isExist(hash: String): Boolean {

        (select from Music::class
                where (Music_Table.hash eq hash)
                ).queryList().let {

            if (it.size > 0) {
                Android.log("数据库已存在 $hash")
                return true
            }
        }
        return false
    }


}