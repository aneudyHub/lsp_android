package com.system.lsp.data.local.database.entities

import androidx.room.Embedded
import androidx.room.Relation


data class CustomerWithLoans(
    @Embedded val customerEntity: CustomerEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "customerId"
    )
    val loans: List<LoanEntity>
)