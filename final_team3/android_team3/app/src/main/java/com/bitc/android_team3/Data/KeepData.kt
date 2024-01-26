package com.bitc.android_team3.Data

import com.google.gson.annotations.SerializedName

data class KeepData(
    @SerializedName("kpIdx") val kpIdx: Int = 0,
    @SerializedName("kpCd") val kpCd: String?,
    @SerializedName("kpName") val kpName: String?,
    @SerializedName("kpJeongGa") val kpJeongGa: Int?,
    @SerializedName("kpDiscount") val kpDiscount: Int?,
    @SerializedName("kpPrice") val kpPrice: Int?,
    @SerializedName("kpCnt") var kpCnt: Int?,
    @SerializedName("kpCreateDate") val kpCreateDate: String?,
    @SerializedName("kpId") val kpId: String?,
    @SerializedName("kpImage") val kpImage: String?

)