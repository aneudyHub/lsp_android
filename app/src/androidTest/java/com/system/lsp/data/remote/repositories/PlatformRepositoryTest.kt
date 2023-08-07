package com.system.lsp.data.remote.repositories

import androidx.test.runner.AndroidJUnit4
import com.google.gson.Gson
import com.system.lsp.data.local.models.PlatformSession
import com.system.lsp.data.remote.models.HttpResponseErrorCode
import com.system.lsp.data.remote.models.PlatformAuthorizationResponse
import com.system.lsp.data.remote.models.Result
import com.system.lsp.data.repositories.PlatformSessionRepository
import com.system.lsp.di.AppModule
import com.system.lsp.di.RepositoriesModule
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
@UninstallModules(AppModule::class, RepositoriesModule::class)
class PlatformRepositoryTest {

    @Inject
    lateinit var platformSessionRepository: PlatformSessionRepository

    private val mockHashCode = "0000-0000"
    private val mockApiUrl = "https://api.central.com"

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
    fun authenticationShouldFail() = runTest {

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(400)
        )

        val response = platformSessionRepository.authenticate(mockHashCode)
        val error: Result.Error = response as Result.Error
        Assert.assertEquals(error.error, HttpResponseErrorCode.BAD_REQUEST)
        Assert.assertEquals(Result.Success(null),platformSessionRepository.loadPlatformSession())
        Assert.assertNull(platformSessionRepository.apiUrl)
    }


    @Test
    fun authenticationShouldSuccess()= runTest{
        val platformAuthorizationResponse = PlatformAuthorizationResponse(
            apiUrl = mockApiUrl
        )

        // todo check the ticket [LSP_AND-6 Move the company info from api to the authentication platform flow]
        val platformSession = PlatformSession(
            hashCodeAuthenticator = "",
            companyName = "",
            companyAddress = "",
            companyPhone = "",
            apiUrl = mockApiUrl
        )

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(gson.toJson(platformAuthorizationResponse))
        )

        val response = platformSessionRepository.authenticate(mockHashCode)

        Assert.assertEquals(Result.Success(null),response)
        Assert.assertEquals(mockApiUrl,platformSessionRepository.apiUrl)
        Assert.assertEquals(Result.Success(platformSession),platformSessionRepository.loadPlatformSession())

    }

}