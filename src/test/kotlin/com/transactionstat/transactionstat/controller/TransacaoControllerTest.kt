package com.transactionstat.transactionstat.controller

import com.transactionstat.transactionstat.service.TransacaoService
import com.transactionstat.transactionstat.config.MockedBeans
import com.transactionstat.transactionstat.dto.TransacaoRequestDTO
import com.transactionstat.transactionstat.model.TipoTransacao

import com.fasterxml.jackson.databind.ObjectMapper
import com.transactionstat.transactionstat.common.ResultadoValidacao
import com.transactionstat.transactionstat.model.Transacao

import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.mockito.kotlin.any

import org.springframework.http.MediaType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.OffsetDateTime
import java.util.UUID

@WebMvcTest(TransacaoController::class)
@Import(MockedBeans::class)
class TransacaoControllerTest {

    @Autowired
    private lateinit var transacaoService: TransacaoService

    @Autowired
    private lateinit var mockMvc: MockMvc

    private val objectMapper = ObjectMapper().findAndRegisterModules()

    @Test
    fun `should create a transaction and return 201`() {
        val dto = TransacaoRequestDTO(
            valor = 100.0,
            dataHora = OffsetDateTime.now(),
            tipo = TipoTransacao.DEPOSITO
        )

        `when`(transacaoService.adicionarTransacao(any())).thenReturn(ResultadoValidacao(sucesso = true))

        mockMvc.perform(
            post("/transacao")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
        ).andExpect(status().isCreated)
    }

    @Test
    fun `should delete a existent transaction and return 204`(){
        val id = UUID.randomUUID()

        `when`(transacaoService.deletarTransacao(id)).thenReturn(true)

        mockMvc.perform(delete("/transacao/$id"))
            .andExpect(status().isNoContent)
    }

    @Test
    fun `should return 404 when trying to delete a non-existent transaction`() {
        val id = UUID.randomUUID()

        `when`(transacaoService.deletarTransacao(id)).thenReturn(false)

        mockMvc.perform(delete("/transacao/$id"))
            .andExpect(status().isNotFound)
    }

    @Test
    fun `should return a list of transactions`(){
        val transacao = Transacao(
            id = UUID.randomUUID(),
            valor = 150.0,
            dataHora = OffsetDateTime.now(),
            tipo = TipoTransacao.SAQUE
        )

        `when`(transacaoService.obterTransacoesPorFusoHorario(any())).thenReturn(emptyList())

        mockMvc.perform(get("/transacao/estatistica"))
            .andExpect(status().isNoContent)
    }
}