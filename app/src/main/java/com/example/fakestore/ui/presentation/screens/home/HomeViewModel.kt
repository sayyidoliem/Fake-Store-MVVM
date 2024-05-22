package com.example.fakestore.ui.presentation.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fakestore.data.repository.StoreRepository
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
}