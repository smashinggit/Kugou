package com.cs.kugou.lyric.formats.krc

import com.cs.framework.Android
import com.cs.kugou.bean.Lyric
import com.cs.kugou.lyric.LyricReader
import com.cs.kugou.utils.LyricUtils
import com.cs.kugou.utils.ZLibUtils
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStream
import java.nio.charset.Charset
import java.util.*
import java.util.regex.Pattern
import kotlin.collections.HashMap
import kotlin.experimental.xor

/**
 *
 * author : ChenSen
 * data : 2018/2/27
 * desc:  krc歌词读取
 */
object KrcLyricReader : LyricReader() {

    //解码参数
    private val key = charArrayOf('@', 'G', 'a', 'w', '^', '2', 't', 'G', 'Q', '6', '1', '-', 'Î', 'Ò', 'n', 'i')

    val LEGAL_SONGNAME_PREFIX = "[ti:"        //歌曲名
    val LEGAL_SINGERNAME_PREFIX = "[ar:"      //歌手名
    val LEGAL_OFFSET_PREFIX = "[offset:"          //时间补偿值
    val LEGAL_BY_PREFIX = "[by:"              //歌词上传者
    val LEGAL_HASH_PREFIX = "[hash:"
    val LEGAL_AL_PREFIX = "[al:"
    val LEGAL_SIGN_PREFIX = "[sign:"
    val LEGAL_QQ_PREFIX = "[qq:"
    val LEGAL_TOTAL_PREFIX = "[total:"
    val LEGAL_LANGUAGE_PREFIX = "[language:"


    override fun readInputStream(inputStream: InputStream): Lyric {
        var noLyricLineCount = 0

        var lyric = Lyric()
        var lyricLines = TreeMap<Int, Lyric.LyricLine>()
        var lyricTag = HashMap<String, Any>()

        var lyricText = krc2Text(inputStream)         //解密歌词
        var lyricTextSplit = lyricText.split("\n")

        for (index in 0 until lyricTextSplit.size) {
            var lineInfo = lyricTextSplit[index]

            try {
                var lyricLine = parserLineInfos(lyricTag, lineInfo)  //解析每一行
                //过滤歌词文件头部相关信息。只保留歌词
                if (lyricLine.lineLyric.isEmpty()) {
                    noLyricLineCount++
                } else {
                    lyricLines.put(index - noLyricLineCount, lyricLine)
                }
                //  Android.log("第 ${index - noLyricLineCount} 行歌词  ${lyricLine.lineLyric}")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        inputStream.close()
        lyric.lyricTag = lyricTag
        lyric.lyricLineInfo = lyricLines

        // Android.log("读取歌词信息 $lyric")
        return lyric
    }

    //解析每一行
    private fun parserLineInfos(lyricTag: HashMap<String, Any>, lineInfo: String): Lyric.LyricLine {
        var lyricLine = Lyric.LyricLine()

        if (lineInfo.startsWith(LEGAL_SONGNAME_PREFIX)) {  //歌曲名
            var start = LEGAL_SONGNAME_PREFIX.length
            var end = lineInfo.lastIndexOf("]")
            lyricTag.put(Lyric.LyricTag.TAG_TITLE, lineInfo.substring(start, end))

        } else if (lineInfo.startsWith(LEGAL_SINGERNAME_PREFIX)) {  //歌手名
            var start = LEGAL_SINGERNAME_PREFIX.length
            var end = lineInfo.lastIndexOf("]")
            lyricTag.put(Lyric.LyricTag.TAG_ARTIST, lineInfo.substring(start, end))

        } else if (lineInfo.startsWith(LEGAL_OFFSET_PREFIX)) {      //时间补偿值
            var start = LEGAL_OFFSET_PREFIX.length
            var end = lineInfo.lastIndexOf("]")
            lyricTag.put(Lyric.LyricTag.TAG_OFFSET, lineInfo.substring(start, end))

        } else if (lineInfo.startsWith(LEGAL_BY_PREFIX)
                || lineInfo.startsWith(LEGAL_HASH_PREFIX)
                || lineInfo.startsWith(LEGAL_SIGN_PREFIX)
                || lineInfo.startsWith(LEGAL_QQ_PREFIX)
                || lineInfo.startsWith(LEGAL_TOTAL_PREFIX)
                || lineInfo.startsWith(LEGAL_AL_PREFIX)) {

            var start = lineInfo.indexOf("[") + 1
            var end = lineInfo.lastIndexOf("]")
            val temp = lineInfo.substring(start, end).split(":")
            lyricTag.put(temp[0], if (temp.size == 1) "" else temp[1])
        } else if (lineInfo.startsWith(LEGAL_LANGUAGE_PREFIX)) {  //翻译歌词

        } else {
            var pattern = Pattern.compile("\\[\\d+\\,\\d+\\]")
            var matcher = pattern.matcher(lineInfo)

            if (matcher.find()) {
                // [此行开始时刻距0时刻的毫秒数,此行持续的毫秒数]
                // <0,此字持续的毫秒数,0>歌<此字开始的时刻距此行开始时刻的毫秒数,此字持续的毫秒数,0>词
                // <此字开始的时刻距此行开始时刻的毫秒数,此字持续的毫秒数,0>正
                // <此字开始的时刻距此行开始时刻的毫秒数,此字持续的毫秒数,0>文
                // [29367,3137]
                // <0,252,0>递
                // <252,254,0>进
                // <506,354,0>的

                // 获取行的出现时间和结束时间
                var startIndex = matcher.start()
                var endIndex = matcher.end()
                val lineTime = lineInfo.substring(startIndex + 1, endIndex - 1).split(",")
                lyricLine.startTime = lineTime[0].toInt()
                lyricLine.endTime = lineTime[1].toInt()

                //获取歌词信息
                var lineContent = lineInfo.substring(endIndex, lineInfo.length)

                // 歌词匹配的正则表达式
                val regex = "\\<\\d+,\\d+,\\d+\\>"
                var lyricWordsPattern = Pattern.compile(regex)
                var lyricWordsMatcher = lyricWordsPattern.matcher(lineContent)

                // 歌词分隔
                var lineContentSplit = lineContent.split(regex.toRegex())
                var lyricsWords = extractLyricWords(lineContentSplit)
                lyricLine.lyricWords = lyricsWords

                // 获取每个歌词的时间
                var wordsInterval = arrayOfNulls<Int>(lyricsWords.size)
                var index = 0
                while (lyricWordsMatcher.find()) {
                    var wordsDisIntervalStr = lyricWordsMatcher.group()
                    val wordsDisIntervalStrTemp = wordsDisIntervalStr
                            .substring(1, wordsDisIntervalStr.length - 1)
                    val wordsDisIntervalTemp = wordsDisIntervalStrTemp.split(",")
                    wordsInterval[index++] = wordsDisIntervalTemp[1].toInt()
                }
                lyricLine.wordInterval = wordsInterval

                // 获取当行歌词
                val lineLyrics = lyricWordsMatcher.replaceAll("")
                lyricLine.lineLyric = lineLyrics
            }

        }
        return lyricLine
    }

    //提取歌词
    private fun extractLyricWords(lineContentSplit: List<String>): Array<String?> {
        if (lineContentSplit.size < 2)
            return emptyArray()

        var temp = arrayOfNulls<String>(lineContentSplit.size - 1)
        for (i in 1 until lineContentSplit.size) {
            temp[i - 1] = lineContentSplit[i]
        }
        return temp
    }


    @Throws(IOException::class)
    fun krc2Text(filenName: String): String {
        return krc2Text(FileInputStream(File(filenName)))
    }

    /**
     * 将krc文件转换成文本
     *
     * @param filenm krc文件路径加文件名
     * @return krc文件处理后的文本
     * @throws IOException
     */
    @Throws(IOException::class)
    fun krc2Text(inputStream: InputStream): String {
        val top = ByteArray(4)
        val zipByte = ByteArray(inputStream.available())
        inputStream.read(top)
        inputStream.read(zipByte)
        val j = zipByte.size

        for (k in 0 until j) {
            val l = k % 16
            zipByte[k] = zipByte[k] xor LyricUtils.miarry[l].toByte()
        }
        var result = String(ZLibUtils.decompress(zipByte), Charset.defaultCharset())
        //  Android.log("krc转文本\n $result")
        return result
    }


}