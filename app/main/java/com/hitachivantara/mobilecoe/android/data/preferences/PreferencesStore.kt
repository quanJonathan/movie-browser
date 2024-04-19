package com.hitachivantara.mobilecoe.android.data.preferences

import android.content.Context
import android.content.SharedPreferences

class PreferencesStore(context: Context) {
    private var sharedPreferences: SharedPreferences

    enum class Key {
        LOGIN_USER,
        API_TOKEN
    }

    init {
        sharedPreferences = context.applicationContext.getSharedPreferences(
            context.packageName,
            Context.MODE_PRIVATE
        )
    }

    companion object {
        private var preferencesStore: PreferencesStore? = null
        fun getInstance(context: Context): PreferencesStore {
            if (preferencesStore == null) {
                preferencesStore = PreferencesStore(context)
            }
            return preferencesStore!!
        }

        private const val API_TOKEN =
            "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJkNDg3N2NmNzYxMGQyMDYzNzYwMThiMGEyYmY4YzRkZiIsInN1YiI6IjY0YTJhNjc2OGUyMGM1MDBlYzNjN2UxNyIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.1yBUqEvlWwTptwWlUAnYFJGDgTjhrQul5qGzISSp1Eg"
    }

    fun saveUser(userId: String) {
        sharedPreferences.edit().putString(Key.LOGIN_USER.name, userId).apply()
    }

    fun clearUser() {
        sharedPreferences.edit().remove(Key.LOGIN_USER.name).apply()
    }

    fun getLoginUserId(): String? {
        return sharedPreferences.getString(Key.LOGIN_USER.name, null)
    }

    fun saveToken() {
        sharedPreferences.edit().putString(Key.API_TOKEN.name, API_TOKEN).apply()
    }

    fun getToken(): String {
        return sharedPreferences.getString(Key.API_TOKEN.name, null) ?: API_TOKEN
    }
}