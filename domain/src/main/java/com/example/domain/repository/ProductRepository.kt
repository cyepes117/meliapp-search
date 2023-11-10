package com.example.domain.repository

import com.example.domain.entities.Product

interface ProductRepository {
    suspend fun getProductList(): List<Product>
    suspend fun getProductDetails(productId: Int): Product
}
