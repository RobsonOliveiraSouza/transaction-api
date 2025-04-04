package com.transactionstat.transactionstat

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching


@SpringBootApplication
@EnableCaching
class TransactionstatApplication

fun main(args: Array<String>) {
	runApplication<TransactionstatApplication>(*args)
}
