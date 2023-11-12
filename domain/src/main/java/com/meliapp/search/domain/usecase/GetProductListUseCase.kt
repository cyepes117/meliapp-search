package com.meliapp.search.domain.usecase

import arrow.core.Either
import com.meliapp.search.domain.entities.Product
import com.meliapp.search.domain.repository.ProductRepository
import com.meliapp.search.domain.repository.RepositoryError

interface GetProductListUseCase : UseCase<String, Either<RepositoryError, List<Product>>>

internal class GetProductListUseCaseImpl(
    private val productRepository: ProductRepository
) : GetProductListUseCase {

    override suspend operator fun invoke(input: String): Either<RepositoryError, List<Product>> {
        return productRepository.getProductList(input)
    }
}
