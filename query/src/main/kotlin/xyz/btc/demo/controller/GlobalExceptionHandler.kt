package xyz.btc.demo.controller

import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.bind.support.WebExchangeBindException

@RestControllerAdvice
class GlobalExceptionHandler {

    private val logger = LoggerFactory.getLogger(GlobalExceptionHandler::class.java)

    @ExceptionHandler(Exception::class)
    fun handle(ex: Exception): ResponseEntity<ErrorInfo> {
        logger.error("Exception occurred", ex)

        return when (val rootCause = findCauseUsingPlainJava(ex)) {
            is WebExchangeBindException -> {
                ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                    .body(ErrorInfo(rootCause.allErrors.map { it.defaultMessage }.joinToString(separator = " ")))
            }

            else -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorInfo(rootCause.message ?: "Internal exception."))
        }
    }

    private fun findCauseUsingPlainJava(throwable: Throwable?): Throwable {
        var rootCause = throwable
        while (rootCause!!.cause != null && rootCause.cause !== rootCause) {
            rootCause = rootCause.cause
        }
        return rootCause
    }

}

data class ErrorInfo(
    val message: String
)