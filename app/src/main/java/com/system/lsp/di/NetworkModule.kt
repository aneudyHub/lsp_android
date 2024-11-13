package com.system.lsp.di

import android.app.Application
import com.system.lsp.BuildConfig
import com.system.lsp.data.local.sharedpreferences.PlatformSessionSharedPreferences
import com.system.lsp.data.local.sharedpreferences.UserSessionSharedPreferences
import com.system.lsp.data.remote.api.ApiService
import com.system.lsp.data.remote.api.PlatformService
import com.system.lsp.data.remote.network.AuthInterceptor
import com.system.lsp.data.remote.network.LogoutCallback
import com.system.lsp.data.remote.network.ResponseInterceptor
import com.system.lsp.data.repositories.UsersRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    private const val PLATFORM_BASE_URL = BuildConfig.PLATFORM_URL


    @Singleton
    @Provides
    fun provideLogoutCallback(application:Application): LogoutCallback {
        return application as LogoutCallback
    }


    @Singleton
    @Provides
    fun provideHttpLoggingInterceptor() = HttpLoggingInterceptor()
        .apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

    @Singleton
    @Provides
    fun provideOkHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor,
        userSessionSharedPreferences: UserSessionSharedPreferences,
        logoutCallback: LogoutCallback
    ): OkHttpClient {
        return OkHttpClient
            .Builder()
            .addInterceptor(AuthInterceptor(userSessionSharedPreferences))
            .addInterceptor(ResponseInterceptor(userSessionSharedPreferences, logoutCallback))
            .addInterceptor(httpLoggingInterceptor)
            .build()
    }

    @Singleton
    @Provides
    @Named("apiService")
    fun provideRetrofit(okHttpClient: OkHttpClient, @Named("baseUrl") baseUrl: String?): Retrofit =
        Retrofit.Builder()
            .baseUrl(baseUrl ?: "")
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()

    @Singleton
    @Provides
    @Named("baseUrl")
    fun provideBaseUrl(platformSessionSharedPreferences: PlatformSessionSharedPreferences): String? {
        return if (BuildConfig.DEBUG) {
            BuildConfig.BASE_URL
        } else {
            platformSessionSharedPreferences.apiUrl
        }
//        return platformSessionSharedPreferences.apiUrl

    }

    @Provides
    @Singleton
    @Named("platformService")
    fun providePlatformRetrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl(PLATFORM_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()

    @Provides
    fun provideApiService(@Named("apiService") retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    @Provides
    fun providePlatformService(@Named("platformService") retrofit: Retrofit): PlatformService {
        return retrofit.create(PlatformService::class.java)
    }
}