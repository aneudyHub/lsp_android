package com.system.lsp.data.remote.repositories

import androidx.test.runner.AndroidJUnit4
import com.google.gson.Gson
import com.system.lsp.data.local.models.User
import com.system.lsp.data.remote.models.HttpResponseErrorCode
import com.system.lsp.data.remote.models.LoginResponse
import com.system.lsp.di.AppModule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import com.system.lsp.data.remote.models.Result
import com.system.lsp.data.repositories.UsersRepository
import com.system.lsp.di.RepositoriesModule
import dagger.hilt.android.testing.HiltAndroidRule
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Rule
import org.junit.runner.RunWith
import javax.inject.Inject

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
@UninstallModules(AppModule::class, RepositoriesModule::class)
class UsersRepositoryTest {

    @Inject
    lateinit var usersRepository: UsersRepository
    private val mockToken = "6088248A77FFC52FE145108256440F1A7E5E6399A8847C8A76CE77DE52FB5926"
    private val mockUser = User(
        id = 1,
        firstName = "Juan",
        lastName = "Perez",
        email = "example@example.com",
        "8093962555"
    )

    private val gson = Gson()

    @get:Rule
    var hiltRule = HiltAndroidRule(this)
    private var mockWebServer = MockWebServer()


    @Before
    fun setup() {
        hiltRule.inject()
        mockWebServer.start(8080)
    }

    @After
    fun teardown() {
        mockWebServer.shutdown()
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

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(gson.toJson(loginResponse))
        )

        val repositoryResponse = usersRepository.signIn(userName, password)
        val expectedResult = Result.Success(null)
        Assert.assertEquals(expectedResult, repositoryResponse)
        Assert.assertEquals(mockUser, usersRepository.currentUser)
    }

    @Test
    fun signInShouldFail_UserAndPasswordAreWrong() = runTest {
        val userName = "Juan Perez"
        val password = "Pwd12340"

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(400)
        )

        val repositoryResponse = usersRepository.signIn(userName, password)

        val error: Result.Error = repositoryResponse as Result.Error

        Assert.assertEquals(error.error, HttpResponseErrorCode.BAD_REQUEST)
        Assert.assertEquals(null, usersRepository.currentUser)
    }

    @Test
    fun logOut() = runTest {
        signInShouldSuccess()
        usersRepository.logOut()
        Assert.assertEquals(null, usersRepository.currentUser)
    }

}