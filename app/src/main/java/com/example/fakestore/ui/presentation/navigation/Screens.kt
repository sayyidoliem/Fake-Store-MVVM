package com.example.fakestore.ui.presentation.navigation

sealed class Screens(val route: String) {
    data object Home : Screens("home")
    data object Detail : Screens("detail?&index={index}") {
        fun createRoute(index: Int): String {
            return "detail?&index=${index}"
        }
    }
}