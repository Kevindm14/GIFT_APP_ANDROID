package com.example.gift_app_android.models

import com.google.gson.annotations.SerializedName

data class Response(
    @SerializedName("token") var token: String,
    @SerializedName("user") var user: User,
)
