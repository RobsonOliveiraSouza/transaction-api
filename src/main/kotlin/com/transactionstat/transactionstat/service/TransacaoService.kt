package com.transactionstat.transactionstat.service

import com.transactionstat.transactionstat.common.ResultadoValidacao
import com.transactionstat.transactionstat.model.Transacao
import com.transactionstat.transactionstat.model.TipoTransacao
import com.transactionstat.transactionstat.repository.TransacaoRepository
import org.springframework.cache.annotation.Cacheable
import org.springframework.cache.annotation.CacheEvict
import org.springframework.stereotype.Service
import java.time.OffsetDateTime
import java.time.ZoneOffset
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class TransacaoService(
    private val transacaoRepository: TransacaoRepository,
    private val redisTemplate: StringRedisTemplate
) {
    companion object {
        private val UTC_OFFSET = ZoneOffset.UTC
    }

    @CacheEvict("estatisticas", allEntries = true)
    @Transactional
    fun adicionarTransacao(transacao: Transacao): ResultadoValidacao {
        return try {
            val transacaoUTC = transacao.copy(dataHora = transacao.dataHora.withOffsetSameInstant(UTC_OFFSET))

            val validacao = validarTransacao(transacaoUTC)
            if (!validacao.sucesso) return validacao

            transacaoRepository.save(transacaoUTC)
            atualizarCache()
            println("✅ Transação registrada no banco: ${transacaoUTC.tipo} - $transacaoUTC")
            ResultadoValidacao(true)
        } catch (e: Exception) {
            println("❌ Erro ao salvar a transação: ${e.message}")
            ResultadoValidacao(false, "Erro interno: ${e.message}")
        }
    }


    private fun validarTransacao(transacao: Transacao): ResultadoValidacao {
        val agoraUTC = OffsetDateTime.now(UTC_OFFSET)

        return when {
            transacao.valor <= 0 -> {
                println("⚠️ Erro: Transação com valor inválido (${transacao.valor})!")
                ResultadoValidacao(false, "Valor inválido: deve ser maior que zero.")
            }
            transacao.dataHora.isAfter(agoraUTC) -> {
                println("⚠️ Erro: Transação com data futura (${transacao.dataHora})!")
                ResultadoValidacao(false, "Data inválida: não pode ser no futuro.")
            }
            transacao.tipo !in TipoTransacao.entries -> {
                println("⚠️ Erro: Tipo de transação inválido (${transacao.tipo})!")
                ResultadoValidacao(false, "Tipo inválido: ${transacao.tipo}")
            }
            else -> ResultadoValidacao(true)
        }
    }

    @CacheEvict("estatisticas", allEntries = true)
    @Transactional
    fun deletarTransacao(id: UUID): Boolean {
        val transacao = transacaoRepository.findById(id)
        return if (transacao.isPresent) {
            transacaoRepository.delete(transacao.get())
            redisTemplate.delete("estatisticas")
            println("🗑️ Transação com ID $id removida.")
            true
        } else {
            false
        }
    }

    @Cacheable("estatisticas")
    fun obterEstatisticas(): Map<String, Map<String, Double>> {
        val agoraUTC = OffsetDateTime.now(UTC_OFFSET)
        val inicioDoDiaUTC = agoraUTC.toLocalDate().atStartOfDay().atOffset(UTC_OFFSET)

        println("📊 Buscando transações entre $inicioDoDiaUTC e $agoraUTC...")
        val transacoesDoDia = transacaoRepository.findByDataHoraBetween(inicioDoDiaUTC, agoraUTC)

        if (transacoesDoDia.isEmpty()) {
            println("⚠️ Nenhuma transação encontrada para hoje.")
            return emptyMap()
        }

        return transacoesDoDia.groupBy { it.tipo.name }
            .mapValues { (_, transacoes) ->
                val statistics = transacoes.map { it.valor }
                    .let { valores ->
                        DoubleSummaryStatistics().apply {
                            valores.forEach { accept(it) }
                        }
                    }

                fun format(value: Double): Double = String.format("%.2f", value).toDouble()

                mapOf(
                    "count" to format(statistics.count.toDouble()),
                    "sum" to format(statistics.sum),
                    "avg" to format(statistics.average),
                    "min" to format(statistics.min),
                    "max" to format(statistics.max)
                )
            }
    }

    fun obterTransacoesPorFusoHorario(timezone: ZoneOffset): List<Transacao> {
        return transacaoRepository.findAll().map {
            it.copy(dataHora = it.dataHora.withOffsetSameInstant(timezone))
        }
    }

    private fun atualizarCache() {
        redisTemplate.delete("estatisticas")
    }
}
