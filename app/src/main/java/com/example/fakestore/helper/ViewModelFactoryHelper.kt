package com.example.fakestore.helper

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.Factory
import androidx.lifecycle.viewmodel.ViewModelInitializer

fun <VM : ViewModel> viewModelFactory(initializer: () -> VM): ViewModelProvider.Factory {//to make the viewmodel injectable in the composable screen
    return object : Factory{
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return initializer() as T
        }
    }
}