package com.transactionstat.transactionstat.dto

import com.transactionstat.transactionstat.model.TipoTransacao
import java.time.OffsetDateTime
import java.util.UUID

data class TransacaoResponseDTO(
    val id: UUID?,
    val valor: Double,
    val dataHora: OffsetDateTime,
    val tipo: TipoTransacao
)

