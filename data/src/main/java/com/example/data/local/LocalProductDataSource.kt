package com.example.data.local

import com.example.data.local.entities.ProductEntity

interface LocalProductDataSource {
    suspend fun getProductList(): List<ProductEntity>
    suspend fun getProductDetails(productId: Int): ProductEntity?
    suspend fun saveProductList(products: List<ProductEntity>)
    suspend fun saveProductDetails(product: ProductEntity)
}

internal class LocalProductDataSourceImpl(private val productDao: ProductDao) : LocalProductDataSource {
    override suspend fun getProductList(): List<ProductEntity> {
        return productDao.getProductList()
    }

    override suspend fun getProductDetails(productId: Int): ProductEntity? {
        return productDao.getProductDetails(productId)
    }

    override suspend fun saveProductList(products: List<ProductEntity>) {
        productDao.saveProductList(products)
    }

    override suspend fun saveProductDetails(product: ProductEntity) {
        productDao.saveProductDetails(product)
    }
}
