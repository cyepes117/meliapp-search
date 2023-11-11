package com.meliapp.search.domain.usecase

import com.meliapp.search.domain.entities.Product
import com.meliapp.search.domain.repository.ProductRepository

interface GetProductListUseCase : UseCase<String, List<Product>>

internal class GetProductListUseCaseImpl(
    private val productRepository: ProductRepository
) : GetProductListUseCase {
    override suspend operator fun invoke(input: String): List<Product> {
        return productRepository.getProductList(input)
    }
}
