package com.system.lsp.data.repositories.models

data class Customer(
    val id: Long? = 0,
    val name: String? = "",
    val lastName: String? = "",
    val documentId: String? = "",
    val phoneNumber: String? = "",
    val pictureUrl: String? = "",
    val address: String? = "",
    val location: String? = "",
)
