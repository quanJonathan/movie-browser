package com.hitachivantara.mobilecoe.android.data.api

import retrofit2.Response

sealed class ApiResponse<out T> {
    companion object {
        fun <T> create(response: Response<T>): ApiResponse<T> {
            return if (response.isSuccessful) {
                val body = response.body()
                if (body == null || response.code() == 204) {
                    ApiEmptyResponse()
                } else {
                    ApiSuccessResponse(body)
                }
            } else {
                ApiErrorResponse(
                    response.code(),
                    response.errorBody()?.string() ?: response.message()
                )
            }
        }

        fun create(errorCode: Int, error: Throwable): ApiErrorResponse {
            return ApiErrorResponse(errorCode, error.message ?: "Unknown Error!")
        }
    }
}

class ApiEmptyResponse<T> : ApiResponse<T>()
data class ApiErrorResponse(val errorCode: Int, val errorMessage: String) : ApiResponse<Nothing>()
data class ApiSuccessResponse<T>(val body: T) : ApiResponse<T>()