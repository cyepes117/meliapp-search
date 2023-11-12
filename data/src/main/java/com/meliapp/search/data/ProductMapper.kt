package com.meliapp.search.data

import com.meliapp.search.data.api.dto.ProductDto
import com.meliapp.search.data.local.entities.ProductEntity
import com.meliapp.search.domain.entities.Product

interface ProductMapper {
    fun toLocalEntity(product: Product): ProductEntity
    fun toLocalEntity(productDto: ProductDto): ProductEntity
    fun toDomainEntity(productEntity: ProductEntity): Product
    fun toDomainEntity(productDto: ProductDto): Product
}

class ProductMapperImpl : ProductMapper {
    override fun toLocalEntity(product: Product): ProductEntity {
        return ProductEntity(
            id = product.id,
            title = product.title,
            thumbnail = product.thumbnail,
            price = product.price
        )
    }

    override fun toLocalEntity(productDto: ProductDto): ProductEntity {
        return ProductEntity(
            id = productDto.id,
            title = productDto.title,
            thumbnail = productDto.thumbnail,
            price = productDto.price
        )
    }

    override fun toDomainEntity(productEntity: ProductEntity): Product {
        return Product(
            id = productEntity.id,
            title = productEntity.title,
            thumbnail = productEntity.thumbnail,
            price = productEntity.price
        )
    }

    override fun toDomainEntity(productDto: ProductDto): Product {
        return Product(
            id = productDto.id,
            title = productDto.title,
            thumbnail = productDto.thumbnail,
            price = productDto.price
        )
    }
}
