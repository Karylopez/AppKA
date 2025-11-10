package com.example.appka.data.model

data class Product(
    val id: Int,
    val title: String,
    val description: String,
    val price: Double,
    val thumbnail: String,
    val category: String,
    val rating: Double,
    val stock: Int
)