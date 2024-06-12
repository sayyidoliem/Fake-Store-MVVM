package com.example.fakestore

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.fakestore.ui.presentation.navigation.Screens
import com.example.fakestore.ui.presentation.screens.detail.DetailScreens
import com.example.fakestore.ui.presentation.screens.home.HomeScreens
import com.example.fakestore.ui.theme.FakeStoreTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FakeStoreTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    NavHost(navController = navController, startDestination = Screens.Home.route) {
                        composable(Screens.Home.route) {
                            HomeScreens(
                                navigate = { index ->
                                    navController.navigate(
                                        Screens.Detail.createRoute(index!!)
                                    )
                                },
                            )
                        }
                        composable(Screens.Detail.route,
                            arguments = listOf(
                                navArgument("index") {
                                    type = NavType.IntType
                                    defaultValue = 0
                                }
                            )
                        ) {
                            val index = it.arguments!!.getInt("index", 0)
                            DetailScreens(
                                index = index,
                                navigateUp = { navController.navigateUp() }
                            )
                        }
                    }
                }
            }
        }
    }
}