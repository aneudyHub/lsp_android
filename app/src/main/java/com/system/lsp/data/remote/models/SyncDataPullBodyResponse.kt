package com.system.lsp.data.remote.models

import com.google.gson.annotations.SerializedName

data class SyncDataPullBodyResponse(
    @SerializedName("clientes") val customers: List<Customer>,
    @SerializedName("prestamos") val loans: List<Loan>,
    @SerializedName("prestamos_detalles") val loanQuotes: List<LoanQuote>,
    @SerializedName("estado") val state: String,
    @SerializedName("mensaje") val message: String
)

data class Customer(
    @SerializedName("id") val id: Int,
    @SerializedName("nombre_completo") val fullName: String,
    @SerializedName("documento") val document: String,
    @SerializedName("telefono") val phone: String,
    @SerializedName("celular") val cell: String,
    @SerializedName("foto") val photo: String,
    @SerializedName("lat") val latitude: String,
    @SerializedName("lng") val longitude: String,
    @SerializedName("direccion") val address: String,
    @SerializedName("updated_at") val updatedAt: String
)


data class Loan(
    @SerializedName("id") val id: Int,
    @SerializedName("clientes_id") val clientId: Int,
    @SerializedName("capital") val capital: Double,
    @SerializedName("porciento_interes") val interestPercentage: Float,
    @SerializedName("porciento_mora") val defaultInterestPercentage: Float,
    @SerializedName("plazo") val term: String,
    @SerializedName("cuotas") val quotes: Int,
    @SerializedName("fecha_registro") val registrationDate: String,
    @SerializedName("fecha_inicio") val startDate: String,
    @SerializedName("fecha_aprobado") val approvalDate: String? = null,
    @SerializedName("fecha_saldo") val balanceDate: String? = null,
    @SerializedName("rechazado") val rejected: String,
    @SerializedName("activo") val active: String,
    @SerializedName("fecha_creacion") val creationDate: String,
    @SerializedName("estado") val status: String,
    @SerializedName("saldado") val paidOff: Boolean,
    @SerializedName("plantilla") val template: String,
    @SerializedName("updated_at") val updatedAt: String,
    @SerializedName("capital_amortizable") val amortizableCapital: String,
    @SerializedName("tipo_nombre") val typeName: String
)


data class LoanQuote(
    @SerializedName("id") val id: Int,
    @SerializedName("prestamos_id") val loanId: Int,
    @SerializedName("cuota") val installmentNumber: String,
    @SerializedName("capital") val capital: Double,
    @SerializedName("interes") val interest: Double,
    @SerializedName("mora") val defaultInterest: Double,
    @SerializedName("fecha") val date: String,
    @SerializedName("dias") val days: String,
    @SerializedName("fecha_pagado") val paidDate: String? = null,
    @SerializedName("pagado") val isPaid: Boolean,
    @SerializedName("activo") val active: String,
    @SerializedName("monto_pagado") val amountPaid: Double,
    @SerializedName("abono_mora") val defaultInterestPayment: Double,
    @SerializedName("mora_acumulada") val accumulatedDefaultInterest: Double,
    @SerializedName("updated_at") val updatedAt: String
)