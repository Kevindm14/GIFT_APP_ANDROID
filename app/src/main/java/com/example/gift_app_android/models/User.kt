package com.example.gift_app_android.models

import com.google.gson.annotations.SerializedName
import java.util.*

data class User(
    var email: String,
    var password: String
    // @SerializedName("id") var id: String,
    // @SerializedName("first_name") var firstName: String,
    // @SerializedName("last_name") var lastName: String,
)
