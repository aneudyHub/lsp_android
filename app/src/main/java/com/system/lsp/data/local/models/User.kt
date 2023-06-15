package com.system.lsp.data.local.models

data class User(
    val id: Int,
    var firstName: String?,
    var lastName: String?,
    var email: String?,
    var phone: String?
)