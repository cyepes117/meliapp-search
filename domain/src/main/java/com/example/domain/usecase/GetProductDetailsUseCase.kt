package com.example.domain.usecase

import com.example.domain.entities.Product
import com.example.domain.repository.ProductRepository

class GetProductDetailsUseCase(private val productRepository: ProductRepository) {
    suspend operator fun invoke(productId: Int): Product {
        return productRepository.getProductDetails(productId)
    }
}
