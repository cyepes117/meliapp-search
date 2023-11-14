package com.meliapp.search.data.api

import arrow.core.left
import arrow.core.right
import com.meliapp.search.data.api.dto.MultiGetResponse
import com.meliapp.search.data.api.dto.ProductDto
import com.meliapp.search.data.api.dto.SearchResponse
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class RemoteProductDataSourceTest {

    private val testDispatcher = StandardTestDispatcher()
    private val testCoroutineScope = TestScope(testDispatcher)
    private lateinit var remoteProductDataSource: RemoteProductDataSource
    private lateinit var apiService: ProductApiService

    @BeforeEach
    fun setUp() {
        apiService = mockk()
        remoteProductDataSource = RemoteProductDataSourceImpl(apiService)
    }

    @Test
    fun `get product list success`() = testCoroutineScope.runTest {
        // GIVEN
        val productName = "testProduct"
        val expectedProductList = listOf(
            ProductDto(id = "1", title = "Product 1", thumbnail = "", price = 20.0),
            ProductDto(id = "2", title = "Product 2", thumbnail = "", price = 30.0)
        )
        val searchResponse = SearchResponse(
            siteId = "123",
            countryDefaultTimeZone = "UTC",
            query = "product",
            results = expectedProductList
        )
        coEvery { apiService.getProductList(productName) } returns searchResponse.right()

        // WHEN
        val result = remoteProductDataSource.getProductList(productName)

        // THEN
        assertEquals(expectedProductList.right(), result)
    }

    @Test
    fun `get product list failure`() = testCoroutineScope.runTest {
        // GIVEN
        val productName = "testProduct"
        val expectedError = ApiError.NetworkError(Throwable())
        coEvery { apiService.getProductList(productName) } returns expectedError.left()

        // WHEN
        val result = remoteProductDataSource.getProductList(productName)

        // THEN
        assertEquals(expectedError.left(), result)
    }

    @Test
    fun `get product details success`() = testCoroutineScope.runTest {
        // GIVEN
        val productId = "123"
        val expectedProductDetails =
            ProductDto(id = productId, title = "Test Product", price = 99.99, thumbnail = "")
        val multiGetResponse = MultiGetResponse(code = 123, result = expectedProductDetails)
        coEvery { apiService.getProductDetails(productId) } returns multiGetResponse.right()

        // WHEN
        val result = remoteProductDataSource.getProductDetails(productId)

        // THEN
        assertEquals(expectedProductDetails.right(), result)
    }

    @Test
    fun `get product details failure`() = testCoroutineScope.runTest {
        // GIVEN
        val productId = "456"
        val expectedError = ApiError.NotFoundError
        coEvery { apiService.getProductDetails(productId) } returns expectedError.left()

        // WHEN
        val result = remoteProductDataSource.getProductDetails(productId)

        // THEN
        assertEquals(expectedError.left(), result)
    }
}
