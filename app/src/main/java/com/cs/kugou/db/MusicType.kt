package com.cs.kugou.db

import com.raizlabs.android.dbflow.annotation.PrimaryKey
import com.raizlabs.android.dbflow.annotation.Table

/**
 *
 * author : ChenSen
 * data : 2018/2/6
 * desc: 关系表
 */
@Table(database = KgDataBase::class)
data class MusicType(
        @PrimaryKey
        var mHash: String = "",

        @PrimaryKey
        var tid: Int = 0
)