package com.example.data.api

import com.example.data.api.dto.ProductDto
import retrofit2.http.GET
import retrofit2.http.Path

interface ProductApiService {
    @GET("products")
    suspend fun getProductList(): List<ProductDto>

    @GET("products/{productId}")
    suspend fun getProductDetails(@Path("productId") productId: Int): ProductDto?
}
