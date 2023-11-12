package com.meliapp.search.data.api

import retrofit2.Response

sealed class ApiResponse<out T : Any?> {
    data class Success<out T : Any>(val data: T) : ApiResponse<T>()
    data class Error(val code: Int, val errorMessage: String) : ApiResponse<Nothing>()
}

suspend fun <T : Any?> safeApiCall(apiCall: suspend () -> Response<T>): ApiResponse<T> {
    return try {
        val response = apiCall()
        if (response.isSuccessful) {
            response.body()?.let {
                ApiResponse.Success(it)
            } ?: ApiResponse.Error(response.code(), "Empty response body")
        } else {
            ApiResponse.Error(response.code(), response.message())
        }
    } catch (e: Exception) {
        ApiResponse.Error(0, e.localizedMessage ?: "Unknown error")
    }
}
