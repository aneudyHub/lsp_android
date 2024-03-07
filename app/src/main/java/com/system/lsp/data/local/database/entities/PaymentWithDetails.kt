package com.system.lsp.data.local.database.entities

import androidx.room.Embedded
import androidx.room.Relation

data class PaymentWithDetails(
    @Embedded val paymentEntity: PaymentEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "paymentId"
    )
    val paymentDetailsEntity: PaymentDetailsEntity
)