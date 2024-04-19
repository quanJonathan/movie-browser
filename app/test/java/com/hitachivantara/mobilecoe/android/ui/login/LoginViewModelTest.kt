package com.hitachivantara.mobilecoe.android.ui.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.hitachivantara.mobilecoe.android.DispatcherProvider
import com.hitachivantara.mobilecoe.android.TestDispatcherProvider
import com.hitachivantara.mobilecoe.android.data.LoginRepository
import com.hitachivantara.mobilecoe.android.data.Result
import com.hitachivantara.mobilecoe.android.data.model.LoggedInUser
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.anyString
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations.openMocks

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class LoginViewModelTest {
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()
    private lateinit var sut: LoginViewModel

    @Mock
    private lateinit var repository: LoginRepository
    private lateinit var mocks: AutoCloseable
    private lateinit var dispatcherProvider: DispatcherProvider

    @Before
    fun setUp() {
        mocks = openMocks(this)
        dispatcherProvider = TestDispatcherProvider()
        sut = LoginViewModel(repository, dispatcherProvider)

    }

    @Test
    fun test_loginUser_shouldReturnSuccess() {
        val user = mock(LoggedInUser::class.java)
        Mockito.`when`(user.displayName).thenReturn("test")
        runTest {
            Mockito.doReturn(Result.Success(user)).`when`(repository)
                .login(anyString(), anyString())
            sut.login(anyString(), anyString())
            Assert.assertNull(sut.loginResult.value?.error)
            Assert.assertNotNull(sut.loginResult.value?.success)
            Assert.assertEquals("test", sut.loginResult.value?.success?.displayName)
        }
    }


    @Test
    fun test_loginUser_shouldReturnError() {
        val user = mock(LoggedInUser::class.java)
        Mockito.`when`(user.displayName).thenReturn("test")
        runTest {
            Mockito.doReturn(Result.Error(Exception("failed"))).`when`(repository)
                .login(anyString(), anyString())
            sut.login(anyString(), anyString())
            Assert.assertNull(sut.loginResult.value?.success)
            Assert.assertNotNull(sut.loginResult.value?.error)
            Assert.assertEquals("failed", sut.loginResult.value?.error)
        }
    }

    @Test
    fun test_validateUsername_shouldNotValid() {
        sut.loginDataChanged("test@", "")
        Assert.assertNotNull(sut.loginFormState.value?.usernameError)
    }

    @Test
    fun test_validateUsername_shouldValid() {
        sut.loginDataChanged("test", "")
        Assert.assertNull(sut.loginFormState.value?.usernameError)

        sut.loginDataChanged("test@g.com", "")
        Assert.assertNull(sut.loginFormState.value?.usernameError)
    }

    @Test
    fun test_validatePassword_shouldNotValid() {
        sut.loginDataChanged("test", "")
        Assert.assertNull(sut.loginFormState.value?.usernameError)
        Assert.assertNotNull(sut.loginFormState.value?.passwordError)
    }

    @Test
    fun test_validatePassword_shouldValid() {
        sut.loginDataChanged("test", "123456")
        Assert.assertNull(sut.loginFormState.value?.usernameError)
        Assert.assertNull(sut.loginFormState.value?.passwordError)
        Assert.assertNotNull(sut.loginFormState.value?.isDataValid)
    }

    @After
    fun tearDown() {
        mocks.close()
    }
}