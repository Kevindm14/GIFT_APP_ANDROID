package com.example.gift_app_android.api

import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface Api {
    @Headers("Content-Type: application/json")
    @POST("/auth/login")
    suspend fun login(@Body requestBody: RequestBody): Response<ResponseBody>
}