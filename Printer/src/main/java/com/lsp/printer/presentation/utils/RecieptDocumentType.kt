package com.lsp.printer.presentation.utils

enum class RecieptDocumentType {
    ORIGINAL_DOCUMENT{
        override fun toString(): String = "*RECIBO DE PAGO*"

    },
    COPY_DOCUMENT {
        override fun toString(): String = "*COPIA RECIBO DE PAGO*"
    };

    abstract override fun toString(): String
}