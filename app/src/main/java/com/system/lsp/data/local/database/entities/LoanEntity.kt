package com.system.lsp.data.local.database.entities

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.system.lsp.data.local.database.AppDatabase
import java.sql.Date

@Entity(
    tableName = AppDatabase.LOANS_TABLE_NAME,
//    foreignKeys = [ForeignKey(
//        entity = CustomerEntity::class,
//        parentColumns = ["id"],
//        childColumns = ["customerId"]
//    )]
)
data class LoanEntity(
    @PrimaryKey(autoGenerate = false)
    var id: Long? = 0,
    @ColumnInfo(index = true)
    var customerId: Long? = 0,
    var capitalAmount: Double? = 0.0,
    var interestPercentage: Float? = 0f,
    var delayInterestPercentage: Float? = 0f,
    var termType: String? = "",
    var quotes: Int? = 0,
    var startDate: Date? = null,
    var createdDate: Date? = null,
    var updatedAt: Date? = null,
    var isPaid: Boolean? = false,
    var endDate: Date? = null,
    var createdBy: String? = "",
    var isInserted: Boolean? = false,
    var isUpdated: Boolean? = false,
    var isDeleted: Boolean? = false
)