package tech.pacifici.patronus.controller

import org.springframework.beans.TypeMismatchException
import org.springframework.dao.CannotAcquireLockException
import org.springframework.dao.DataAccessResourceFailureException
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MissingRequestHeaderException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import tech.pacifici.patronus.exception.DuplicateResourceException
import tech.pacifici.patronus.exception.InvalidOperationException
import tech.pacifici.patronus.exception.ResourceNotFoundException
import tech.pacifici.patronus.logger.ErrorLogger
import tech.pacifici.patronus.logger.ErrorResponse
import java.util.IllegalFormatException

/**
 * Global exception handler for the Patronus application.
 * This class centralizes the handling of exceptions across all @RequestMapping methods,
 * providing consistent error responses and logging.
 */
@RestControllerAdvice
class GlobalExceptionHandler(
    private val errorLogger: ErrorLogger,
) : ResponseEntityExceptionHandler() {
    /**
     * Handles exceptions of type [ResourceNotFoundException].
     *
     * @param ex The exception thrown.
     * @param request The current web request.
     * @return A [ResponseEntity] containing the error details and HTTP status NOT_FOUND.
     */
    @ExceptionHandler(ResourceNotFoundException::class)
    fun handleResourceNotFound(
        ex: ResourceNotFoundException,
        request: WebRequest,
    ): ResponseEntity<Any>? = handleGeneralError(HttpStatus.NOT_FOUND, ex.message ?: "Resource Not found")

    /**
     * Handles exceptions of type [InvalidOperationException].
     *
     * @param ex The exception thrown.
     * @param request The current web request.
     * @return A [ResponseEntity] containing the error details and HTTP status BAD_REQUEST.
     */
    @ExceptionHandler(InvalidOperationException::class)
    fun handleInvalidOperation(
        ex: InvalidOperationException,
        request: WebRequest,
    ): ResponseEntity<Any>? = handleGeneralError(HttpStatus.BAD_REQUEST, ex.message ?: "Invalid operation")

    /**
     * Handles exceptions of type [DuplicateResourceException].
     *
     * @param ex The exception thrown.
     * @param request The current web request.
     * @return A [ResponseEntity] containing the error details and HTTP status CONFLICT.
     */
    @ExceptionHandler(DuplicateResourceException::class)
    fun handleDuplicateResource(
        ex: DuplicateResourceException,
        request: WebRequest,
    ): ResponseEntity<Any>? = handleGeneralError(HttpStatus.CONFLICT, ex.message ?: "Duplicate resource")

    /**
     * Handles runtime exceptions related to bad requests, such as [IllegalArgumentException], [IllegalFormatException], and [IllegalStateException].
     *
     * @param ex The exception thrown.
     * @param request The current web request.
     * @return A [ResponseEntity] containing the error details and HTTP status BAD_REQUEST.
     */
    @ExceptionHandler(
        IllegalArgumentException::class,
        IllegalFormatException::class,
        IllegalStateException::class,
    )
    fun handleBadRequestRuntimeException(
        ex: RuntimeException,
        request: WebRequest,
    ): ResponseEntity<Any>? = handleGeneralError(HttpStatus.BAD_REQUEST, ex.message ?: "Bad Request")

    /**
     * Handles exceptions of type [MissingRequestHeaderException].
     *
     * @param ex The exception thrown.
     * @param request The current web request.
     * @return A [ResponseEntity] containing the error details and HTTP status BAD_REQUEST.
     */
    @ExceptionHandler(MissingRequestHeaderException::class)
    fun handleBadRequestException(
        ex: Exception,
        request: WebRequest,
    ): ResponseEntity<Any>? = handleGeneralError(HttpStatus.BAD_REQUEST, ex.message ?: "Bad Request")

    /**
     * Handles data access exceptions such as [EmptyResultDataAccessException], [DataAccessResourceFailureException],
     * [CannotAcquireLockException], and [DataIntegrityViolationException].
     *
     * @param ex The exception thrown.
     * @param request The current web request.
     * @return A [ResponseEntity] containing the error details and an appropriate HTTP status based on the exception type.
     */
    @ExceptionHandler(
        EmptyResultDataAccessException::class,
        DataAccessResourceFailureException::class,
        CannotAcquireLockException::class,
        DataIntegrityViolationException::class,
    )
    fun handleDataAccessExceptions(
        ex: Exception,
        request: WebRequest,
    ): ResponseEntity<Any>? {
        val status =
            when (ex) {
                is EmptyResultDataAccessException -> HttpStatus.NOT_FOUND
                is DataAccessResourceFailureException -> HttpStatus.SERVICE_UNAVAILABLE
                is CannotAcquireLockException -> HttpStatus.LOCKED
                is DataIntegrityViolationException -> HttpStatus.CONFLICT
                else -> HttpStatus.INTERNAL_SERVER_ERROR
            }
        val errorResponse = ErrorResponse(status.value(), ex.message ?: "Database error")
        errorLogger.logError(errorResponse)
        return ResponseEntity(errorResponse, status)
    }

    /**
     * Overrides the default handling of [HttpMessageNotReadableException] to provide a consistent error response.
     *
     * @param ex The exception thrown.
     * @param headers HTTP headers.
     * @param status HTTP status code.
     * @param request The current web request.
     * @return A [ResponseEntity] containing the error details and HTTP status BAD_REQUEST.
     */
    override fun handleHttpMessageNotReadable(
        ex: HttpMessageNotReadableException,
        headers: HttpHeaders,
        status: HttpStatusCode,
        request: WebRequest,
    ): ResponseEntity<Any>? = handleGeneralError(HttpStatus.BAD_REQUEST, ex.message ?: "Bad Request")

    /**
     * Overrides the default handling of [TypeMismatchException] to provide a consistent error response.
     *
     * @param ex The exception thrown.
     * @param headers HTTP headers.
     * @param status HTTP status code.
     * @param request The current web request.
     * @return A [ResponseEntity] containing the error details and HTTP status BAD_REQUEST.
     */
    override fun handleTypeMismatch(
        ex: TypeMismatchException,
        headers: HttpHeaders,
        status: HttpStatusCode,
        request: WebRequest,
    ): ResponseEntity<Any>? = handleGeneralError(HttpStatus.BAD_REQUEST, ex.message ?: "Bad Request")

    /**
     * Helper method to create a consistent error response.
     *
     * @param status HTTP status code.
     * @param message Error message.
     * @return A [ResponseEntity] containing the error details and the specified HTTP status.
     */
    private fun handleGeneralError(
        status: HttpStatus,
        message: String,
    ): ResponseEntity<Any>? {
        val errorResponse = ErrorResponse(status.value(), message)
        errorLogger.logError(errorResponse)
        return ResponseEntity(errorResponse, status)
    }
}
