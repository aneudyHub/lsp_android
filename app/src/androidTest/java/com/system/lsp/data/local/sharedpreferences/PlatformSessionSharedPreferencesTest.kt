package com.system.lsp.data.local.sharedpreferences

import com.system.lsp.data.local.models.PlatformSession
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
class PlatformSessionSharedPreferencesTest {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var platformSessionSharedPreferences: PlatformSessionSharedPreferences

    private val mockUrl = "https://api.central.com"

    private val mockPlatformSession = PlatformSession(
        hashCodeAuthenticator = "0000-0000",
        companyName = "Company",
        companyAddress = "San francisco,CA",
        companyPhone = "000-000-0000",
        apiUrl = mockUrl
    )


    @Before
    fun setup() {
        hiltRule.inject()
        platformSessionSharedPreferences.dropSession()
    }

    @Test
    fun shouldSaveAndRetrieveApiUrl(){
        platformSessionSharedPreferences.apiUrl = mockUrl
        Assert.assertEquals(mockUrl,platformSessionSharedPreferences.apiUrl)

        platformSessionSharedPreferences.dropSession()
        Assert.assertEquals(null,platformSessionSharedPreferences.apiUrl)
    }

    @Test
    fun shouldNotRetrieveSessionWithNullApiUrl(){
        Assert.assertEquals(null,platformSessionSharedPreferences.apiUrl)
        platformSessionSharedPreferences.saveSession(mockPlatformSession)
        Assert.assertEquals(null,platformSessionSharedPreferences.retrieveSession())
    }

    @Test
    fun shouldRetrieveSessionSaved(){
        Assert.assertNull(platformSessionSharedPreferences.retrieveSession())
        Assert.assertNull(platformSessionSharedPreferences.apiUrl)

        platformSessionSharedPreferences.apiUrl = mockUrl
        platformSessionSharedPreferences.saveSession(mockPlatformSession)
        Assert.assertEquals(mockPlatformSession,platformSessionSharedPreferences.retrieveSession())

        platformSessionSharedPreferences.dropSession()
        Assert.assertNull(platformSessionSharedPreferences.retrieveSession())
        Assert.assertNull(platformSessionSharedPreferences.apiUrl)
    }

    @Test
    fun shouldDropSessionAndClearAllData(){
        platformSessionSharedPreferences.apiUrl = mockUrl
        platformSessionSharedPreferences.saveSession(mockPlatformSession)
        Assert.assertEquals(mockPlatformSession,platformSessionSharedPreferences.retrieveSession())
        Assert.assertEquals(mockUrl,platformSessionSharedPreferences.apiUrl)

        platformSessionSharedPreferences.dropSession()
        Assert.assertNull(platformSessionSharedPreferences.retrieveSession())
        Assert.assertNull(platformSessionSharedPreferences.apiUrl)
    }
}