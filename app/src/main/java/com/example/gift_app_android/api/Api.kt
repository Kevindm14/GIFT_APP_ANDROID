package com.example.gift_app_android.api

import com.example.gift_app_android.models.CreateEventResponse
import com.example.gift_app_android.models.EventResponse
import com.example.gift_app_android.models.Response
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface Api {
    @Headers("Content-Type: application/json")
    @POST("/auth/login")
    fun login(
        @Body body: RequestBody
    ): Call<Response>

    @Headers("Content-Type: application/json")
    @GET("/events")
    fun listEvents(
        @Header("Authorization") auth: String
    ): Call<EventResponse>

    @Headers("Content-Type: application/json")
    @POST("/events/create")
    fun createEvent(
        @Body body: RequestBody,
        @Header("Authorization") auth: String
    ): Call<CreateEventResponse>
}