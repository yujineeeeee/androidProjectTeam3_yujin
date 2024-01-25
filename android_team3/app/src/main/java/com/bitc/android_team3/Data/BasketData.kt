package com.bitc.android_team3.Data

import com.google.gson.annotations.SerializedName

data class BasketData(@SerializedName("pdIdx") var pdIdx: Int = 0,
                    @SerializedName("pdCd") var pdCd: String?,
                    @SerializedName("pdName") var pdName: String?,
                    @SerializedName("pdJeongGa") var pdJeongGa: Int?,
                    @SerializedName("pdDiscount") var pdDiscount: Int?,
                    @SerializedName("pdPrice") var pdPrice: Int?,
                    @SerializedName("pdCnt") var pdCnt: Int?,
                    @SerializedName("pdCreateDate") var pdCreateDate: String?,
                    @SerializedName("pdId") var pdId: String?)