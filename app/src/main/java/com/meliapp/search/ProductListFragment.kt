package com.meliapp.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels

class ProductListFragment : Fragment() {

    private val viewModel: ProductViewModel by viewModels()

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