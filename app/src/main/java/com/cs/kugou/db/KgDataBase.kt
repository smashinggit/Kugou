package com.cs.kugou.db

import com.raizlabs.android.dbflow.annotation.Database

/**
 *
 * author : ChenSen
 * data : 2018/1/23
 * desc:
 */
@Database(version = KgDataBase.VERSION,name = KgDataBase.NAME)
object KgDataBase {
    const val VERSION = 1
    const val NAME = "kugou"

}