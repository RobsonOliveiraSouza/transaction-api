package com.transactionstat.transactionstat.controller

import com.transactionstat.transactionstat.dto.TransacaoRequestDTO
import com.transactionstat.transactionstat.dto.TransacaoResponseDTO
import com.transactionstat.transactionstat.model.Transacao
import com.transactionstat.transactionstat.model.TipoTransacao
import com.transactionstat.transactionstat.service.TransacaoService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.Instant
import java.time.ZoneId
import java.util.*

@RestController
@RequestMapping("/transacao")
class TransacaoController(private val transacaoService: TransacaoService) {

    @PostMapping
    fun receberTransacao(@RequestBody transacaoDTO: TransacaoRequestDTO): ResponseEntity<Any> {
        val transacao = Transacao(
            valor = transacaoDTO.valor,
            dataHora = transacaoDTO.dataHora,
            tipo = transacaoDTO.tipo
        )
        val resultado = transacaoService.adicionarTransacao(transacao)
        println("📥 Transação recebida: ${transacao.tipo} - $transacao, adicionada? ${resultado}")

        return if (resultado.sucesso) {
            ResponseEntity.status(HttpStatus.CREATED).build()
        } else {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                mapOf("erro" to resultado.mensagem)
            )
        }
    }


    @DeleteMapping("/{id}")
    fun deletarTransacao(@PathVariable id: UUID): ResponseEntity<Unit> {
        return if (transacaoService.deletarTransacao(id)) {
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.notFound().build()
        }
    }


    @GetMapping("/estatistica")
    fun calcularEstatisticas(): ResponseEntity<Map<TipoTransacao, Map<String, Double>>> {
        val estatisticas = transacaoService.obterEstatisticas()
            .mapKeys { TipoTransacao.valueOf(it.key) }

        return if (estatisticas.isNotEmpty()) {
            ResponseEntity.ok(estatisticas)
        } else {
            ResponseEntity.noContent().build()
        }
    }

    @GetMapping
    fun listarTransacoes(@RequestParam(required = false, defaultValue = "UTC") timezone: String): ResponseEntity<List<TransacaoResponseDTO>> {
        val zoneId = ZoneId.of(timezone)
        val offset = zoneId.rules.getOffset(Instant.now())
        val transacoes = transacaoService.obterTransacoesPorFusoHorario(offset)
        val response = transacoes.map { transacao ->
            TransacaoResponseDTO(
                id = transacao.id,
                valor = transacao.valor,
                dataHora = transacao.dataHora,
                tipo = transacao.tipo
            )
        }
        return ResponseEntity.ok(response)
    }
}
