package com.lsp.printer.data.models

import java.util.Date

data class Printer(
    val id: Int,
    val customName: String = "",
    val macAddress: String
)