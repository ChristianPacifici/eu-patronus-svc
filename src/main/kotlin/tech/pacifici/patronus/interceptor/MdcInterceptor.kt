package tech.pacifici.patronus.interceptor

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.MDC
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor

/**
 * Interceptor responsible for managing the Mapped Diagnostic Context (MDC) in the application.
 * This interceptor adds request and correlation IDs from HTTP headers to the MDC for logging purposes,
 * and clears the MDC after the request is completed.
 */
@Component
class MdcInterceptor : HandlerInterceptor {
    /**
     * Companion object containing constants for header names and MDC keys.
     */
    companion object {
        /** Header name for the request ID. */
        const val REQUEST_ID_HEADER = "x-request-id"

        /** Header name for the correlation ID. */
        const val CORRELATION_ID_HEADER = "x-correlation-id"

        /** MDC key for the request ID. */
        const val REQUEST_ID_MDC_KEY = "requestId"

        /** MDC key for the correlation ID. */
        const val CORRELATION_ID_MDC_KEY = "correlationId"
    }

    /**
     * Called before the handler is executed.
     * Extracts the request ID and correlation ID from the HTTP headers and adds them to the MDC.
     *
     * @param request The current HTTP request.
     * @param response The current HTTP response.
     * @param handler The handler that will be executed.
     * @return `true` to continue processing the request, `false` to abort.
     */
    override fun preHandle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any,
    ): Boolean {
        request.getHeader(REQUEST_ID_HEADER)?.let { MDC.put(REQUEST_ID_MDC_KEY, it) }
        request.getHeader(CORRELATION_ID_HEADER)?.let { MDC.put(CORRELATION_ID_MDC_KEY, it) }
        return true
    }

    /**
     * Called after the request is completed.
     * Clears the MDC to remove the request and correlation IDs, ensuring a clean state for subsequent requests.
     *
     * @param request The current HTTP request.
     * @param response The current HTTP response.
     * @param handler The handler that was executed.
     * @param ex Any exception that occurred during request processing, or `null` if no exception occurred.
     */
    override fun afterCompletion(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any,
        ex: Exception?,
    ) {
        MDC.clear()
    }
}
