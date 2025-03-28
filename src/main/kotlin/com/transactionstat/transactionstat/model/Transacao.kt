package com.transactionstat.transactionstat.model

import java.time.OffsetDateTime

data class Transacao(
    val valor: Double,
    val dataHora: OffsetDateTime,
)
