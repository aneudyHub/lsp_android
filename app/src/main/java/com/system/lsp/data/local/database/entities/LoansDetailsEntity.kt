package com.system.lsp.data.local.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.system.lsp.data.local.database.AppDatabase.Companion.LOANS_DETAILS_TABLE_NAME
import java.util.Date


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
    val id: Long? = 0,
    @ColumnInfo(index = true)
    val loanId: Long? = 0,
    val quota: Int? = 0,
    val capital: Double? = 0.0,
    val interest: Double? = 0.0,
    val delayInterest: Double? = 0.0,
    val dueDate: Date? = null,
    val paidDate: Date? = null,
    val paidAmount: Double? = 0.0,
    val updateAt: Date? = null
)