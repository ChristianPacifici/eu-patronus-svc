package tech.pacifici.patronus.logger

import mu.KotlinLogging
import org.slf4j.MDC
import tech.pacifici.patronus.interceptor.MdcInterceptor

/**
 * Service dedicated to logging error responses.
 * Centralizes the logic for formatting and logging error details.
 */
class ErrorLogger {
    /**
     * Logs an error response with request and correlation IDs.
     *
     * @param error The error response containing status and message.
     */
    fun logError(error: ErrorResponse) {
        val requestId = MDC.get(MdcInterceptor.REQUEST_ID_MDC_KEY)
        val correlationId = MDC.get(MdcInterceptor.CORRELATION_ID_MDC_KEY)

        logger.error(
            "An error occurred for call " +
                "x-request-id $requestId, " +
                "x-correlation-id $correlationId, " +
                "status - ${error.status}, " +
                "message - ${error.errorMessage}",
        )
    }

    companion object {
        private val logger = KotlinLogging.logger {}
    }
}

/**
 * Data class representing the structure of an error response.
 *
 * @property status HTTP status code associated with the error.
 * @property errorMessage Description of the error.
 */
data class ErrorResponse(
    val status: Int,
    val errorMessage: String,
)
