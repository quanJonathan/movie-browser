package com.hitachivantara.mobilecoe.android.data.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
@Entity("User")
data class LoggedInUser(
    @PrimaryKey
    @ColumnInfo("user_id")
    val userId: String,
    @ColumnInfo("display_name")
    val displayName: String,
    @ColumnInfo("password")
    val password: String,
)
