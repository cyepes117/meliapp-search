package com.meliapp.search.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.lifecycleScope
import com.meliapp.search.ui.ProductListFragmentContent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.android.ext.android.get

class MainActivity : ComponentActivity() {
    private val viewModel: ProductEventRouter by lazy { get() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProductListFragmentContent(
                query = viewModel.viewModelState.collectAsState().value.query,
                products = viewModel.viewModelState.collectAsState().value.results,
                onProductSelected = { product ->
                    viewModel.publishViewEvent(
                        ProductEventRouter.ViewEvent.ProductList.SelectProduct(product = product)
                    )
                },
                onQueryChanged = { query ->
                    onQueryChangedWithDebounce(query)
                },
                onClearQuery = {
                    viewModel.publishViewEvent(
                        ProductEventRouter.ViewEvent.ProductList.Clear
                    )
                },
                isLoading = viewModel.viewModelState.collectAsState().value.isLoading,
                isApiError = viewModel.viewModelState.collectAsState().value.isApiError,
                isNotFoundError = viewModel.viewModelState.collectAsState().value.isNotFoundError,
                isFatalError = viewModel.viewModelState.collectAsState().value.isFatalError,
            )
        }
    }

    private val onQueryChangedWithDebounce: (String) -> Unit by lazy {
        debounce(
            coroutineScope = lifecycleScope
        ) { query ->
            viewModel.publishViewEvent(
                ProductEventRouter.ViewEvent.ProductList.Search(productName = query)
            )
        }
    }

    private fun <T> debounce(
        waitMs: Long = 1000L,
        coroutineScope: CoroutineScope,
        destinationFunction: (T) -> Unit
    ): (T) -> Unit {
        var debounceJob: Job? = null
        return { param: T ->
            debounceJob?.cancel()
            debounceJob = coroutineScope.launch {
                delay(waitMs)
                destinationFunction(param)
            }
        }
    }
}
