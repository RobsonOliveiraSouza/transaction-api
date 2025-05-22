package com.transactionstat.transactionstat.dto

import com.fasterxml.jackson.annotation.JsonFormat
import com.transactionstat.transactionstat.model.TipoTransacao
import java.time.OffsetDateTime
import java.util.UUID

data class TransacaoResponseDTO(
    val id: UUID?,
    val valor: Double,

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    val dataHora: OffsetDateTime,
    val tipo: TipoTransacao
)