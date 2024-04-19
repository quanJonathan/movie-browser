package com.hitachivantara.mobilecoe.android.data.model

data class Review(
    val author: String,
    val content: String,
    val author_details: Author
)

data class Author(
    val avatar_path: String?,
    val username: String,
    val rating: Int,
    val name: String?
)