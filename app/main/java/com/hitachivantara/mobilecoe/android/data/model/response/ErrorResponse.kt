package com.hitachivantara.mobilecoe.android.data.model.response

data class ErrorResponse(
    val status_code: Int,
    val status_message: String,
    val success: Boolean
)