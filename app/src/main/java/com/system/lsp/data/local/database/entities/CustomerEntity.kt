package com.system.lsp.data.local.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.system.lsp.data.local.database.AppDatabase
import java.util.Date


@Entity(tableName = AppDatabase.CUSTOMERS_TABLE_NAME)
data class CustomerEntity(
    @PrimaryKey(autoGenerate = false)
    val id: Long? = 0,
    val name: String? = "",
    val lastName: String? = "",
    val documentId: String? = "",
    val phoneNumber: String? = "",
    val pictureUrl: String? = "",
    val address: String? = "",
    val location: String? = "",
    val createdAt: Date? = null,
    val isInserted: Boolean? = false,
    val isUpdated: Boolean? = false,
    val isDeleted: Boolean? = false
)