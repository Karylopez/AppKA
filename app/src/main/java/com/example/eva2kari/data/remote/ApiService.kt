package com.example.eva2kari.data.remote

import com.example.eva2kari.data.model.LoginRequest
import com.example.eva2kari.data.model.LoginResponse
import com.example.eva2kari.data.model.User
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @POST("auth/login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<LoginResponse>

    @GET("auth/me")
    suspend fun getCurrentUser(@Header("Authorization") token: String): Response<User>

    @GET("users/{id}")
    suspend fun getUserById(@Path("id") userId: Int): Response<User>
}