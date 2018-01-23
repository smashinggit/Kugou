package com.cs.kugou.db

import com.raizlabs.android.dbflow.annotation.Column
import com.raizlabs.android.dbflow.annotation.PrimaryKey
import com.raizlabs.android.dbflow.annotation.Table

/**
 *
 * author : ChenSen
 * data : 2018/1/23
 * desc:
 */
@Table(database = KgDataBase::class)
data class User(@PrimaryKey(autoincrement = true)
                var id: Int = 0,

                @Column
                var name: String? = null,

                @Column
                var age: Int? = null) {


}