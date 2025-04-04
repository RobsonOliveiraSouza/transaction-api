package com.transactionstat.transactionstat.dto

import java.time.OffsetDateTime
import com.transactionstat.transactionstat.model.TipoTransacao

data class TransacaoRequestDTO(
    val valor: Double,
    val dataHora: OffsetDateTime,
    val tipo: TipoTransacao
)
