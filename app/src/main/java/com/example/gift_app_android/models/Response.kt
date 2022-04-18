package com.example.gift_app_android.models

import com.google.gson.annotations.SerializedName

data class Response(
    var token: String,
    var message: String,
    var user: User,
    var status: Int,
)
