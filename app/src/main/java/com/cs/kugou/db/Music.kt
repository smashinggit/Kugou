package com.cs.kugou.db

import com.raizlabs.android.dbflow.annotation.Column
import com.raizlabs.android.dbflow.annotation.PrimaryKey
import com.raizlabs.android.dbflow.annotation.Table
import java.io.Serializable

/**
 *
 * author : ChenSen
 * data : 2018/1/23
 * desc:
 */
@Table(database = KgDataBase::class)
data class Music(@PrimaryKey(autoincrement = true)
                 var id: Int = 0,

                 @Column
                 var name: String? = null, //歌名

                 @Column
                 var artist: String? = null, //歌手

                 @Column
                 var album: String? = null, //专辑

                 @Column
                 var url: String? = null, //url

                 @Column
                 var year: String? = null, //年份

                 @Column
                 var duration: Int? = null, //时长

                 @Column
                 var size: Long? = null, //大小

                 @Column
                 var isLocal: Boolean? = false, //是否在本地

                 @Column
                 var isLike: Boolean? = false, //是否喜欢

                 @Column
                 var isDownload: Boolean? = false, //是否已下载

                 @Column
                 var isRecent: Boolean? = false, //是否最近播放

                 @Column
                 var isPlay: Boolean? = false, //是否在播放列表

                 @Column
                 var isPlaying: Boolean? = false, //是否正在播放

                 @Column
                 var progress: Int? = 0, //当前播放进度

                 @Column
                 var isFree: Boolean? = false, //是否免费下载

                 @Column
                 var isMV: Boolean? = false //是否有MV

)  {

}