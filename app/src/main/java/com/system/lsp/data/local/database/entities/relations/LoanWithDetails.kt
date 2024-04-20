package com.system.lsp.data.local.database.entities.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.system.lsp.data.local.database.entities.LoanEntity
import com.system.lsp.data.local.database.entities.LoansDetailsEntity


data class LoanWithDetails(
    @Embedded val loan : LoanEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "loanId"
    )
    val details: List<LoansDetailsEntity>
)