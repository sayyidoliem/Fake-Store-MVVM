package com.example.fakestore.ui.presentation.screens.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fakestore.data.repository.StoreRepository
import com.example.fakestore.data.source.remote.model.ProductsItemModel
import com.example.fakestore.data.source.remote.model.ProductsModel
import com.rmaprojects.apirequeststate.ResponseState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: StoreRepository) : ViewModel() {

    private val _homeState = MutableStateFlow<ResponseState<ProductsModel>>(ResponseState.Idle)

    val homeState = _homeState.asStateFlow()//convert data flow into stateflow
        .stateIn(
            scope = viewModelScope,//like coroutine but in viewmodel environment
            started = SharingStarted.WhileSubscribed(5000),//timeout if no one takes data
            initialValue = ResponseState.Idle//return to initial state
        )

    fun getData(){
        viewModelScope.launch {
            _homeState.emitAll(repository.getAllProducts())
        }
    }

    private val _detailState = MutableStateFlow<ResponseState<ProductsItemModel>>(ResponseState.Idle)

    val detailState = _detailState.asStateFlow()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ResponseState.Idle
        )

    fun getDataDetail(id: Int) {
        viewModelScope.launch {
            _detailState.emitAll(repository.getProductDetail(id))//don't forget private val model you use
        }
    }

    var quantityValue by mutableIntStateOf(1)

    var totalPriceValue by mutableDoubleStateOf(0.0)

    fun totalPrice(value: Double) {
        totalPriceValue = value * quantityValue
    }
    fun addQuantity() {
        if (quantityValue < Int.MAX_VALUE) {
            quantityValue++
        }
    }

    fun removeQuantity() {
        if (quantityValue > 1) {
            quantityValue--
        }
    }
}