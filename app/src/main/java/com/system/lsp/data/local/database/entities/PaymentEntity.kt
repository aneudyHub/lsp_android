package com.system.lsp.data.local.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.system.lsp.data.local.database.AppDatabase
import java.sql.Date

@Entity(
    tableName = AppDatabase.PAYMENTS_TABLE_NAME,
    foreignKeys = [ForeignKey(
        entity = LoanEntity::class,
        parentColumns = ["id"],
        childColumns = ["loanId"]
    )]
)
data class PaymentEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Long? = 0,
    var date: Date? = null,
    var userId: Long? = 0,
    @ColumnInfo(index = true)
    var loanId: Long? = 0,
    var isSynced: Boolean? = false
)