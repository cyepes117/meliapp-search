package com.example.domain.usecase

import com.example.domain.entities.Product
import com.example.domain.repository.ProductRepository

class GetProductListUseCase(private val productRepository: ProductRepository) {
    suspend operator fun invoke(): List<Product> {
        return productRepository.getProductList()
    }
}
