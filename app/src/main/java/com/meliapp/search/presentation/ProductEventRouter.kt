package com.meliapp.search.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.meliapp.search.base.EventRouter
import com.meliapp.search.base.StatefulEventRouter
import com.meliapp.search.data.api.ApiError
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
            data object Clear : ProductList()
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
        val query: String = "",
        val results: List<Product> = emptyList(),
        val selectedResult: Product? = null,
        val isLoading: Boolean = false,
        val isApiError: Boolean = false,
        val isNotFoundError: Boolean = false,
        val isFatalError: Boolean = false,
    ) : StatefulEventRouter.State
}

internal class ProductViewModel(
    private val getProductListUseCase: GetProductListUseCase,
    private val getProductDetailsUseCase: GetProductDetailsUseCase
) : ViewModel(), ProductEventRouter {


    private val _viewModelState = MutableStateFlow(DEFAULT_STATE)
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
                    is ProductEventRouter.ViewEvent.ProductDetail.Buy -> TODO()
                    is ProductEventRouter.ViewEvent.ProductDetail.ContactStore -> TODO()
                    is ProductEventRouter.ViewEvent.ProductList.Search ->
                        getProductListAndEmitEvents(viewEvent.productName)

                    is ProductEventRouter.ViewEvent.ProductList.SelectProduct ->
                        getProductDetailsAndEmitEvents(viewEvent.product.id)

                    is ProductEventRouter.ViewEvent.ProductList.Clear ->
                        _viewModelState.emit(DEFAULT_STATE)
                }
            }
        }
    }

    private suspend fun getProductDetailsAndEmitEvents(productId: String) {
        _viewModelState.emit(
            _viewModelState.value.copy(
                isLoading = true,
            )
        )

        getProductDetailsUseCase(productId).fold(
            ifLeft = { error ->
                when (error) {
                    is ApiError.NotFoundError -> {
                        _viewModelState.emit(
                            _viewModelState.value.copy(
                                isNotFoundError = true,
                                isLoading = false,
                            )
                        )
                    }

                    is ApiError.HttpError,
                    is ApiError.NetworkError -> {
                        _viewModelState.emit(
                            _viewModelState.value.copy(
                                isApiError = true,
                                isLoading = false,
                            )
                        )
                    }

                    else -> {
                        _viewModelState.emit(
                            _viewModelState.value.copy(
                                isFatalError = true,
                                isLoading = false,
                            )
                        )
                    }
                }

            },
            ifRight = { product ->
                _viewModelState.emit(
                    _viewModelState.value.copy(
                        selectedResult = product,
                        isLoading = false,
                    )
                )

                _viewModelEvents.emit(
                    ProductEventRouter.ViewModelEvent.ProductDetail.ShowProductDetail(
                        product = product,
                    )
                )
            },
        )
    }

    private suspend fun getProductListAndEmitEvents(productName: String) {
        if (productName.isEmpty()) {
            return
        }

        _viewModelState.emit(
            _viewModelState.value.copy(
                isLoading = true,
            )
        )

        getProductListUseCase(productName).fold(
            ifLeft = { error ->
                when (error) {
                    is ApiError.NotFoundError -> {
                        _viewModelState.emit(
                            _viewModelState.value.copy(
                                isNotFoundError = true,
                                isLoading = false,
                            )
                        )
                    }

                    is ApiError.HttpError,
                    is ApiError.NetworkError -> {
                        _viewModelState.emit(
                            _viewModelState.value.copy(
                                isApiError = true,
                                isLoading = false,
                            )
                        )
                    }

                    else -> {
                        _viewModelState.emit(
                            _viewModelState.value.copy(
                                isFatalError = true,
                                isLoading = false,
                            )
                        )
                    }
                }
            },
            ifRight = { productList ->
                _viewModelState.emit(
                    _viewModelState.value.copy(
                        query = productName,
                        results = productList,
                        isLoading = false,
                    )
                )

                _viewModelEvents.emit(
                    ProductEventRouter.ViewModelEvent.ProductList.ShowList(
                        products = productList,
                    )
                )
            },
        )
    }

    private companion object {
        val DEFAULT_STATE = ProductEventRouter.State(
            query = "",
            results = emptyList(),
            selectedResult = null,
            isLoading = false,
        )
    }
}
