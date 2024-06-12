package com.example.fakestore.ui.presentation.screens.home

import android.content.res.Configuration
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.layout.ThreePaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.fakestore.FakeStoreApplication
import com.example.fakestore.helper.viewModelFactory

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun HomeScreens(
    navigate: (index: Int?) -> Unit,
    viewModel: HomeViewModel = viewModel(
        factory = viewModelFactory {//for get viewmodel with can inject repository or other
            HomeViewModel(FakeStoreApplication.appModule.getRepository)
        }
    )
) {

    var index by remember { mutableIntStateOf(0) }

    LaunchedEffect(key1 = true) {//for running coroutine at first
        viewModel.getData()
    }

    val dashBoardState =
        viewModel.homeState.collectAsStateWithLifecycle()//state in flow collected and live change

    val detailState = viewModel.detailState.collectAsStateWithLifecycle()

    val configuration = LocalConfiguration.current

    val navigator = rememberListDetailPaneScaffoldNavigator()

    BackHandler(navigator.canNavigateBack()) {
        navigator.navigateBack()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Fake Store") },
                navigationIcon = {
                    if (navigator.currentDestination?.pane == ThreePaneScaffoldRole.Primary && configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                        IconButton(
                            onClick = { navigator.navigateBack() }) {
                            Icon(
                                modifier = Modifier.size(24.dp),
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = ""
                            )
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        ListDetailPaneScaffold(modifier = Modifier.fillMaxSize(),
            directive = navigator.scaffoldDirective,
            value = navigator.scaffoldValue,
            listPane = {
                AnimatedPane(modifier = Modifier.fillMaxSize()) {
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
//                                                navigate(productsItemModel.id)//send the id
                                                index = productsItemModel.id
                                                navigator.navigateTo(ListDetailPaneScaffoldRole.Detail)
                                                viewModel.getDataDetail(id = index)//use the index to func get data in viewmodel|
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
            },
            detailPane = {
                AnimatedPane {
                    detailState.value.DisplayResult(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
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
                                    Card(
                                        modifier = Modifier
                                            .align(Alignment.Center)
                                            .aspectRatio(1 / 1f)
                                            .padding(16.dp),
                                        elevation = CardDefaults.cardElevation(defaultElevation = 16.dp)
                                    ) {
                                        AsyncImage(
                                            modifier = Modifier.fillMaxSize(),
                                            model = productModel.image,
                                            contentDescription = "",
                                            contentScale = ContentScale.Crop
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
            }
        )
    }
}

//    when(navigator.currentDestination?.pane) { use this for find current pane in threePaneScaffold
//        ThreePaneScaffoldRole.Primary -> {}
//        ThreePaneScaffoldRole.Secondary -> {}
//        ThreePaneScaffoldRole.Tertiary -> {}
//        null -> {}
//    }
