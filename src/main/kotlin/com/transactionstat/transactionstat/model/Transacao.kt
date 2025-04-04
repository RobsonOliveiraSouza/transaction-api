package com.transactionstat.transactionstat.model

import jakarta.persistence.*
import java.time.OffsetDateTime
import java.util.UUID

@Entity
@Table(name = "transacoes")
data class Transacao(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "uuid")
    var id: UUID? = null,

    @Column(nullable = false)
    var valor: Double = 0.0,

    @Column(name = "data_hora", nullable = false)
    var dataHora: OffsetDateTime = OffsetDateTime.now(),

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var tipo: TipoTransacao = TipoTransacao.DEPOSITO
) {
    constructor() : this(null, 0.0, OffsetDateTime.now(), TipoTransacao.DEPOSITO)
}


enum class TipoTransacao {
    DEPOSITO, SAQUE, TRANSFERENCIA, PAGAMENTO, CREDITO
}