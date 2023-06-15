package com.system.lsp.repositories

import com.system.lsp.data.local.models.User
import com.system.lsp.data.local.sharedpreferences.UserSessionSharedPreferences
import com.system.lsp.data.remote.api.ApiService
import com.system.lsp.data.remote.models.ApiError
import com.system.lsp.data.remote.models.ApiResponse
import com.system.lsp.data.remote.models.ErrorType
import com.system.lsp.data.remote.models.LoginResponse
import com.system.lsp.data.remote.repositories.UsersRepository
import com.system.lsp.data.remote.repositories.UsersRepositoryImpl
import com.system.lsp.di.AppModule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import com.system.lsp.data.remote.models.Result
import org.mockito.Mockito.times
import org.mockito.Mockito.verify

@HiltAndroidTest
@UninstallModules(AppModule::class)
class UsersRepositoryTest {

    private lateinit var usersRepository: UsersRepository
    private var mockApiService: ApiService = mock(ApiService::class.java)
    private var mockUserSessionPref: UserSessionSharedPreferences =
        mock(UserSessionSharedPreferences::class.java)
    private val mockToken = "6088248A77FFC52FE145108256440F1A7E5E6399A8847C8A76CE77DE52FB5926"
    private val mockUser = User(
        id = 1,
        firstName = "Juan",
        lastName = "Perez",
        email = "example@example.com",
        "8093962555"
    )


    @Before
    fun setup() {
        usersRepository = UsersRepositoryImpl(mockApiService, mockUserSessionPref)
    }

    @Test
    fun signInShouldSuccess() = runTest {
        val userName = "Juan Perez"
        val password = "Pwd12340"

        val loginResponse = LoginResponse(
            id = 1,
            firstName = "Juan",
            lastName = "Perez",
            email = "example@example.com",
            "8093962555",
            token = mockToken
        )

        val apiResponse = ApiResponse(true, loginResponse, null)
        `when`(mockApiService.signIn(userName, password)).thenReturn(apiResponse)
        val repositoryResponse = usersRepository.signIn(userName, password)
        `when`(mockUserSessionPref.retrieveUser()).thenReturn(mockUser)
        verify(mockUserSessionPref, times(1)).saveUser(mockUser)

        val expectedResult = Result.Success(null)
        Assert.assertEquals(expectedResult, repositoryResponse)
        Assert.assertEquals(mockUser, usersRepository.currentUser)
    }

    @Test
    fun signInShouldFail() = runTest {
        val userName = "Juan Perez"
        val password = "Pwd12340"
        val apiError = ApiError(401, "usuario y contrasena incorrecto")
        val apiResponse = ApiResponse<LoginResponse>(false, null, apiError)


        `when`(mockApiService.signIn(userName, password)).thenReturn(apiResponse)
        val repositoryResponse = usersRepository.signIn(userName, password)
        val expectedResult = Result.Error(ErrorType.USER_OR_PASS_WRONG)

        Assert.assertEquals(expectedResult, repositoryResponse)
        Assert.assertEquals(null, usersRepository.currentUser)
    }

    @Test
    fun logOut() = runTest {
        signInShouldSuccess()
        usersRepository.logOut()
        verify(mockUserSessionPref, times(1)).dropSession()
        `when`(mockUserSessionPref.retrieveUser()).thenReturn(null)
        Assert.assertEquals(null, usersRepository.currentUser)
    }

}