package com.bitc.android_team3

import com.google.gson.annotations.SerializedName

data class UserLoginData (@SerializedName("id") val id: String, @SerializedName("pw") val pw: String)