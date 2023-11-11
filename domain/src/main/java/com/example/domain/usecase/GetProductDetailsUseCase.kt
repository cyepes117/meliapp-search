package com.example.domain.usecase

import com.example.domain.entities.Product
import com.example.domain.repository.ProductRepository

interface GetProductDetailsUseCase : UseCase<Int, Product>

internal class GetProductDetailsUseCaseImpl(
    private val productRepository: ProductRepository
) : GetProductDetailsUseCase {
    override suspend operator fun invoke(input: Int): Product {
        return productRepository.getProductDetails(input)
    }
}
