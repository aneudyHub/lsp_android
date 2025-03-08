package com.system.lsp.data.local.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.system.lsp.data.local.database.AppDatabase.Companion.LOANS_DETAILS_TABLE_NAME
import java.sql.Date


@Entity(
    tableName = LOANS_DETAILS_TABLE_NAME,
    foreignKeys = [ForeignKey(
        entity = LoanEntity::class,
        parentColumns = ["id"],
        childColumns = ["loanId"]
    )]
)
data class LoansDetailsEntity(
    @PrimaryKey(autoGenerate = false)
    var id: Long? = 0,
    @ColumnInfo(index = true)
    var loanId: Long? = 0,
    var quota: Int? = 0,
    var capital: Double? = 0.0,
    var interest: Double? = 0.0,
    var delayInterest: Double? = 0.0,
    var delayInterestPaid: Double? = 0.0,
    var dueDate: Date? = null,
    var paidDate: Date? = null,
    var paidAmount: Double? = 0.0,
    var updateAt: Date? = null,
    var isPaid: Boolean? = false,
)