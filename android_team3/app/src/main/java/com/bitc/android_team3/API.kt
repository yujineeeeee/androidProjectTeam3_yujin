package com.bitc.android_team3

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface API {
    @POST("/userLogin")
    fun userLogin(@Body user: UserLoginData): Call<Int>

    @GET("/userInfo")
    fun userInfo(@Query("id") id: String): Call<UserInfoData>

    @GET("/userIdCheck")
    fun userIdCheck(@Query("id") id:String): Call<Int>

    @POST("/userInsert")
    fun userInsert(@Body user: UserInfoData): Call<Int>
}

object RetrofitBuilder {
    var api: API = Retrofit.Builder()
        .baseUrl("http://10.0.2.2:8080")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(API::class.java)
}