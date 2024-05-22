package com.example.fakestore.ui.presentation.screens.detail

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

class DetailViewModel(private val repository: StoreRepository) : ViewModel() {

    private val _detailState =
        MutableStateFlow<ResponseState<ProductsItemModel>>(ResponseState.Idle)

    val detailState = _detailState.asStateFlow()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ResponseState.Idle
        )

    fun getData(id: Int) {
        viewModelScope.launch {
            _detailState.emitAll(repository.getProductDetail(id))//don't forget private val model you use
        }
    }
}