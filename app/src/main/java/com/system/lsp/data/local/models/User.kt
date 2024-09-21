package com.system.lsp.data.local.models

data class User(
    val id: Long,
    var firstName: String?,
    var lastName: String?,
    var email: String?,
    var phone: String?
)