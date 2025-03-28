package com.transactionstat.transactionstat.controller

import com.transactionstat.transactionstat.model.Transacao
import com.transactionstat.transactionstat.service.TransacaoService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/transacao")
class TransacaoController(private val transacaoService: TransacaoService) {

    @PostMapping
    fun receberTransacao(@RequestBody transacao: Transacao): ResponseEntity<Unit> {
        if (transacao.valor == null || transacao.dataHora == null) {
            return ResponseEntity(HttpStatus.BAD_REQUEST)
        }

        return if (transacaoService.adicionarTransacao(transacao)) {
            ResponseEntity(HttpStatus.CREATED)
        } else {
            ResponseEntity(HttpStatus.UNPROCESSABLE_ENTITY)
        }
    }

    @DeleteMapping
    fun limparTransacoes(): ResponseEntity<Unit> {
        transacaoService.limparTransacoes()
        return ResponseEntity(HttpStatus.OK)
    }

    // Para teste, apagar depois
    @GetMapping
    fun listarTransacoes(): ResponseEntity<List<Transacao>> {
        val transacoes = transacaoService.obterTodasTransacoes()
        return ResponseEntity(transacoes, HttpStatus.OK)
    }


    @GetMapping("/estatistica")
    fun calcularEstatisticas(): ResponseEntity<Map<String, Double>> {
        val estatisticas = transacaoService.obterEstatisticas()
        return ResponseEntity.ok(estatisticas)
    }

}