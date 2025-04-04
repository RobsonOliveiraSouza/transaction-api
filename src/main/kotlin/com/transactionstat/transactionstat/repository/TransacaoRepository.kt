package com.transactionstat.transactionstat.repository

import com.transactionstat.transactionstat.model.Transacao
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.OffsetDateTime
import java.util.*

interface TransacaoRepository : JpaRepository<Transacao, UUID> {
    fun findByDataHoraBetween(inicio: OffsetDateTime, fim: OffsetDateTime): List<Transacao>
}