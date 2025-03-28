package com.transactionstat.transactionstat

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class TransactionstatApplication

fun main(args: Array<String>) {
	runApplication<TransactionstatApplication>(*args)
}
