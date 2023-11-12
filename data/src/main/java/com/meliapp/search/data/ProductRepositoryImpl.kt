package com.meliapp.search.data

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.meliapp.search.data.api.ApiError
import com.meliapp.search.data.api.RemoteProductDataSource
import com.meliapp.search.data.local.LocalProductDataSource
import com.meliapp.search.domain.entities.Product
import com.meliapp.search.domain.repository.ProductRepository
import com.meliapp.search.domain.repository.RepositoryError
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ProductRepositoryImpl(
    private val remoteDataSource: RemoteProductDataSource,
    private val localDataSource: LocalProductDataSource,
    private val productMapper: ProductMapper,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : ProductRepository {

    override suspend fun getProductList(productName: String): Either<RepositoryError, List<Product>> =
        withContext(dispatcher) {

            val localProducts = localDataSource.getProductList(productName)
            if (!localProducts.isNullOrEmpty()) {
                return@withContext localProducts.map { productMapper.toDomainEntity(it) }.right()
            }

            val remoteProducts = remoteDataSource.getProductList(productName)
            remoteProducts.fold(
                ifLeft = {
                    return@withContext it.left()
                },
                ifRight = { productDtoList ->
                    if (productDtoList.isEmpty()) {
                        return@withContext ApiError.NotFoundError.left()
                    } else {
                        localDataSource.saveProductList(
                            productDtoList.map { productDto ->
                                productMapper.toLocalEntity(productDto)
                            }
                        )
                        return@withContext productDtoList.map { productDto ->
                            productMapper.toDomainEntity(productDto)
                        }.right()
                    }
                },
            )
        }

    override suspend fun getProductDetails(productId: String): Either<RepositoryError, Product> =
        withContext(dispatcher) {
            val localProduct = localDataSource.getProductDetails(productId)
            if (localProduct != null) {
                return@withContext productMapper.toDomainEntity(localProduct).right()
            }

            val remoteResult = remoteDataSource.getProductDetails(productId)
            remoteResult.fold(
                ifLeft = {
                    return@withContext it.left()
                },
                ifRight = { productDto ->
                    if (productDto != null) {
                        localDataSource.saveProductDetails(productMapper.toLocalEntity(productDto))
                        return@withContext productMapper.toDomainEntity(productDto).right()
                    } else {
                        return@withContext ApiError.NotFoundError.left()
                    }
                },
            )
        }
}

