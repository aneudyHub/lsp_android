package com.system.lsp.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.system.lsp.data.local.database.AppDatabase.Companion.DATABASE_VERSION

@Database(version = DATABASE_VERSION, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    companion object {
        const val DATABASE_NAME = "lsp_local_db"
        const val DATABASE_VERSION = 1
    }
}