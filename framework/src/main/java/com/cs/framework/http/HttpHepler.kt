package com.cs.framework.http

import android.content.Context
import com.cs.framework.Android
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by chensen on 2018/1/17.
 */
object HttpHepler {
    var BASE_URL = ""
    private val httpClient = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(if (Android.DEV) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE))
            .build()

    private val builder = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(httpClient)
            .baseUrl(BASE_URL)


    fun <S> createService(service: Class<S>): S {
        return builder.build().create(service)
    }


    fun setCookie(context: Context) {
    }

    fun clearCookie() {
    }
}