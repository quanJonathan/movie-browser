package com.hitachivantara.mobilecoe.android.data.api
import com.google.gson.Gson
import com.hitachivantara.mobilecoe.android.data.model.response.ErrorResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

fun <T> Call<T>.enqueue(callback: (ApiResponse<T>) -> Unit) {
    this.enqueue(object : Callback<T> {
        override fun onResponse(call: Call<T>, response: Response<T>) {
            if (response.code() == 200 && response.body() != null) {
                if (response.body() == null) {
                    callback.invoke(ApiEmptyResponse())
                } else {
                    response.body()?.let { callback.invoke(ApiSuccessResponse(it)) }
                }
            } else if (response.code() == 404) {
                val errorResponse = Gson().fromJson(response.errorBody()?.string() ?: "", ErrorResponse::class.java)
                callback.invoke(ApiResponse.create(errorResponse.status_code, Throwable(errorResponse.status_message)))
            }
        }

        override fun onFailure(call: Call<T>, t: Throwable) {
            callback.invoke(ApiResponse.create(-1, t))
        }
    })
}