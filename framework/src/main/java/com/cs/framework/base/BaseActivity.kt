package com.cs.framework.base

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.cs.framework.Android

/**
 *
 * author : ChenSen
 * data : 2018/1/18
 * desc:
 */
abstract class BaseActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
    }

    abstract fun getLayoutId(): Int

    fun Context.toast(message: Any) {
        Toast.makeText(this, message.toString(), Toast.LENGTH_SHORT).show()
    }

    fun Context.toastLong(message: Any) {
        Toast.makeText(this, message.toString(), Toast.LENGTH_LONG).show()
    }

    fun Context.log(message: Any) {
        Android.log(message)
    }

    fun Context.log(tag: String, message: Any) {
        Android.log(tag, message)
    }

}