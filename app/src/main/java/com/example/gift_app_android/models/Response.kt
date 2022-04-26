package com.example.gift_app_android.models

import java.io.Serializable

data class Response(
    var token: String,
    var message: String,
    var user: User,
    var status: Int,
) : Serializable
