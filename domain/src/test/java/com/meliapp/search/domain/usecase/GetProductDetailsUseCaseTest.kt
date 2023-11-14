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

internal class GetProductDetailsUseCaseTest {

    private val testDispatcher = StandardTestDispatcher()
    private val testCoroutineScope = TestScope(testDispatcher)
    private lateinit var getProductDetailsUseCase: GetProductDetailsUseCase
    private lateinit var repository: ProductRepository

    @BeforeEach
    fun setUp() {
        repository = mockk()
        getProductDetailsUseCase = GetProductDetailsUseCaseImpl(repository)
    }

    @Test
    fun `get product details success`() = testCoroutineScope.runTest {
        // GIVEN
        val productId = "123"
        val expectedProduct =
            Product(id = productId, title = "Test Product", price = 99.99, thumbnail = "")
        coEvery { repository.getProductDetails(productId) } returns expectedProduct.right()

        // WHEN
        val result = getProductDetailsUseCase(productId)

        // THEN
        assertEquals(expectedProduct.right(), result)
    }

    @Test
    fun `get product details failure`() = testCoroutineScope.runTest {
        // GIVEN
        val productId = "456"
        val expectedError = RepositoryError()
        coEvery { repository.getProductDetails(productId) } returns expectedError.left()

        // WHEN
        val result = getProductDetailsUseCase(productId)

        // THEN
        assertEquals(expectedError.left(), result)
    }
}
