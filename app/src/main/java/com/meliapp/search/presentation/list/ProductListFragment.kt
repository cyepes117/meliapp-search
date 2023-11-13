package com.meliapp.search.presentation.list

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.ComposeView
import androidx.lifecycle.lifecycleScope
import com.meliapp.search.presentation.ProductEventRouter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.android.ext.android.get

class ProductListFragment : Fragment() {

    private val viewModel: ProductEventRouter by lazy { get() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
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
