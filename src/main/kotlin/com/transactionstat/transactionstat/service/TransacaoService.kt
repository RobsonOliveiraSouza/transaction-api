package com.transactionstat.transactionstat.service

import com.transactionstat.transactionstat.model.Transacao
import org.springframework.stereotype.Service
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.util.concurrent.ConcurrentLinkedQueue

@Service
class TransacaoService {

    private val transacoes = ConcurrentLinkedQueue<Transacao>()

    fun adicionarTransacao(transacao: Transacao): Boolean {
        if (!validarTransacao(transacao)) {
            return false
        }
        transacoes.add(transacao)
        return true
    }

    fun limparTransacoes() {
        transacoes.clear()
    }

    private fun validarTransacao(transacao: Transacao): Boolean {
        return transacao.valor > 0 && transacao.dataHora.isBefore(OffsetDateTime.now())
    }

    fun obterEstatisticas(): Map<String, Double> {
        val agora = OffsetDateTime.now(ZoneOffset.UTC)
        val tempoLimite = agora.minusSeconds(60)

        println("Agora: $agora, Tempo Limite: $tempoLimite")
        println("Transações Armazenadas: $transacoes")

        val transacoesRecentes = transacoes.filter { it.dataHora.isAfter(tempoLimite) }

        if (transacoesRecentes.isEmpty()) {
            println("Nenhuma transação dentro dos últimos 60 segundos.")
            return mapOf(
                "count" to 0.0,
                "sum" to 0.0,
                "avg" to 0.0,
                "min" to 0.0,
                "max" to 0.0
            )
        }

        val statistics = transacoesRecentes.map { it.valor }.stream()
            .mapToDouble { it }
            .summaryStatistics()

        return mapOf(
            "count" to statistics.count.toDouble(),
            "sum" to statistics.sum.toDouble(),
            "avg" to statistics.average,
            "min" to statistics.min.toDouble(),
            "max" to statistics.max.toDouble()
        )
    }

    fun obterTodasTransacoes(): List<Transacao> {
        return transacoes.toList()
    }
}