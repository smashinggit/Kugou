package com.cs.kugou.utils

import java.io.File
import java.io.FileInputStream
import java.math.BigInteger
import java.nio.channels.FileChannel
import java.security.MessageDigest

/**
 *
 * author : ChenSen
 * data : 2018/2/6
 * desc:
 */
object MD5Utils {


    /**
     * 获取文件的md5值
     */
    fun getFileMD5(file: File):String {
        var value = ""
        var inputStream: FileInputStream? = null
        try {
            inputStream = FileInputStream(file)
            val byteBuffer = inputStream.channel.map(FileChannel.MapMode.READ_ONLY, 0, file.length())
            val md5 = MessageDigest.getInstance("MD5")
            md5.update(byteBuffer)
            val bi = BigInteger(1, md5.digest())
            value = bi.toString(16)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (null != inputStream) {
                try {
                    inputStream.close()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        return value

    }

}