package com.example.fakestore.data.repository

import android.util.Log
import com.example.fakestore.data.source.remote.ApiInterface
import com.example.fakestore.data.source.remote.model.ProductsItemModel
import com.example.fakestore.data.source.remote.model.ProductsModel
import com.rmaprojects.apirequeststate.ResponseState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class StoreRepositoryImplementation(private val apiInterface: ApiInterface) : StoreRepository {
    override fun getAllProducts(): Flow<ResponseState<ProductsModel>> = flow {
        emit(ResponseState.Loading)
        try {
            val result = apiInterface.getAllProducts()
            emit(ResponseState.Success(result))
        } catch (e: Exception) {
            Log.d("TAG", "getAllProducts: $e")
            emit(ResponseState.Error(e.message.toString()))
        }
    }


    override fun getProductDetail(id : Int): Flow<ResponseState<ProductsItemModel>> = flow {
        emit(ResponseState.Loading)
        try {
            val result = apiInterface.getProductDetail(id = id)
            emit(ResponseState.Success(result))
        } catch (e: Exception) {
            emit(ResponseState.Error(e.message.toString()))
        }
    }
}