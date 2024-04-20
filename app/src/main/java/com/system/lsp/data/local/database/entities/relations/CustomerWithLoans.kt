package com.system.lsp.data.local.database.entities.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.system.lsp.data.local.database.entities.CustomerEntity
import com.system.lsp.data.local.database.entities.LoanEntity


data class CustomerWithLoans(
    @Embedded val customerEntity: CustomerEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "customerId"
    )
    val loans: List<LoanEntity>
)