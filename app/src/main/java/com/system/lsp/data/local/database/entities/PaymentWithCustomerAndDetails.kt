package com.system.lsp.data.local.database.entities

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation


data class PaymentWithCustomerAndPaymentDetails(
    @Embedded val payment: PaymentEntity,
    @Relation(
        parentColumn = "loanId",
        entityColumn = "id",
        associateBy = Junction(
            value = CustomerEntity::class,
            parentColumn = "customerId",
            entityColumn = "id")
    )
    val loan: LoanEntity,
    val customer: CustomerEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "paymentId"
    )
    val paymentDetailsEntity: List<PaymentDetailsEntity>
)