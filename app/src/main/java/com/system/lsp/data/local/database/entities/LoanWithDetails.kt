package com.system.lsp.data.local.database.entities

import androidx.room.Embedded
import androidx.room.Relation


data class LoanWithDetails(
    @Embedded val loan : LoanEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "loanId"
    )
    val details: List<LoansDetailsEntity>
)