package com.example.domain.usecase

import com.example.domain.entities.Product
import com.example.domain.repository.ProductRepository

interface GetProductListUseCase : NoInputUseCase<List<Product>>

class GetProductListUseCaseImpl(
    private val productRepository: ProductRepository
) : GetProductListUseCase {
    override suspend operator fun invoke(): List<Product> {
        return productRepository.getProductList()
    }
}
