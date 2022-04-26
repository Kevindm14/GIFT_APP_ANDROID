package com.example.gift_app_android.models

import com.google.gson.annotations.SerializedName

data class ResponseQr(
    @SerializedName("giftID") var giftID: String,
    @SerializedName("qrImage") var qrImage: String,
    @SerializedName("status") var status: Int
)
