package com.hitachivantara.mobilecoe.android.data

import androidx.lifecycle.LiveData
import com.hitachivantara.mobilecoe.android.data.database.UserDao
import com.hitachivantara.mobilecoe.android.data.model.LoggedInUser
import com.hitachivantara.mobilecoe.android.data.preferences.PreferencesStore
import java.io.IOException
import java.math.BigInteger
import java.security.MessageDigest
import java.util.UUID

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource(
    private val userDao: UserDao,
    private val preferencesStore: PreferencesStore
) {

    fun login(username: String, password: String): Result<LoggedInUser> {
        return try {
            val fakeUser =
                LoggedInUser(UUID.randomUUID().toString(), username, password.md5())
            val user = userDao.getUserByUserName(username)
            if (user == null) {
                userDao.insertUser(fakeUser)
                preferencesStore.saveUser(fakeUser.userId)
                Result.Success(fakeUser)
            } else if (user.password == password.md5()) {
                preferencesStore.saveUser(user.userId)
                Result.Success(user)
            } else {
                Result.Error(IOException("Wrong password!"))
            }
        } catch (e: Throwable) {
            Result.Error(IOException("Error logging in", e))
        }
    }

    fun logout() {
        preferencesStore.clearUser()
    }

    fun updateUser(user: LoggedInUser): Result<LoggedInUser> {
        val dbUser = userDao.getUser(userId = user.userId)
        val isValidUserName = userDao.getUserByUserName(user.displayName) == null
        return if (user.password == dbUser.password && isValidUserName) {
            userDao.updateUser(user)
            Result.Success(user)
        } else {
            Result.Error(Exception(if (!isValidUserName) "The username already taken!!" else "Wrong password!!"))
        }
    }

    fun getUser(userId: String): LiveData<LoggedInUser> {
        return userDao.getUserObserver(userId)
    }
}

fun String.md5(): String {
    val md = MessageDigest.getInstance("MD5")
    return BigInteger(1, md.digest(this.toByteArray())).toString(16).padStart(32, '0')
}
