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
    var id: Long? = 0,
    @ColumnInfo(index = true)
    var paymentId: Long? = 0,
    @ColumnInfo(index = true)
    var quoteId: Long? = null,
    var capital: Double? = 0.00,
    var interest: Double? = 0.00,
    var delayInterest: Double? = 0.00
)