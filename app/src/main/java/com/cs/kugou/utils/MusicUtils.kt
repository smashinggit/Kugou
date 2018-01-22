package com.cs.kugou.utils

import android.content.Context
import android.database.Cursor
import android.provider.MediaStore
import com.cs.framework.Android
import com.cs.kugou.bean.Music


/**
 * Created by chensen on 2018/1/21.
 */
object MusicUtils {

    fun getLocalMusic(cotext: Context): ArrayList<Music> {
        var musicList = arrayListOf<Music>()
        var cursor: Cursor? = null
        try {
            cursor = cotext.contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null
                    , null, MediaStore.Audio.AudioColumns.IS_MUSIC)
            cursor?.let {
                while (it.moveToNext()) {

                    val title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE))//歌曲的名称
                    val album = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM))//歌曲的专辑名
                    val artist = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST))//歌曲的歌手名
                    val url = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA))//歌曲文件的全路径
                    val display_name = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME))//歌曲文件的名称
                    val year = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.YEAR))//歌曲文件的发行日期
                    val duration = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION))//歌曲的总播放时长
                    val size = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE))//歌曲文件的大小

                    if (checkIsMusic(duration, size)) {
                        var music = Music(title, album, artist, url, display_name, year, duration, size)
                        musicList.add(music)
                        Android.log("本地音乐  " + music.toString())
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            cursor?.let {
                it.close()
            }
        }
        Android.log("本地音乐数量 " + musicList.size)
        return musicList
    }

    /**
     * 根据播放时长和文件大小判断是否是音乐文件
     */
    fun checkIsMusic(duration: Int, size: Long): Boolean {
        if (duration <= 0 || size <= 0)
            return false

        var minute = duration / 1000 / 60
        var second = duration / 1000 % 60
        if (minute <= 0 && second <= 60)
            return false
        if (size <= 1024 * 1024)
            return false

        return true
    }


    /**
     * 格式化音乐文件名称,返回 歌手名 和 歌曲名
     */
    fun formatMusic(music: Music): Array<String>? {

        val split = music.name?.replace(" ", "")?.split(".")
        val info = split?.get(0)?.split("-")

        if (info?.size == 1)
            return arrayOf("未知", info[0])
        else if (info?.size == 2)
            return arrayOf(info[0], info[1])
        else if (info?.size == 3)
            return arrayOf(info[0] + "-" + info[1], info[2])  //如：A-Lin - 听见下雨的声音 (Live).mp3
        else
            return arrayOf("未知", "未知")
    }


}