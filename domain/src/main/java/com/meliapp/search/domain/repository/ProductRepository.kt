package com.meliapp.search.domain.repository

import com.meliapp.search.domain.entities.Product

interface ProductRepository {
    suspend fun getProductList(): List<Product>
    suspend fun getProductDetails(productId: Int): Product
}
