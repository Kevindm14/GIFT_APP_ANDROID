package com.example.gift_app_android.models

import java.io.Serializable

data class Event(
    var id: String = "",
    var title: String = "",
    var description: String = "",
    var date: String = "",
    var sent: String = "",
    var gift_id: String = "",
    var Gift: Gift
) : Serializable
