package com.example.gift_app_android.models

import java.io.Serializable

data class Gift(
    var id: String = "",
    var title: String = "",
    var qr: String = "",
    var code: String = "",
    var videoURL: String = "",
) : Serializable

data class ResponseGift(
    var message: String,
    var status: Int,
    var gift: Gift,
) : Serializable

data class GiftList(
    var gifts: List<Gift>,
) : Serializable
