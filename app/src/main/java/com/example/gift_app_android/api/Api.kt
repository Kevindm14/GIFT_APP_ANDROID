package com.example.gift_app_android.api

import com.example.gift_app_android.models.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*


interface Api {
    @POST("/auth/login")
    suspend fun login(
        @Body body: RequestBody
    ): retrofit2.Response<Response>

    @POST("/auth/signup")
    suspend fun register(
        @Body body: RequestBody
    ): retrofit2.Response<Response>

    @GET("/events")
    suspend fun listEvents(
        @Header("Authorization") auth: String
    ): retrofit2.Response<EventResponse>

    @GET("/gifts/{user_id}")
    suspend fun listGifts(
        @Path("user_id") userID: String,
        @Header("Authorization") auth: String
    ): retrofit2.Response<GiftList>

    @GET("/gifts/qr/view")
    suspend fun generateQR(
        @Header("Authorization") auth: String
    ): retrofit2.Response<ResponseQr>

    @POST("/events/create")
    fun createEvent(
        @Body body: RequestBody,
        @Header("Authorization") auth: String
    ): Call<CreateEventResponse>

    @Multipart
    @POST("/gifts/create")
    suspend fun createGift(
        @Header("Authorization") authorization: String,
        @PartMap map: Map<String, @JvmSuppressWildcards RequestBody>,
        @Part videoFile: MultipartBody.Part
    ): retrofit2.Response<ResponseGift>
}