package com.example.fakestore.ui.presentation.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.fakestore.FakeStoreApplication
import com.example.fakestore.helper.viewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreens(
    navigate: (index: Int?) -> Unit,
    viewModel: HomeViewModel = viewModel(
        factory = viewModelFactory {//for get viewmodel with can inject repository or other
            HomeViewModel(FakeStoreApplication.appModule.getRepository)
        }
    )
) {
    LaunchedEffect(key1 = true) {//for running coroutine at first
        viewModel.getData()
    }

    val dashBoardState = viewModel.homeState.collectAsStateWithLifecycle()//state in flow collected and live change

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Fake Store") },
            )
        }
    ) { innerPadding ->
        dashBoardState.value.DisplayResult(
            onLoading = {
                Column(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator()
                }
            },
            onSuccess = { productsModel ->
                LazyColumn(modifier = Modifier.padding(innerPadding)) {
                    items(productsModel.size) {
                        productsModel.forEach { productsItemModel ->
                            Card(
                                modifier = Modifier
                                    .padding(8.dp)
                                    .fillMaxSize(),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                                ),
                                onClick = {
                                    navigate(productsItemModel.id)//send the id
                                }
                            ) {
                                ListItem(
                                    overlineContent = {
                                        Text(
                                            text = productsItemModel.category,
                                        )
                                    },
                                    leadingContent = {
                                        AsyncImage(
                                            modifier = Modifier.size(56.dp),
                                            model = productsItemModel.image,
                                            contentDescription = ""
                                        )
                                    },
                                    headlineContent = {
                                        Text(
                                            text = productsItemModel.title,
                                            fontWeight = FontWeight.SemiBold,
                                            maxLines = 2
                                        )
                                    },
                                    supportingContent = {
                                        Text(
                                            text = productsItemModel.description,
                                            overflow = TextOverflow.Clip,
                                            maxLines = 1
                                        )
                                    },
                                    trailingContent = { Text(text = "${productsItemModel.price} $") }
                                )
                            }
                        }
                    }
                }
            },
            onError = {
                Column(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text = it)
                }
            }
        )
    }
}