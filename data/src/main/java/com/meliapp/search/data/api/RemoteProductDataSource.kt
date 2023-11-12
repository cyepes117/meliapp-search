package com.meliapp.search.data.api

import com.meliapp.search.data.api.dto.ProductDto

interface RemoteProductDataSource {
    suspend fun getProductList(productName: String): List<ProductDto>
    suspend fun getProductDetails(productId: String): ProductDto?
}

internal class RemoteProductDataSourceImpl(private val apiService: ProductApiService) :
    RemoteProductDataSource {
    override suspend fun getProductList(productName: String): List<ProductDto> {
        return apiService.getProductList(productName).results
    }

    override suspend fun getProductDetails(productId: String): ProductDto? {
        return apiService.getProductDetails(productId)
    }
}
