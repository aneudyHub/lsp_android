package com.system.lsp.data.remote.models

import com.google.gson.annotations.SerializedName

data class SyncDataPullBodyResponse(
    @SerializedName("clientes") val customer: ArrayList<Customer>,
    @SerializedName("prestamos") val loans: ArrayList<Loan>,
    @SerializedName("prestamos_detalles") val loanQuotes: ArrayList<LoanQuote>,
    @SerializedName("estado") val state: String,
    @SerializedName("mensaje") val message: String
)

data class Customer(
    @SerializedName("id") val id: String,
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
    @SerializedName("id") val id: String,
    @SerializedName("clientes_id") val clientId: String,
    @SerializedName("capital") val capital: String,
    @SerializedName("porciento_interes") val interestPercentage: String,
    @SerializedName("porciento_mora") val defaultInterestPercentage: String,
    @SerializedName("plazo") val term: String,
    @SerializedName("cuotas") val installments: String,
    @SerializedName("fecha_registro") val registrationDate: String,
    @SerializedName("fecha_inicio") val startDate: String,
    @SerializedName("fecha_aprobado") val approvalDate: String? = null,
    @SerializedName("fecha_saldo") val balanceDate: String? = null,
    @SerializedName("rechazado") val rejected: String,
    @SerializedName("activo") val active: String,
    @SerializedName("fecha_creacion") val creationDate: String,
    @SerializedName("estado") val status: String,
    @SerializedName("saldado") val paidOff: String,
    @SerializedName("plantilla") val template: String,
    @SerializedName("updated_at") val updatedAt: String,
    @SerializedName("capital_amortizable") val amortizableCapital: String,
    @SerializedName("tipo_nombre") val typeName: String
)


data class LoanQuote(
    @SerializedName("id") val id: String,
    @SerializedName("prestamos_id") val loanId: String,
    @SerializedName("cuota") val installmentNumber: String,
    @SerializedName("capital") val capital: String,
    @SerializedName("interes") val interest: String,
    @SerializedName("mora") val defaultInterest: String,
    @SerializedName("fecha") val date: String,
    @SerializedName("dias") val days: String,
    @SerializedName("fecha_pagado") val paidDate: String? = null,
    @SerializedName("pagado") val paid: String,
    @SerializedName("activo") val active: String,
    @SerializedName("monto_pagado") val amountPaid: String,
    @SerializedName("abono_mora") val defaultInterestPayment: String,
    @SerializedName("mora_acumulada") val accumulatedDefaultInterest: String,
    @SerializedName("updated_at") val updatedAt: String
)