package com.system.lsp.data.local.database.entities.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.system.lsp.data.local.database.entities.PaymentDetailsEntity
import com.system.lsp.data.local.database.entities.PaymentEntity

data class PaymentWithDetails(
    @Embedded val paymentEntity: PaymentEntity? = null,
    @Relation(
        parentColumn = "id",
        entityColumn = "paymentId"
    )
    val paymentDetailsEntity: List<PaymentDetailsEntity>? = null
)