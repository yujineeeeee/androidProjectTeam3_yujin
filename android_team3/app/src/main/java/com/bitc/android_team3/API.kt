package com.bitc.android_team3

import com.bitc.android_team3.Data.BasketData
import com.bitc.android_team3.Data.KeepData
import com.bitc.android_team3.Data.Total
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface API {

    //    유저 관련
    @POST("/userLogin")
    fun userLogin(@Body user: UserLoginData): Call<Int>

    @GET("/userInfo")
    fun userInfo(@Query("id") id: String): Call<UserInfoData>

    @GET("/userIdCheck")
    fun userIdCheck(@Query("id") id: String): Call<Int>

    @POST("/userInsert")
    fun userInsert(@Body user: UserInfoData): Call<Int>

    @POST("/userUpdate")
    fun userUpdate(@Body user: UserInfoData): Call<Int>

    @GET("/userDelete")
    fun userDelete(@Query("id") id: String): Call<Int>


    @POST("/totalAmount")
    fun totalAmount(@Body total: Total): Call<String>

    @GET("/BasketAmountList")
    fun BasketAmountList(@Query("pdId") pdId: String): Call<List<BasketData>>

    @GET("/BasketView")
    fun BasketView(
        @Query("pdId") pdId: String?,
        @Query("pdCreateDate") pdCreateDate: String
    ): Call<List<BasketData>>

    @POST("/keepInsert")
    fun KeepInsert(@Body keepData: KeepData): Call<Int>

    @GET("/keepView")
    fun keepView(@Query("kp_id") kp_id: String): Call<List<KeepData>>

    @POST("/keepCntUpdate")
    fun keepCntUpdate(@Query("kp_id") kp_id: String, @Query("kpCd") kpCd: String, @Query("kpCnt") kpCnt: Int): Call<List<KeepData>>

    @POST("/keepDelete")
    fun keepDelete(@Query("kp_id") kp_id: String, @Query("kpCd") kpCd: String): Call<List<KeepData>>

    @POST("/basketInsert")
    fun basketInsert(@Body keep: MutableList<KeepData>): Call<Int>

}

object RetrofitBuilder {
    var api: API = Retrofit.Builder()
        .baseUrl("http://10.0.2.2:8080")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(API::class.java)
}