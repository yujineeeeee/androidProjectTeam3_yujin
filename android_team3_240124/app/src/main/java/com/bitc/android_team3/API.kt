package com.bitc.android_team3

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface API {
    @POST("/totalAmount")
    fun totalAmount(@Body total: Total): Call<String>

    @POST("/userLogin")
    fun userLogin(@Body total: Total): Call<String>

    @GET("/BasketAmountList")
    fun BasketAmountList(@Query("pdId") pdId: String): Call<List<BasketData>>

    @GET("/BasketView")
    fun BasketView(@Query("pdId") pdId: String,@Query("pdCreateDate") pdCreateDate: String): Call<List<BasketData>>

}

object RetrofitBuilder {
    var api: API = Retrofit.Builder()
        .baseUrl("http://10.0.2.2:8080")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(API::class.java)
}