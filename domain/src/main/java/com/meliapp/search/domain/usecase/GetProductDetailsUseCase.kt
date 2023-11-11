package com.meliapp.search.domain.usecase

import com.meliapp.search.domain.entities.Product
import com.meliapp.search.domain.repository.ProductRepository

interface GetProductDetailsUseCase : UseCase<Int, Product>

internal class GetProductDetailsUseCaseImpl(
    private val productRepository: ProductRepository
) : GetProductDetailsUseCase {
    override suspend operator fun invoke(input: Int): Product {
        return productRepository.getProductDetails(input)
    }
}
