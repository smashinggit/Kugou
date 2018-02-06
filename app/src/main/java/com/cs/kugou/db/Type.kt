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
 *  0|本地
 *  1|网络
 *  2|喜欢
 *  3|下载
 *  4|最近
 *  5|播放
 *
 */
@Table(database = KgDataBase::class)
data class Type(
        @PrimaryKey
        @Column
        var id: Int = 0,

        @Column
        var name: String = "本地") {
}