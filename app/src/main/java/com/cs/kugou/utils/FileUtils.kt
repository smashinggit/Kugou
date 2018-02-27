package com.cs.kugou.utils

import android.os.Environment
import java.io.File

/**
 *
 * author : ChenSen
 * data : 2018/2/23
 * desc:
 */
object FileUtils {

    val PATH = "cKugou"
    val PATH_LYRIC = PATH + File.separator + "lyrics"  //歌词目录


    /**
     * 通过 歌手名+歌曲名 获取歌词文件
     */
    fun getLyricFiLe(fileName: String): File? {
        var root = File(getFilePath(PATH))
        var lyricDir = File(getFilePath(PATH_LYRIC))
        var lyricFile = File(getFilePath(PATH_LYRIC, fileName))


        if (!root.exists()) root.mkdir()
        if (!lyricDir.exists()) lyricDir.mkdir()

        return if (lyricFile.exists()) lyricFile else null
    }

    fun getFilePath(filePath: String) = Environment.getExternalStorageDirectory().path+ File.separator + filePath
    fun getFilePath(filePath: String, fileName: String) = Environment.getExternalStorageDirectory().path +File.separator + filePath + File.separator + fileName
}