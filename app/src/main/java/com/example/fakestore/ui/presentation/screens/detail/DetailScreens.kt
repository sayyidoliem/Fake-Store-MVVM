package com.example.fakestore.ui.presentation.screens.detail

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.fakestore.FakeStoreApplication
import com.example.fakestore.helper.viewModelFactory

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun DetailScreens(
    index: Int,//index get from previous screen
    navigateUp: () -> Unit,
    viewModel: DetailViewModel = viewModel(
        factory = viewModelFactory {
            DetailViewModel(FakeStoreApplication.appModule.getRepository)
        }
    )
) {

    LaunchedEffect(key1 = true) {
        viewModel.getData(id = index)//use the index to func get data in viewmodel
    }

    val detailState = viewModel.detailState.collectAsStateWithLifecycle()

    val configuration = LocalConfiguration.current

    val navigator = rememberListDetailPaneScaffoldNavigator()

    detailState.value.DisplayResult(
        modifier = Modifier.fillMaxSize(),
        onLoading = {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator()
            }
        },
        onSuccess = { productModel ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                Box(modifier = Modifier.fillMaxWidth()) {
                    AsyncImage(
                        modifier = Modifier.align(Alignment.Center),
                        model = productModel.image,
                        contentDescription = ""
                    )
                    IconButton(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(16.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.background.copy(alpha = 0.2F)),
                        onClick = { navigateUp() }) {
                        Icon(
                            modifier = Modifier.size(24.dp),
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = ""
                        )
                    }
                }
                Text(
                    modifier = Modifier.padding(
                        horizontal = 16.dp,
                        vertical = 8.dp
                    ),
                    text = productModel.title,
                    fontSize = 32.sp,
                    lineHeight = 32.sp,
                    fontWeight = FontWeight.Bold
                )
                Row(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = productModel.category)
                    Text(
                        modifier = Modifier.padding(vertical = 8.dp),
                        text = "${productModel.rating.rate} | ${productModel.rating.count}",
                    )
                }
                Text(
                    modifier = Modifier.padding(
                        horizontal = 16.dp,
                        vertical = 8.dp
                    ),
                    text = productModel.description
                )
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { viewModel.addQuantity() }) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "add"
                        )
                    }
                    Text(text = viewModel.quantityValue.toString())
                    IconButton(onClick = { viewModel.removeQuantity() }) {
                        Icon(
                            imageVector = Icons.Default.Remove,
                            contentDescription = "remove"
                        )
                    }
                }
                viewModel.totalPrice(productModel.price)
                Text(
                    modifier = Modifier.padding(
                        horizontal = 16.dp,
                        vertical = 8.dp
                    ),
                    text = "Total ${viewModel.totalPriceValue}$"
                )
                Box(modifier = Modifier.padding(16.dp))
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 8.dp),
                    onClick = { /*TODO*/ }
                ) {
                    Text(text = "Buy")
                }
            }
        },
        onError = {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = "sorry, data not found")
            }
        }
    )
}