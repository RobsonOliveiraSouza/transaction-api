package com.transactionstat.transactionstat.config

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OpenApiConfig {

    @Bean
    fun customOpenAPI(): OpenAPI = OpenAPI()
        .info(
            Info()
                .title("TransactionStat API")
                .version("1.0")
                .description("API REST para gerenciar transações financeiras e calcular estatísticas.")
        )
}
