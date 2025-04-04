package com.transactionstat.transactionstat.exception

import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.ErrorResponse
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import java.time.format.DateTimeParseException

@ControllerAdvice
class GlobalExceptionHandler {

    companion object {
        private const val ERROR_KEY = "error"
    }

    private val logger = LoggerFactory.getLogger(GlobalExceptionHandler::class.java)

    fun handleValidationException(ex: MethodArgumentNotValidException): ResponseEntity<Map<String, String>> {
        logger.error("Validation failed: {}", ex.message)
        val errors = ex.bindingResult.fieldErrors.associate { it.field to (it.defaultMessage ?: "Invalid") }
        return ResponseEntity.badRequest().body(errors)
    }

    @ExceptionHandler(DateTimeParseException::class)
    fun handleDateTimeParseException(ex: DateTimeParseException): ResponseEntity<Map<String, String>> {
        logger.error("Invalid date/time format: {}", ex.message)
        val error = mapOf(ERROR_KEY to "Invalid date/time format. Use ISO 8601 format.")
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error)
    }

    @ExceptionHandler(Exception::class)
    fun handleGenericException(ex: Exception): ResponseEntity<Map<String, String>> {
        logger.error("Unexpected error occurred: {}", ex.message, ex)
        val error = mapOf(ERROR_KEY to (ex.message ?: "Unexpected error"))
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error)
    }
}