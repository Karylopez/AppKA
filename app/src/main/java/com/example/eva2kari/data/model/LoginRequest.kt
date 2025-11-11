package com.example.eva2kari.data.model

data class LoginRequest(
    val username: String,
    val password: String,
    val expiresInMins: Int = 30
)