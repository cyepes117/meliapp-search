package com.meliapp.search.data.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchResponse(
    @SerialName("site_id") val siteId: String,
    @SerialName("country_default_time_zone") val countryDefaultTimeZone: String,
    @SerialName("query") val query: String,
    @SerialName("results") val results: List<ProductDto>,
)

@Serializable
data class ProductDto(
    @SerialName("id") val id: Int,
    @SerialName("name") val name: String,
    @SerialName("description") val description: String,
    @SerialName("price") val price: Double,
)