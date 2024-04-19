package com.hitachivantara.mobilecoe.android.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hitachivantara.mobilecoe.android.DefaultDispatcherProvider
import com.hitachivantara.mobilecoe.android.data.LoginDataSource
import com.hitachivantara.mobilecoe.android.data.LoginRepository
import com.hitachivantara.mobilecoe.android.data.database.UserDao
import com.hitachivantara.mobilecoe.android.data.preferences.PreferencesStore

/**
 * ViewModel provider factory to instantiate LoginViewModel.
 * Required given LoginViewModel has a non-empty constructor
 */
class LoginViewModelFactory(
    private val userDao: UserDao,
    private val preferencesStore: PreferencesStore
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(
                loginRepository = LoginRepository(
                    dataSource = LoginDataSource(userDao, preferencesStore)
                ), DefaultDispatcherProvider()
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}