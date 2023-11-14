package com.meliapp.search.data.local

import com.meliapp.search.data.local.entities.ProductEntity
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class LocalProductDataSourceTest {

    private val testDispatcher = StandardTestDispatcher()
    private val testCoroutineScope = TestScope(testDispatcher)
    private lateinit var localProductDataSource: LocalProductDataSource
    private lateinit var productDao: ProductDao

    @BeforeEach
    fun setUp() {
        productDao = mockk()
        localProductDataSource = LocalProductDataSourceImpl(productDao)
    }

    @Test
    fun `get product list success`() = testCoroutineScope.runTest {
        // GIVEN
        val productName = "testProduct"
        val expectedProductList = listOf(
            ProductEntity(id = "1", title = "Product 1", thumbnail = "", price = 20.0),
            ProductEntity(id = "2", title = "Product 2", thumbnail = "", price = 30.0)
        )
        coEvery { productDao.getProductList(productName) } returns expectedProductList

        // WHEN
        val result = localProductDataSource.getProductList(productName)

        // THEN
        assertEquals(expectedProductList, result)
    }

    @Test
    fun `get product details success`() = testCoroutineScope.runTest {
        // GIVEN
        val productId = "123"
        val expectedProductDetails =
            ProductEntity(id = productId, title = "Test Product", price = 99.99, thumbnail = "")
        coEvery { productDao.getProductDetails(productId) } returns expectedProductDetails

        // WHEN
        val result = localProductDataSource.getProductDetails(productId)

        // THEN
        assertEquals(expectedProductDetails, result)
    }

    @Test
    fun `save product list success`() = testCoroutineScope.runTest {
        // GIVEN
        val productsToSave = listOf(
            ProductEntity(id = "1", title = "Product 1", thumbnail = "", price = 20.0),
            ProductEntity(id = "2", title = "Product 2", thumbnail = "", price = 30.0)
        )
        coEvery { productDao.saveProductList(productsToSave) } just Runs

        // WHEN
        localProductDataSource.saveProductList(productsToSave)

        // THEN
        coVerify { productDao.saveProductList(productsToSave) }
    }

    @Test
    fun `save product details success`() = testCoroutineScope.runTest {
        // GIVEN
        val productToSave =
            ProductEntity(id = "123", title = "Test Product", price = 99.99, thumbnail = "")
        coEvery { productDao.saveProductDetails(productToSave) } just Runs

        // WHEN
        localProductDataSource.saveProductDetails(productToSave)

        // THEN
        coVerify { productDao.saveProductDetails(productToSave) }
    }
}
