package com.system.lsp.di

//import com.system.lsp.sync.DataSyncWorkerFactory
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class SyncModule {
//    @Binds
//    abstract fun bindWorkerFactory(factory: DataSyncWorkerFactory): DataSyncWorkerFactory
}