package com.cs.kugou.lyric

import com.cs.kugou.bean.Lyric
import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import java.nio.charset.Charset

/**
 *
 * author : ChenSen
 * data : 2018/2/27
 * desc:  歌词读取器
 */
abstract class LyricReader {

    var defaultCharset = Charset.forName("utf-8")


    //读取歌词文件
    @Throws(Exception::class)
    fun readFile(file: File): Lyric? {
        return if (file != null) readInputStream(FileInputStream(file)) else null
    }


    //读取歌词文件
    abstract fun readInputStream(inputStream: InputStream): Lyric


}