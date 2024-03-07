package com.system.lsp.data.local.database.entities

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.system.lsp.data.local.database.AppDatabase
import java.util.Date

@Entity(
    tableName = AppDatabase.LOANS_TABLE_NAME,
    foreignKeys = [ForeignKey(
        entity = CustomerEntity::class,
        parentColumns = ["id"],
        childColumns = ["customerId"]
    )]
)
data class LoanEntity(
    @PrimaryKey(autoGenerate = false)
    val id: Long? = 0,
    @ColumnInfo(index = true)
    val customerId: Long? = 0,
    val capitalAmount: Double? = 0.0,
    val interestPercentage: Float? = 0f,
    val delayInterestPercentage: Float? = 0f,
    val termType: String? = "",
    val quotes: Int? = 0,
    val startDate: Date? = Date(),
    val createdDate: Date? = Date(),
    val updatedAt: Date? = Date(),
    val isPaid: Boolean? = false,
    val endDate: Date? = Date(),
    val createdBy: String? = "",
    val isInserted: Boolean? = false,
    val isUpdated: Boolean? = false,
    val isDeleted: Boolean? = false
)