package com.meliapp.search.presentation.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import com.meliapp.search.presentation.ProductEventRouter
import org.koin.android.ext.android.get

class ProductDetailFragment : Fragment() {

    private val viewModel: ProductEventRouter by lazy { get() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return ComposeView(requireContext()).apply {
            setContent {
                ProductDetail(
                    product = viewModel.viewModelState.collectAsState().value.selectedResult!!,
                    onProductSelected = {},
                )
            }
        }
    }
}
