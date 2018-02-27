package com.cs.framework.api

import com.google.gson.JsonObject
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

/**
 *
 * author : ChenSen
 * data : 2018/2/23
 * desc:
 */
interface Api {

    @GET("/new/app/i/krc.php")
    fun searchLyric(@Query("keyword") keyword: String, @Query("timelength") duration: String,
                    @Query("type") type: Int = 1, @Query("client") client: String = "pc",
                    @Query("cmd") cmd: Int = 200, @Query("hash") hash: String): Observable<String>
}