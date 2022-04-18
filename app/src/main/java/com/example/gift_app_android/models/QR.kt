package com.example.gift_app_android.models

import com.google.gson.annotations.SerializedName

data class QR(
    @SerializedName("giftID") var id: String = "",
    @SerializedName("qrImage") var title: String = "",
    @SerializedName("status") var status: Int = 0,
)
