package com.cs.kugou.bean

import java.util.*

/**
 *
 * author : ChenSen
 * data : 2018/2/27
 * desc:
 */
data class Lyric(var lyricType: String = "krc",                            //歌词格式
                 var lyricLineInfo: TreeMap<Int, LyricLine>? = null,       //歌词每一行数据
                 var lyricTag: Map<String, Any>? = null) {


    //每一行歌词
    data class LyricLine(var startTime: Int = 0,                  //开始时间
                         var endTime: Int = 0,                    //结束时间
                         var lineLyric: String = "",              //该行歌词
                         var lyricWords: Array<String?>? = null,           // 歌词数组，用来分割每个歌词
                         var wordInterval: Array<Int?>? = null) {          //存放每个歌词的时间

    }

    //歌词标签
    object LyricTag {
        val TAG_TITLE = "title"
        val TAG_ARTIST = "artist"
        val TAG_OFFSET = "offset"   //时间补偿值
        val TAG_BY = "by"           //上传者
        val TAG_TOTAL = "TOTAL"     //歌词总时长
    }
}