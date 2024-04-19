package com.hitachivantara.mobilecoe.android.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hitachivantara.mobilecoe.android.data.LoginDataSource
import com.hitachivantara.mobilecoe.android.data.LoginRepository
import com.hitachivantara.mobilecoe.android.data.database.UserDao
import com.hitachivantara.mobilecoe.android.data.preferences.PreferencesStore

class SettingViewModelFactory(
    private val userDao: UserDao,
    private val preferencesStore: PreferencesStore, private val userId: String
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SettingViewModel::class.java)) {
            return SettingViewModel(
                loginRepository = LoginRepository(
                    dataSource = LoginDataSource(userDao, preferencesStore)
                ), userId
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}