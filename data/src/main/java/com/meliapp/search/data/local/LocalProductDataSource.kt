package com.meliapp.search.data.local

import com.meliapp.search.data.local.entities.ProductEntity

interface LocalProductDataSource {
    suspend fun getProductList(productName: String): List<ProductEntity>?
    suspend fun getProductDetails(productId: Int): ProductEntity?
    suspend fun saveProductList(products: List<ProductEntity>)
    suspend fun saveProductDetails(product: ProductEntity)
}

internal class LocalProductDataSourceImpl(private val productDao: ProductDao) :
    LocalProductDataSource {
    override suspend fun getProductList(productName: String): List<ProductEntity>? {
        return productDao.getProductList(productName)
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
