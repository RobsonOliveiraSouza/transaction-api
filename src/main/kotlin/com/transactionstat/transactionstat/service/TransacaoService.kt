package com.transactionstat.transactionstat.service

import com.transactionstat.transactionstat.model.Transacao
import org.springframework.stereotype.Service
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.util.concurrent.ConcurrentLinkedQueue

@Service
class TransacaoService {

    private val BRAZIL_OFFSET = ZoneOffset.of("-03:00")
    private val transacoes = ConcurrentLinkedQueue<Transacao>()

    fun adicionarTransacao(transacao: Transacao): Boolean {
        // Converte para horário do Brasil, independente do offset recebido
        val transacaoBrazil = transacao.dataHora
            .withOffsetSameInstant(BRAZIL_OFFSET)
            .let { transacao.copy(dataHora = it) }

        if (!validarTransacao(transacaoBrazil)) {
            return false
        }

        transacoes.add(transacaoBrazil)
        println("Transação convertida para Brazil/East: $transacaoBrazil")
        return true
    }

    fun limparTransacoes() {
        transacoes.clear()
    }

    private fun validarTransacao(transacao: Transacao): Boolean {
        val agora = OffsetDateTime.now(BRAZIL_OFFSET)
        return transacao.valor > 0 && transacao.dataHora.isBefore(agora)
    }

    fun obterEstatisticas(): Map<String, Double> {
        val agora = OffsetDateTime.now(BRAZIL_OFFSET)
        val tempoLimite = agora.minusSeconds(60)

        println("Agora (Brazil/East): $agora, Tempo Limite: $tempoLimite")
        println("Transações Armazenadas: $transacoes")

        val transacoesRecentes = transacoes.filter {
            it.dataHora.isAfter(tempoLimite)
        }

        if (transacoesRecentes.isEmpty()) {
            println("Nenhuma transação dentro dos últimos 60 segundos.")
            return emptyMap()
        }

        val statistics = transacoesRecentes.map { it.valor }
            .stream()
            .mapToDouble { it }
            .summaryStatistics()

        return mapOf(
            "count" to statistics.count.toDouble(),
            "sum" to statistics.sum,
            "avg" to statistics.average,
            "min" to statistics.min,
            "max" to statistics.max
        )
    }

    fun obterTodasTransacoes(): List<Transacao> {
        return transacoes.toList()
    }
}