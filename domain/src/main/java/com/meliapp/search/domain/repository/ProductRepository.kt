package com.meliapp.search.domain.repository

import com.meliapp.search.domain.entities.Product

interface ProductRepository {
    suspend fun getProductList(productName: String): List<Product>
    suspend fun getProductDetails(productId: String): Product
}
