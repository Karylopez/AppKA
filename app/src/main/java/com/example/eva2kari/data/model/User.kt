package com.example.eva2kari.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "users")
data class User(
    @PrimaryKey
    @SerializedName("id")
    val id: Int,

    @SerializedName("username")
    val username: String,

    @SerializedName("email")
    val email: String,

    @SerializedName("firstName")
    val firstName: String,

    @SerializedName("lastName")
    val lastName: String,

    @SerializedName("gender")
    val gender: String,

    @SerializedName("image")
    val image: String, // URL de imagen de la API

    // Campo adicional para imagen local capturada/seleccionada
    val localImagePath: String? = null,

    @SerializedName("token")
    val token: String? = null
)