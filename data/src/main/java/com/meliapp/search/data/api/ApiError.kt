package com.meliapp.search.data.api

import com.meliapp.search.domain.repository.RepositoryError

sealed class ApiError : RepositoryError() {
    data class HttpError(val code: Int, val body: String) : ApiError()
    data class NetworkError(val throwable: Throwable) : ApiError()
    data class UnknownApiError(val throwable: Throwable) : ApiError()
    data object NotFoundError : ApiError()
}
