package com.example.fakestore.data.source.remote

import com.example.fakestore.data.source.remote.model.ProductsItemModel
import com.example.fakestore.data.source.remote.model.ProductsModel
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiInterface {

    @GET("products")
    suspend fun getAllProducts() : ProductsModel

    @GET("products/{id}")
    suspend fun getProductDetail(
        @Path("id") id : Int//use @Path for return or replace the api index
    ): ProductsItemModel
}