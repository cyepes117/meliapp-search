package com.meliapp.search.presentation.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.meliapp.search.presentation.ProductEventRouter
import com.meliapp.search.presentation.ProductEventRouter.ViewEvent
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
                    product = viewModel.viewModelState.collectAsStateWithLifecycle().value.selectedResult!!,
                    onBuyClicked = { viewModel.publishViewEvent(ViewEvent.ProductDetail.Buy) },
                    onContactClicked = { viewModel.publishViewEvent(ViewEvent.ProductDetail.ContactStore) },
                )
            }
        }
    }
}
