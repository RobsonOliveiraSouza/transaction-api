package com.transactionstat.transactionstat.common

data class ResultadoValidacao(
    val sucesso: Boolean,
    val mensagem: String? = null,
)
