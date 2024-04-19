package com.hitachivantara.mobilecoe.android.ui.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hitachivantara.mobilecoe.android.data.LoginRepository
import com.hitachivantara.mobilecoe.android.data.Result
import com.hitachivantara.mobilecoe.android.data.model.LoggedInUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SettingViewModel(private val loginRepository: LoginRepository, userId: String) :
    ViewModel() {
    private val _updateUser: MutableLiveData<Result<LoggedInUser>> = MutableLiveData()
    var updateUser: LiveData<Result<LoggedInUser>> = _updateUser
    var user: LiveData<LoggedInUser> = getUser(userId)
    fun getUser(userId: String): LiveData<LoggedInUser> {
        return loginRepository.getUser(userId)
    }

    fun updateUser(user: LoggedInUser) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = loginRepository.updateUser(user)
            withContext(Dispatchers.Main) {
                _updateUser.value = result
            }
        }
    }

    fun logout() {
        loginRepository.logout()
    }

}