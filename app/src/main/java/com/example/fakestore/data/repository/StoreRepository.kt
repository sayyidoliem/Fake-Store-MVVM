package com.example.fakestore.data.repository

import com.example.fakestore.data.source.remote.model.ProductsItemModel
import com.example.fakestore.data.source.remote.model.ProductsModel
import com.rmaprojects.apirequeststate.ResponseState
import kotlinx.coroutines.flow.Flow

interface StoreRepository {
    fun getAllProducts(): Flow<ResponseState<ProductsModel>>
    fun getProductDetail(id : Int): Flow<ResponseState<ProductsItemModel>>
}