package com.meliapp.search.domain.usecase

import com.meliapp.search.domain.entities.Product
import com.meliapp.search.domain.repository.ProductRepository

interface GetProductDetailsUseCase : UseCase<String, Product>

internal class GetProductDetailsUseCaseImpl(
    private val productRepository: ProductRepository
) : GetProductDetailsUseCase {
    override suspend operator fun invoke(input: String): Product {
        return productRepository.getProductDetails(input)
    }
}
