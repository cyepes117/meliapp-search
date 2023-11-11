package com.meliapp.search.domain.usecase

import com.meliapp.search.domain.entities.Product
import com.meliapp.search.domain.repository.ProductRepository

interface GetProductListUseCase : NoInputUseCase<List<Product>>

class GetProductListUseCaseImpl(
    private val productRepository: ProductRepository
) : GetProductListUseCase {
    override suspend operator fun invoke(): List<Product> {
        return productRepository.getProductList()
    }
}
