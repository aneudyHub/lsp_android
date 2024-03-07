package com.system.lsp.data.local.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.system.lsp.data.local.database.AppDatabase

@Entity(
    tableName = AppDatabase.PAYMENTS_DETAILS_TABLE_NAME, foreignKeys = [
        ForeignKey(
            entity = PaymentEntity::class,
            parentColumns = ["id"],
            childColumns = ["paymentId"]
        ),
        ForeignKey(
            entity = LoansDetailsEntity::class,
            parentColumns = ["id"],
            childColumns = ["quoteId"]
        ),
    ]
)
data class PaymentDetailsEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long? = 0,
    @ColumnInfo(index = true)
    val paymentId: Long? = 0,
    @ColumnInfo(index = true)
    val quoteId: Long? = null,
    val capital: Double? = 0.00,
    val interest: Double? = 0.00,
    val delayInterest: Double? = 0.00
)