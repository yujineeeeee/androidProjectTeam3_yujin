package com.bitc.android_team3.Data

import com.google.gson.annotations.SerializedName

data class Total(
    @SerializedName("pdId") val pdId: String?,
    @SerializedName("pdCreateDate") val pdCreateDate: String
)