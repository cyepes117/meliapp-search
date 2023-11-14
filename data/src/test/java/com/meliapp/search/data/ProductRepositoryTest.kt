package com.meliapp.search.data

import arrow.core.right
import com.meliapp.search.data.api.ApiError
import com.meliapp.search.data.api.RemoteProductDataSource
import com.meliapp.search.data.api.dto.ProductDto
import com.meliapp.search.data.local.LocalProductDataSource
import com.meliapp.search.data.local.entities.ProductEntity
import com.meliapp.search.domain.repository.ProductRepository
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

internal class ProductRepositoryTest {

    private lateinit var productRepository: ProductRepository
    private lateinit var remoteDataSource: RemoteProductDataSource
    private lateinit var localDataSource: LocalProductDataSource
    private lateinit var productMapper: ProductMapper
    private val testDispatcher = StandardTestDispatcher()
    private val testCoroutineScope = TestScope(testDispatcher)

    @BeforeEach
    fun setUp() {
        remoteDataSource = mockk()
        localDataSource = mockk()
        productMapper = ProductMapperImpl()

        productRepository = ProductRepositoryImpl(
            remoteDataSource = remoteDataSource,
            localDataSource = localDataSource,
            productMapper = productMapper,
            dispatcher = testDispatcher,
        )
    }

    @Nested
    inner class GetProductList {

        @Test
        fun `returns local products when available`() = testCoroutineScope.runTest {
            // GIVEN
            val productName = "testProduct"
            val localProducts =
                listOf(
                    ProductEntity(
                        id = "1",
                        title = "Local Product",
                        price = 99.99,
                        thumbnail = ""
                    )
                )
            coEvery { localDataSource.getProductList(productName) } returns localProducts

            // WHEN
            val result = productRepository.getProductList(productName)

            // THEN
            assertTrue(result.isRight())
            assertEquals(localProducts.map { productMapper.toDomainEntity(it) }, result.getOrNull())
        }

        @Test
        fun `returns remote products and saves them locally`() = testCoroutineScope.runTest {
            // GIVEN
            val productName = "testProduct"
            val remoteProducts =
                listOf(
                    ProductDto(
                        id = "2",
                        title = "Remote Product",
                        price = 149.99,
                        thumbnail = ""
                    )
                )
            coEvery { localDataSource.getProductList(productName) } returns null
            coEvery { remoteDataSource.getProductList(productName) } returns remoteProducts.right()
            coEvery { localDataSource.saveProductList(any()) } just Runs

            // WHEN
            val result = productRepository.getProductList(productName)

            // THEN
            assertTrue(result.isRight())
            assertEquals(
                remoteProducts.map { productMapper.toDomainEntity(it) },
                result.getOrNull()
            )
        }

        @Test
        fun `returns NotFoundError when remote products are empty`() = testCoroutineScope.runTest {
            // GIVEN
            val productName = "testProduct"
            coEvery { localDataSource.getProductList(productName) } returns null
            coEvery { remoteDataSource.getProductList(productName) } returns emptyList<ProductDto>().right()

            // WHEN
            val result = productRepository.getProductList(productName)

            // THEN
            assertTrue(result.isLeft())
            assertEquals(ApiError.NotFoundError, result.leftOrNull())
        }
    }

    @Nested
    inner class GetProductDetails {

        @Test
        fun `returns local product when available`() = testCoroutineScope.runTest {
            // GIVEN
            val productId = "testProductId"
            val localProduct =
                ProductEntity(
                    id = productId,
                    title = "Local Product",
                    price = 99.99,
                    thumbnail = ""
                )
            coEvery { localDataSource.getProductDetails(productId) } returns localProduct

            // WHEN
            val result = productRepository.getProductDetails(productId)

            // THEN
            assertTrue(result.isRight())
            assertEquals(productMapper.toDomainEntity(localProduct), result.getOrNull())
        }

        @Test
        fun `returns remote product and saves it locally`() = testCoroutineScope.runTest {
            // GIVEN
            val productId = "testProductId"
            val remoteProductDto =
                ProductDto(id = productId, title = "Remote Product", price = 149.99, thumbnail = "")
            coEvery { localDataSource.getProductDetails(productId) } returns null
            coEvery { remoteDataSource.getProductDetails(productId) } returns remoteProductDto.right()
            coEvery { localDataSource.saveProductDetails(any()) } just Runs

            // WHEN
            val result = productRepository.getProductDetails(productId)

            // THEN
            assertTrue(result.isRight())
            assertEquals(productMapper.toDomainEntity(remoteProductDto), result.getOrNull())
        }

        @Test
        fun `returns NotFoundError when remote product is null`() = testCoroutineScope.runTest {
            // GIVEN
            val productId = "testProductId"
            coEvery { localDataSource.getProductDetails(productId) } returns null
            coEvery { remoteDataSource.getProductDetails(productId) } returns null.right()

            // WHEN
            val result = productRepository.getProductDetails(productId)

            // THEN
            assertTrue(result.isLeft())
            assertEquals(ApiError.NotFoundError, result.leftOrNull())
        }
    }
}
