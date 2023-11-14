package com.meliapp.search.presentation

import arrow.core.left
import arrow.core.right
import com.meliapp.search.data.api.ApiError
import com.meliapp.search.domain.entities.Product
import com.meliapp.search.domain.usecase.GetProductDetailsUseCase
import com.meliapp.search.domain.usecase.GetProductListUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
internal class ProductViewModelTest {

    private lateinit var viewModel: ProductViewModel
    private lateinit var getProductListUseCase: GetProductListUseCase
    private lateinit var getProductDetailsUseCase: GetProductDetailsUseCase
    private val testDispatcher = StandardTestDispatcher()
    private val testCoroutineScope = TestScope(testDispatcher)

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        getProductListUseCase = mockk()
        getProductDetailsUseCase = mockk()

        viewModel = ProductViewModel(
            getProductListUseCase = getProductListUseCase,
            getProductDetailsUseCase = getProductDetailsUseCase
        )
    }

    @Nested
    inner class GetProductDetailsAndEmitEvents {

        @Test
        fun success() = testCoroutineScope.runTest {
            // GIVEN
            val productId = "1"
            val product = Product(id = "1", title = "Product 1", thumbnail = "", price = 20.0)
            coEvery { getProductDetailsUseCase(productId) } returns product.right()

            // WHEN
            viewModel.publishViewEvent(
                ProductEventRouter.ViewEvent.ProductList.SelectProduct(product)
            )

            // THEN
            val state = viewModel.viewModelState.drop(1).first()
            assert(state.selectedResult == product)
            assert(state.isLoading.not())
        }

        @Nested
        inner class Errors {

            @Test
            fun `not found error`() = testCoroutineScope.runTest {
                testErrorHandling(
                    error = ApiError.NotFoundError,
                    productId = "productName"
                ) { isNotFoundError }
            }

            @Test
            fun `http error`() = testCoroutineScope.runTest {
                testErrorHandling(
                    error = ApiError.HttpError(code = 404, body = ""),
                    productId = "productName"
                ) { isApiError }
            }

            @Test
            fun `network error`() = testCoroutineScope.runTest {
                testErrorHandling(
                    error = ApiError.NetworkError(Throwable()),
                    productId = "productName"
                ) { isApiError }
            }

            @Test
            fun `unknown error`() = testCoroutineScope.runTest {
                testErrorHandling(
                    error = ApiError.UnknownApiError(Throwable()),
                    productId = "productName"
                ) { isFatalError }
            }

            private suspend fun testErrorHandling(
                error: ApiError,
                productId: String,
                expectedErrorState: ProductEventRouter.State.() -> Boolean
            ) {
                val product = Product(id = productId, title = "Product 1", thumbnail = "", price = 20.0)
                coEvery { getProductDetailsUseCase(productId) } returns error.left()

                // WHEN
                viewModel.publishViewEvent(
                    ProductEventRouter.ViewEvent.ProductList.SelectProduct(product)
                )

                // THEN
                val state = viewModel.viewModelState.drop(1).first()
                assert(state.expectedErrorState())
                assert(state.isLoading.not())
            }
        }
    }

    @Nested
    inner class GetProductListAndEmitEvents {

        @Test
        fun success() = testCoroutineScope.runTest {
            // GIVEN
            val productName = "productName"
            val productList =
                listOf(Product(id = "1", title = "Product 1", thumbnail = "", price = 20.0))
            coEvery { getProductListUseCase(productName) } returns productList.right()

            // WHEN
            viewModel.publishViewEvent(ProductEventRouter.ViewEvent.ProductList.Search(productName))

            // THEN
            val state = viewModel.viewModelState.drop(1).first()
            assert(state.query == productName)
            assert(state.results == productList)
            assert(state.isLoading.not())
        }

        @Nested
        inner class Errors {

            @Test
            fun `not found error`() = testCoroutineScope.runTest {
                testErrorHandling(
                    error = ApiError.NotFoundError,
                    productName = "productName"
                ) { isNotFoundError }
            }

            @Test
            fun `http error`() = testCoroutineScope.runTest {
                testErrorHandling(
                    error = ApiError.HttpError(code = 404, body = ""),
                    productName = "productName"
                ) { isApiError }
            }

            @Test
            fun `network error`() = testCoroutineScope.runTest {
                testErrorHandling(
                    error = ApiError.NetworkError(Throwable()),
                    productName = "productName"
                ) { isApiError }
            }

            @Test
            fun `unknown error`() = testCoroutineScope.runTest {
                testErrorHandling(
                    error = ApiError.UnknownApiError(Throwable()),
                    productName = "productName"
                ) { isFatalError }
            }

            private suspend fun testErrorHandling(
                error: ApiError,
                productName: String,
                expectedErrorState: ProductEventRouter.State.() -> Boolean
            ) {
                coEvery { getProductListUseCase(productName) } returns error.left()

                // WHEN
                viewModel.publishViewEvent(
                    ProductEventRouter.ViewEvent.ProductList.Search(
                        productName
                    )
                )

                // THEN
                val state = viewModel.viewModelState.drop(1).first()
                assert(state.expectedErrorState())
                assert(state.isLoading.not())
            }
        }
    }
}
