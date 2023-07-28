package com.system.lsp.data.local.sharedpreferences

import com.system.lsp.data.local.models.User
import com.system.lsp.di.AppModule
import com.system.lsp.di.RepositoriesModule
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import javax.inject.Inject


@HiltAndroidTest
@RunWith(JUnit4::class)
@UninstallModules(AppModule::class, RepositoriesModule::class)
class UserSessionSharedPreferencesTest {


    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var userSessionSharedPreferences: UserSessionSharedPreferences

    private val mockToken = "ABC123"

    private val mockUser = User(
        id = 1,
        firstName = "Juan",
        lastName = "Perez",
        email = "example@example.com",
        "8093962555"
    )

    @Before
    fun setup() {
        hiltRule.inject()
        userSessionSharedPreferences.dropSession()
    }

    @Test
    fun shouldSaveToken() {
        userSessionSharedPreferences.token = mockToken
        Assert.assertEquals(mockToken, userSessionSharedPreferences.token)
    }

    @Test
    fun shouldSaveAUserAndRetrieveUser() {
        Assert.assertEquals(null, userSessionSharedPreferences.retrieveUser())
        userSessionSharedPreferences.saveUser(mockUser)
        Assert.assertEquals(null, userSessionSharedPreferences.retrieveUser())
        userSessionSharedPreferences.token = mockToken
        Assert.assertEquals(mockUser, userSessionSharedPreferences.retrieveUser())

    }

    @Test
    fun shouldDropSession() {
        userSessionSharedPreferences.token = mockToken
        userSessionSharedPreferences.saveUser(mockUser)
        val savedUser = userSessionSharedPreferences.retrieveUser()
        Assert.assertEquals(mockUser, savedUser)
        userSessionSharedPreferences.dropSession()
        Assert.assertEquals(null, userSessionSharedPreferences.retrieveUser())
        Assert.assertEquals(null, userSessionSharedPreferences.token)
    }
}