package com.meliapp.search.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import com.meliapp.search.domain.entities.Product
import com.meliapp.search.ui.ProductListItem
import org.koin.android.ext.android.get

class ProductDetailFragment : Fragment() {

    private val viewModel: ProductEventRouter by lazy { get() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return ComposeView(requireContext()).apply {
            setContent {
                ProductListItem(
                    product = Product(id = "1", title = "Product 1", thumbnail = "", price = 20.0),
                    onProductSelected = {},
                )
            }
        }
    }
}
