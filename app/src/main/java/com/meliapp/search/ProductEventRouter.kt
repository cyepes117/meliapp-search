package com.meliapp.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.meliapp.search.domain.entities.Product
import com.meliapp.search.domain.usecase.GetProductDetailsUseCase
import com.meliapp.search.domain.usecase.GetProductListUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

internal interface ProductEventRouter : StatefulEventRouter<
        ProductEventRouter.ViewEvent,
        ProductEventRouter.ViewModelEvent,
        ProductEventRouter.State,
        > {

    sealed interface ViewEvent : EventRouter.ViewEvent {
        sealed class ProductList : ViewEvent {
            data class Search(val productName: String) : ProductList()
            data class SelectProduct(val product: Product) : ProductList()
            data object Clear: ProductList()
        }

        sealed class ProductDetail : ViewEvent {
            data object Buy : ProductDetail()
            data object ContactStore : ProductDetail()
        }
    }

    sealed interface ViewModelEvent : EventRouter.ViewModelEvent {
        sealed class ProductList : ViewModelEvent {
            data class ShowList(val products: List<Product>) : ProductList()
        }

        sealed class ProductDetail : ViewModelEvent {
            data class ShowProductDetail(val product: Product) : ProductDetail()
        }
    }

    data class State(
        val query: String,
        val results: List<Product>,
        val selectedResult: Product?,
    ) : StatefulEventRouter.State
}

internal class ProductViewModel(
    private val getProductListUseCase: GetProductListUseCase,
    private val getProductDetailsUseCase: GetProductDetailsUseCase
) : ViewModel(), ProductEventRouter {

    private val _viewModelState = MutableStateFlow(
        ProductEventRouter.State(
            query = "",
            results = emptyList(),
            selectedResult = null,
        )
    )
    override val viewModelState: StateFlow<ProductEventRouter.State>
        get() = _viewModelState
    private val _viewModelEvents = MutableSharedFlow<ProductEventRouter.ViewModelEvent>()
    override val viewModelEvents: Flow<ProductEventRouter.ViewModelEvent>
        get() = _viewModelEvents
    private val viewEvents = MutableSharedFlow<ProductEventRouter.ViewEvent>()

    init {
        reactToViewEvents()
    }

    override fun publishViewEvent(event: ProductEventRouter.ViewEvent) {
        viewModelScope.launch {
            viewEvents.emit(event)
        }
    }

    private fun reactToViewEvents() {
        viewModelScope.launch {
            viewEvents.collect { viewEvent ->
                when (viewEvent) {
                    ProductEventRouter.ViewEvent.ProductDetail.Buy -> TODO()
                    ProductEventRouter.ViewEvent.ProductDetail.ContactStore -> TODO()
                    is ProductEventRouter.ViewEvent.ProductList.Search ->
                        getProductListAndEmitEvents(viewEvent.productName)

                    is ProductEventRouter.ViewEvent.ProductList.SelectProduct ->
                        getProductDetailsAndEmitEvents(viewEvent.product.id)

                    ProductEventRouter.ViewEvent.ProductList.Clear -> TODO()
                }
            }
        }
    }

    private suspend fun getProductDetailsAndEmitEvents(productId: String) {
        val productDetails = getProductDetailsUseCase(productId)
        _viewModelState.emit(
            _viewModelState.value.copy(
                selectedResult = productDetails,
            )
        )
        _viewModelEvents.emit(
            ProductEventRouter.ViewModelEvent.ProductDetail.ShowProductDetail(
                product = productDetails,
            )
        )
    }

    private suspend fun getProductListAndEmitEvents(productName: String) {
        val productList = getProductListUseCase(productName)
        _viewModelState.emit(
            _viewModelState.value.copy(
                query = productName,
                results = productList,
            )
        )
        _viewModelEvents.emit(
            ProductEventRouter.ViewModelEvent.ProductList.ShowList(
                products = productList,
            )
        )
    }
}
