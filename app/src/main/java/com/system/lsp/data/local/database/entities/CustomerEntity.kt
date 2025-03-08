package com.system.lsp.data.local.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.system.lsp.data.local.database.AppDatabase
import java.sql.Date


@Entity(tableName = AppDatabase.CUSTOMERS_TABLE_NAME)
data class CustomerEntity(
    @PrimaryKey(autoGenerate = false)
    var id: Long? = 0,
    var name: String? = "",
    var documentId: String? = "",
    var phoneNumber: String? = "",
    var pictureUrl: String? = "",
    var address: String? = "",
    var location: String? = "",
    var createdAt: Date? = null,
    var isInserted: Boolean? = false,
    var isUpdated: Boolean? = false,
    var isDeleted: Boolean? = false
)