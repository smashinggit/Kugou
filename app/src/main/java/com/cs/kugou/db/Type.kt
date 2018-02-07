package com.cs.kugou.db

import com.raizlabs.android.dbflow.annotation.Column
import com.raizlabs.android.dbflow.annotation.PrimaryKey
import com.raizlabs.android.dbflow.annotation.Table

/**
 *
 * author : ChenSen
 * data : 2018/2/6
 * desc: 类型表
 *
 *  0|喜欢
 *  1|下载
 *  2|最近
 *  3|播放
 *
 */
@Table(database = KgDataBase::class)
data class Type(
        @PrimaryKey
        @Column
        var id: Int = 0,

        @Column
        var name: String = "喜欢") {

    companion object {
        const val LIKE = 0
        const val DOWNLOAD = 1
        const val RECENT = 2
        const val PLAY = 3
    }
}