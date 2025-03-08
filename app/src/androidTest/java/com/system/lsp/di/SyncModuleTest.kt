package com.system.lsp.di

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object SyncModuleTest {
    //    @Binds
//    abstract fun bindWorkerFactory(factory: DataSyncWorkerFactory): DataSyncWorkerFactory
}