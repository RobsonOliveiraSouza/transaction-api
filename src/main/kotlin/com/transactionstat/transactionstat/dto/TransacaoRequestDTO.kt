package com.transactionstat.transactionstat.dto

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.OffsetDateTime
import com.transactionstat.transactionstat.model.TipoTransacao

data class TransacaoRequestDTO(
    val valor: Double,

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    val dataHora: OffsetDateTime,
    val tipo: TipoTransacao
)