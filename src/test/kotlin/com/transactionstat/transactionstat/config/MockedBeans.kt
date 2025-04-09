package com.transactionstat.transactionstat.config

import com.transactionstat.transactionstat.service.TransacaoService
import org.mockito.kotlin.mock
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class MockedBeans {

    @Bean
    fun transacaoService(): TransacaoService = mock()
}
