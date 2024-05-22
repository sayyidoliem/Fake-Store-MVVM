package com.example.fakestore.data.source.remote.model

data class ProductsItemModel(
    val category: String,
    val description: String,
    val id: Int,
    val image: String,
    val price: Double,
    val rating: Rating,
    val title: String
)