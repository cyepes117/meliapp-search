package com.meliapp.search

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.meliapp.search.ui.theme.MeliAppTheme
import org.koin.android.ext.android.get

class MainActivity : ComponentActivity() {
    private val viewModel: ProductEventRouter by lazy { get() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MeliAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    //ProductListFragment()

                    ProductListFragmentContent(
                        query = viewModel.viewModelState.collectAsState().value.query,
                        products = viewModel.viewModelState.collectAsState().value.results,
                        onProductSelected = { product ->
                            viewModel.publishViewEvent(
                                ProductEventRouter.ViewEvent.ProductList.SelectProduct(product = product)
                            )
                        },
                        onQueryChanged = { query ->
                            viewModel.publishViewEvent(
                                ProductEventRouter.ViewEvent.ProductList.Search(productName = query)
                            )
                        },
                        onClearQuery = {
                            viewModel.publishViewEvent(
                                ProductEventRouter.ViewEvent.ProductList.Clear
                            )
                        },
                    )
                }
            }
        }
    }
}

