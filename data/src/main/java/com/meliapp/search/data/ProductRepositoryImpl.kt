package com.meliapp.search.data

import com.meliapp.search.data.api.RemoteProductDataSource
import com.meliapp.search.data.local.LocalProductDataSource
import com.meliapp.search.domain.entities.Product
import com.meliapp.search.domain.repository.ProductRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ProductRepositoryImpl(
    private val remoteDataSource: RemoteProductDataSource,
    private val localDataSource: LocalProductDataSource,
    private val productMapper: ProductMapper,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : ProductRepository {
    override suspend fun getProductList(productName: String): List<Product> =
        withContext(dispatcher) {
            val localProducts = localDataSource.getProductList(productName)
            if (!localProducts.isNullOrEmpty()) {
                return@withContext localProducts.map { productMapper.toDomainEntity(it) }
            }

            val remoteProducts = remoteDataSource.getProductList(productName)
            localDataSource.saveProductList(remoteProducts.map { productMapper.toLocalEntity(it) })

            return@withContext remoteProducts.map { productMapper.toDomainEntity(it) }
        }

    override suspend fun getProductDetails(productId: Int): Product =
        withContext(dispatcher) {
            val localProduct = localDataSource.getProductDetails(productId)
            if (localProduct != null) {
                return@withContext productMapper.toDomainEntity(localProduct)
            }

            val remoteResult = remoteDataSource.getProductDetails(productId)
            if (remoteResult != null) {
                localDataSource.saveProductDetails(productMapper.toLocalEntity(remoteResult))
                return@withContext productMapper.toDomainEntity(remoteResult)
            } else {
                // Handle the case when the product details are not available
                throw NoSuchElementException("Product details not found for productId: $productId")
            }
        }
}

