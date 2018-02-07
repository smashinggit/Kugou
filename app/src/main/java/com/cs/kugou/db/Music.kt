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
data class Music(@PrimaryKey(autoincrement = false)
                 @Column
                 var hash: String = "",

                 @Column
                 var location: Int = 0,//位置：0 本地   1 网络

                 @Column
                 var status: Int = 0,//状态：0 完成   1 下载中   2初始化中

                 @Column
                 var musicName: String = "", //歌名

                 @Column
                 var singerName: String = "", //歌手

                 @Column
                 var album: String = "", //专辑

                 @Column
                 var url: String = "", //url

                 @Column
                 var year: String? = null, //年份

                 @Column
                 var duration: Int = 0, //时长

                 @Column
                 var size: Long = 0, //大小

                 @Column
                 var isFree: Boolean? = false, //是否免费下载

                 @Column
                 var isMV: Boolean? = false //是否有MV

) {

}