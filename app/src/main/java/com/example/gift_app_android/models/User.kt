package com.example.gift_app_android.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class User(
    @SerializedName("id") var id: String = "",
    @SerializedName("first_name") var firstName: String = "",
    @SerializedName("last_name") var lastName: String = "",
    @SerializedName("work_phone") var workPhone: String = "",
    @SerializedName("phone_extension") var phoneExtension: String = "",
    @SerializedName("email") var email: String = "",
    @SerializedName("password") var password: String = "",
    @SerializedName("created_at") var createdAt: String = "",
    @SerializedName("updated_at") var updatedAt: String = ""
    // @SerializedName("id") var id: String,
    // @SerializedName("first_name") var firstName: String,
    // @SerializedName("last_name") var lastName: String,
) : Serializable
