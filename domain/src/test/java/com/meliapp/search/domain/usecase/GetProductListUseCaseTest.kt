package com.meliapp.search.domain.usecase

import arrow.core.left
import arrow.core.right
import com.meliapp.search.domain.entities.Product
import com.meliapp.search.domain.repository.ProductRepository
import com.meliapp.search.domain.repository.RepositoryError
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class GetProductListUseCaseTest {

    private val testDispatcher = StandardTestDispatcher()
    private val testCoroutineScope = TestScope(testDispatcher)
    private lateinit var getProductListUseCase: GetProductListUseCase
    private lateinit var repository: ProductRepository

    @BeforeEach
    fun setUp() {
        repository = mockk()
        getProductListUseCase = GetProductListUseCaseImpl(repository)
    }

    @Test
    fun `get product list success`() = testCoroutineScope.runTest {
        // GIVEN
        val productName = "123"
        val expectedProduct =
            listOf(Product(id = productName, title = "Test Product", price = 99.99, thumbnail = ""))
        coEvery { repository.getProductList(productName) } returns expectedProduct.right()

        // WHEN
        val result = getProductListUseCase(productName)

        // THEN
        assertEquals(expectedProduct.right(), result)
    }

    @Test
    fun `get product list failure`() = testCoroutineScope.runTest {
        // GIVEN
        val productName = "456"
        val expectedError = RepositoryError()
        coEvery { repository.getProductList(productName) } returns expectedError.left()

        // WHEN
        val result = getProductListUseCase(productName)

        // THEN
        assertEquals(expectedError.left(), result)
    }
}
