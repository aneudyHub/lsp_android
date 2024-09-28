package com.system.lsp.di

import com.system.lsp.data.repositories.PaymentsRepository
import com.system.lsp.data.repositories.SyncDataRepository
import com.system.lsp.data.repositories.UsersRepository
//import com.system.lsp.sync.DataSyncWorkerFactory
import com.system.lsp.sync.RemoteSyncHandler
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class SyncModule {
//    @Binds
//    abstract fun bindWorkerFactory(factory: DataSyncWorkerFactory): DataSyncWorkerFactory
}