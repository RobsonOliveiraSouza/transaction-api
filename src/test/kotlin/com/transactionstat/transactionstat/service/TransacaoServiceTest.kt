package com.transactionstat.transactionstat.service

import com.transactionstat.transactionstat.model.TipoTransacao
import com.transactionstat.transactionstat.model.Transacao
import com.transactionstat.transactionstat.repository.TransacaoRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.mockito.kotlin.whenever
import org.springframework.data.redis.core.StringRedisTemplate
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.util.*

class TransacaoServiceTest {

    private lateinit var transacaoRepository: TransacaoRepository
    private lateinit var redisTemplate: StringRedisTemplate
    private lateinit var transacaoService: TransacaoService

    @BeforeEach
    fun setUp(){
        transacaoRepository = mock(TransacaoRepository::class.java)
        redisTemplate = mock(StringRedisTemplate::class.java)
        transacaoService = TransacaoService(transacaoRepository, redisTemplate)
    }

    @Test
    fun `should add valid transaction successfully`(){
        val transacao = Transacao(
            valor = 100.0,
            dataHora = OffsetDateTime.now(ZoneOffset.UTC).minusHours(1),
            tipo = TipoTransacao.DEPOSITO
        )

        whenever(transacaoRepository.save(any())).thenReturn(transacao)

        val resultado = transacaoService.adicionarTransacao(transacao)

        assertTrue(resultado)
        verify(transacaoRepository).save(any())
    }

    @Test
    fun `should return false for transaction successfully`(){
        val transacao = Transacao(
            valor = -50.0,
            dataHora = OffsetDateTime.now(ZoneOffset.UTC),
            tipo = TipoTransacao.SAQUE
        )

        val resultado = transacaoService.adicionarTransacao(transacao)

        assertFalse(resultado)
        verify(transacaoRepository, never()).save(any())
    }

    @Test
    fun `should delete a existent transaction`(){
        val id = UUID.randomUUID()
        val transacao = mock(Transacao::class.java)
        val optionalTransacao = Optional.of(transacao)

        whenever(transacaoRepository.findById(id)).thenReturn(optionalTransacao)

        val resultado = transacaoService.deletarTransacao(id)

        assertTrue(resultado)
        verify(transacaoRepository).delete(transacao)
    }

    @Test
    fun `should return false when try delete a non-existent transaction`(){
        val id = UUID.randomUUID()

        whenever(transacaoRepository.findById(id)).thenReturn(Optional.empty())

        val resultado = transacaoService.deletarTransacao(id)

        assertFalse(resultado)
        verify(transacaoRepository, never()).delete(any())
    }
}