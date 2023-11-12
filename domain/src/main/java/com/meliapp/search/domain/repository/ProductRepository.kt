package com.meliapp.search.domain.repository

import arrow.core.Either
import com.meliapp.search.domain.entities.Product

interface ProductRepository {
    suspend fun getProductList(productName: String): Either<RepositoryError, List<Product>>
    suspend fun getProductDetails(productId: String): Either<RepositoryError, Product>
}
