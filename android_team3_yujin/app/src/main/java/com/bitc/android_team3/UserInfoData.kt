package com.bitc.android_team3

import com.google.gson.annotations.SerializedName

data class UserInfoData(@SerializedName("id") val id: String?, @SerializedName("pw") val pw: String?, @SerializedName("name") val name: String,
                        @SerializedName("email") val email: String?, @SerializedName("phone") val phone: String?, @SerializedName("createDate") val createDate: String?)