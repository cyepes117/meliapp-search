package com.example.data

import com.example.data.api.RemoteProductDataSource
import com.example.data.local.LocalProductDataSource
import com.example.domain.entities.Product
import com.example.domain.repository.ProductRepository

class ProductRepositoryImpl(
    private val remoteDataSource: RemoteProductDataSource,
    private val localDataSource: LocalProductDataSource,
    private val productMapper: ProductMapper
) : ProductRepository {
    override suspend fun getProductList(): List<Product> {
        val remoteResults = remoteDataSource.getProductList()
        val localEntities = remoteResults.map { productMapper.toLocalEntity(it) }
        localDataSource.saveProductList(localEntities)
        return remoteResults.map { productMapper.toDomainEntity(it) }
    }

    override suspend fun getProductDetails(productId: Int): Product {
        val localResult = localDataSource.getProductDetails(productId)
        if (localResult != null) {
            return productMapper.toDomainEntity(localResult)
        }

        val remoteResult = remoteDataSource.getProductDetails(productId)
        if (remoteResult != null) {
            localDataSource.saveProductDetails(productMapper.toLocalEntity(remoteResult))
            return productMapper.toDomainEntity(remoteResult)
        } else {
            // Handle the case when the product details are not available
            throw NoSuchElementException("Product details not found for productId: $productId")
        }
    }
}

