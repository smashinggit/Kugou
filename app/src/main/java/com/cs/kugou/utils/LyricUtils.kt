package com.cs.kugou.utils

import android.os.Environment
import com.cs.framework.Android
import okhttp3.*
import okio.Okio
import org.greenrobot.eventbus.EventBus
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.nio.charset.Charset
import kotlin.experimental.xor

/**
 *
 * author : ChenSen
 * data : 2018/2/23
 * desc:
 */
object LyricUtils {

    val miarry = charArrayOf('@', 'G', 'a', 'w', '^', '2', 't', 'G', 'Q', '6', '1', '-', 'Î', 'Ò', 'n', 'i')


    fun loadLyric(fileName: String, keyword: String, duration: String, hash: String, callback: (result: Boolean, lyricPath: String) -> Unit) {

        var lyricFile = FileUtils.getLyricFiLe(fileName + ".krc")

        if (lyricFile != null) { //本地存在
            Android.log("歌词文件存在 $fileName")
            callback(true, lyricFile.absolutePath)

        } else {  //下载歌词
            var saveLyricFilePath = FileUtils.getFilePath(FileUtils.PATH_LYRIC, fileName + ".krc")
            val url = "http://mobilecdn.kugou.com/new/app/i/krc.php?keyword=$keyword&timelength=$duration&type=1&client=pc&cmd=200&hash=$hash"
            Android.log("歌词url + $url")
            Android.log("下载歌词文件")

            var client = OkHttpClient.Builder().build()
            var builder = Request.Builder()
            val request = builder.get().url(url).build()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call?, e: IOException?) {
                    e?.printStackTrace()
                    Android.log("下载歌词错误  $e")
                    callback(false, e.toString())
                }

                override fun onResponse(call: Call?, response: Response?) {
                    val byteString = Okio.buffer(Okio.source(response?.body()?.byteStream())).readByteString()
                    Okio.buffer(Okio.sink(File(saveLyricFilePath))).write(byteString).close()
                    Android.log("歌词文件路径   $saveLyricFilePath")
                    callback(true, saveLyricFilePath)
                }
            })
        }
    }

    /**
     *
     * 将krc文件转换成文本
     *
     * @param filenm krc文件路径加文件名
     * @return krc文件处理后的文本
     * @throws IOException
     */
    @Throws(IOException::class)
    fun krc2Text(filenm: String): String {
        val krcfile = File(filenm)
        val zip_byte = ByteArray(krcfile.length().toInt())
        val fileinstrm = FileInputStream(krcfile)
        val top = ByteArray(4)
        fileinstrm.read(top)
        fileinstrm.read(zip_byte)
        val j = zip_byte.size

        for (k in 0 until j) {
            val l = k % 16
            zip_byte[k] = zip_byte[k] xor miarry[l].toByte()
        }
        var result = String(ZLibUtils.decompress(zip_byte), Charset.defaultCharset())
//        Okio.buffer(Okio.sink(File(Environment.getExternalStorageDirectory().absolutePath + File.separator + "lyrictest.txt")))
//                .write(result.toByteArray()).close()
        return result
    }

}