package com.meliapp.search.data

import com.meliapp.search.data.api.dto.ProductDto
import com.meliapp.search.data.local.entities.ProductEntity
import com.meliapp.search.domain.entities.Product
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class ProductMapperTest {

    private val productMapper: ProductMapper = ProductMapperImpl()

    @Test
    fun `map Product to ProductEntity`() {
        // GIVEN
        val product =
            Product(id = "123", title = "Test Product", price = 99.99, thumbnail = "image.jpg")

        // WHEN
        val result = productMapper.toLocalEntity(product)

        // THEN
        assertEquals(product.id, result.id)
        assertEquals(product.title, result.title)
        assertEquals(product.price, result.price)
        assertEquals(product.thumbnail, result.thumbnail)
    }

    @Test
    fun `map ProductDto to ProductEntity`() {
        // GIVEN
        val productDto = ProductDto(
            id = "456",
            title = "Test Product 2",
            price = 149.99,
            thumbnail = "image2.jpg"
        )

        // WHEN
        val result = productMapper.toLocalEntity(productDto)

        // THEN
        assertEquals(productDto.id, result.id)
        assertEquals(productDto.title, result.title)
        assertEquals(productDto.price, result.price)
        assertEquals(productDto.thumbnail, result.thumbnail)
    }

    @Test
    fun `map ProductEntity to Product`() {
        // GIVEN
        val productEntity = ProductEntity(
            id = "789",
            title = "Test Product 3",
            price = 199.99,
            thumbnail = "image3.jpg"
        )

        // WHEN
        val result = productMapper.toDomainEntity(productEntity)

        // THEN
        assertEquals(productEntity.id, result.id)
        assertEquals(productEntity.title, result.title)
        assertEquals(productEntity.price, result.price)
        assertEquals(productEntity.thumbnail, result.thumbnail)
    }

    @Test
    fun `map ProductDto to Product`() {
        // GIVEN
        val productDto = ProductDto(
            id = "101",
            title = "Test Product 4",
            price = 249.99,
            thumbnail = "image4.jpg"
        )

        // WHEN
        val result = productMapper.toDomainEntity(productDto)

        // THEN
        assertEquals(productDto.id, result.id)
        assertEquals(productDto.title, result.title)
        assertEquals(productDto.price, result.price)
        assertEquals(productDto.thumbnail, result.thumbnail)
    }
}
