package com.cs.kugou.utils

import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.util.zip.Deflater
import java.util.zip.DeflaterOutputStream
import java.util.zip.Inflater
import java.util.zip.InflaterInputStream


/**
 *
 * author : ChenSen
 * data : 2018/2/23
 * desc:
 */
object ZLibUtils {
    fun compress(data: ByteArray): ByteArray {
        var output = ByteArray(0)
        val compresser = Deflater()
        compresser.reset()
        compresser.setInput(data)
        compresser.finish()
        val bos = ByteArrayOutputStream(data.size)
        try {
            val buf = ByteArray(1024)
            while (!compresser.finished()) {
                val i = compresser.deflate(buf)
                bos.write(buf, 0, i)
            }
            output = bos.toByteArray()
        } catch (e: Exception) {
            output = data
            e.printStackTrace()
        } finally {
            try {
                bos.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
        compresser.end()
        return output
    }

    fun compress(data: ByteArray, os: OutputStream) {
        val dos = DeflaterOutputStream(os)
        try {
            dos.write(data, 0, data.size)
            dos.finish()
            dos.flush()
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    fun decompress(data: ByteArray): ByteArray {
        var output = ByteArray(0)
        val decompresser = Inflater()
        decompresser.reset()
        decompresser.setInput(data)
        val o = ByteArrayOutputStream(data.size)
        try {
            val buf = ByteArray(1024)
            while (!decompresser.finished()) {
                val i = decompresser.inflate(buf)
                o.write(buf, 0, i)
            }
            output = o.toByteArray()
        } catch (e: Exception) {
            output = data
            e.printStackTrace()
        } finally {
            try {
                o.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
        decompresser.end()
        return output
    }

    fun decompress(`is`: InputStream): ByteArray {
        val iis = InflaterInputStream(`is`)
        val o = ByteArrayOutputStream(1024)
        try {
            var i = 1024
            val buf = ByteArray(i)
            while (iis.read(buf, 0, i) > 0) {
                o.write(buf, 0, i)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return o.toByteArray()
    }


}