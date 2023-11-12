package com.meliapp.search.data.api.dto

import com.google.gson.annotations.SerializedName

data class SearchResponse(
    @SerializedName("site_id") val siteId: String,
    @SerializedName("country_default_time_zone") val countryDefaultTimeZone: String,
    @SerializedName("query") val query: String,
    @SerializedName("results") val results: List<ProductDto>,
)

data class ProductDto(
    @SerializedName("id") val id: String,
    @SerializedName("title") val title: String,
    @SerializedName("thumbnail") val thumbnail: String,
    @SerializedName("price") val price: Double,
)

data class MultiGetResponse(
    @SerializedName("code") val code: Int,
    @SerializedName("body") val result: ProductDto,
)