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
data class Music_Type(
        @PrimaryKey
        var hash: String = "",

        @PrimaryKey
        var tid: Int = 0
) {
}