package com.meliapp.search.data.api

import arrow.core.Either
import com.meliapp.search.data.api.dto.ProductDto

interface RemoteProductDataSource {
    suspend fun getProductList(productName: String): Either<ApiError, List<ProductDto>>
    suspend fun getProductDetails(productId: String): Either<ApiError, ProductDto?>
}

internal class RemoteProductDataSourceImpl(private val apiService: ProductApiService) :
    RemoteProductDataSource {
    override suspend fun getProductList(productName: String): Either<ApiError, List<ProductDto>> {
        return apiService.getProductList(productName).map { it.results }
    }

    override suspend fun getProductDetails(productId: String): Either<ApiError, ProductDto?> {
        return apiService.getProductDetails(productId).map { it.result }
    }
}
