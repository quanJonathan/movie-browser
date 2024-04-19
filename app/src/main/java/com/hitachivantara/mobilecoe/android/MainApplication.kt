package com.hitachivantara.mobilecoe.android

import android.app.Application
import com.hitachivantara.mobilecoe.android.data.database.AppDatabase
import com.hitachivantara.mobilecoe.android.data.preferences.PreferencesStore

class MainApplication : Application() {
    val appDatabase by lazy { AppDatabase.getDatabase(this) }
    val preferencesStore by lazy { PreferencesStore.getInstance(this) }
}