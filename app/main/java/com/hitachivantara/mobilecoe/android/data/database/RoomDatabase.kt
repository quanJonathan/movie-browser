package com.hitachivantara.mobilecoe.android.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.ProvidedTypeConverter
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.hitachivantara.mobilecoe.android.data.model.Genre
import com.hitachivantara.mobilecoe.android.data.model.LoggedInUser
import com.hitachivantara.mobilecoe.android.data.model.Movie


class GenreListConverter {

    @TypeConverter
    fun fromJsonString(value: String): List<Genre>? {
        val listType = object : TypeToken<List<Genre>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun genreToString(value: List<Genre>): String{
        return Gson().toJson(value)
    }
}

@Database(entities = [LoggedInUser::class, Movie::class], version = 2, exportSchema = false)
@TypeConverters(GenreListConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun watchListDao(): WatchListMovieDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}