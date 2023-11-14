package com.meliapp.search.data.api

import arrow.core.Either
import com.meliapp.search.data.api.dto.MultiGetResponse
import com.meliapp.search.data.api.dto.SearchResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ProductApiService {
    @GET("/sites/MLA/search")
    suspend fun getProductList(@Query("q") productName: String): Either<ApiError, SearchResponse>

    @GET("products/{productId}")
    suspend fun getProductDetails(@Path("productId") productId: String): Either<ApiError, MultiGetResponse>
}
