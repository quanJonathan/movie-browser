package com.hitachivantara.mobilecoe.android.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.hitachivantara.mobilecoe.android.data.model.LoggedInUser

@Dao
interface UserDao {
    @Query("SELECT * from User WHERE user_id = :userId")
    fun getUserObserver(userId: String): LiveData<LoggedInUser>

    @Query("SELECT * from User WHERE user_id = :userId")
    fun getUser(userId: String): LoggedInUser

    @Query("SELECT * from User WHERE display_name =:username")
    fun getUserByUserName(username: String): LoggedInUser?

    @Query("SELECT * from User ORDER BY display_name ASC")
    fun getUsers(): LiveData<List<LoggedInUser>>

    @Delete
    fun deleteUser(user: LoggedInUser)

    @Update
    fun updateUser(user: LoggedInUser)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertUser(user: LoggedInUser)
}