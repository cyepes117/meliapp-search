package com.meliapp.search.domain.usecase

import arrow.core.Either
import com.meliapp.search.domain.entities.Product
import com.meliapp.search.domain.repository.ProductRepository
import com.meliapp.search.domain.repository.RepositoryError

interface GetProductDetailsUseCase : UseCase<String, Either<RepositoryError, Product>>

internal class GetProductDetailsUseCaseImpl(
    private val productRepository: ProductRepository
) : GetProductDetailsUseCase {

    override suspend operator fun invoke(input: String): Either<RepositoryError, Product> {
        return productRepository.getProductDetails(input)
    }
}
