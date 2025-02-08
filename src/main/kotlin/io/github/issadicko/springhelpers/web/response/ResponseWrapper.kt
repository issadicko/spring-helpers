package io.github.issadicko.springhelpers.web.response

import org.springframework.http.HttpStatus
import java.time.Instant

/**
 * A generic wrapper class for HTTP responses that provides a consistent response structure
 * across the application. It includes status, messages, timestamps, and the actual payload.
 *
 * @param T The type of data being wrapped
 */
data class ResponseWrapper<T> private constructor(
    var successful: Boolean = false,
    var message: String? = null,
    var technicalMessage: String? = null,
    var data: T? = null,
    var code: Int = 0,
    var requestId: String? = null,
    var timestamp: Instant = Instant.now(),
    var path: String? = null
) {
    /**
     * Returns the wrapped data if present, otherwise throws an exception
     * @throws IllegalStateException if no data is present
     */
    fun getDataOrThrow(): T = data ?: throw IllegalStateException("No data present in response")

    /**
     * Returns the wrapped data if present, otherwise returns the provided default value
     * @param defaultValue the default value to return if no data is present
     */
    fun getDataOrDefault(defaultValue: T): T = data ?: defaultValue

    /**
     * Checks if the response contains data
     */
    fun hasData(): Boolean = data != null

    /**
     * Builder class for constructing ResponseWrapper instances
     */
    class Builder<T> {
        private var successful: Boolean = false
        private var message: String? = null
        private var technicalMessage: String? = null
        private var data: T? = null
        private var code: Int = 0
        private var requestId: String? = null
        private var path: String? = null
        private var timestamp: Instant = Instant.now()

        fun successful(successful: Boolean) = apply { this.successful = successful }
        fun message(message: String?) = apply { this.message = message }
        fun technicalMessage(technicalMessage: String?) = apply { this.technicalMessage = technicalMessage }
        fun data(data: T?) = apply { this.data = data }
        fun code(code: Int) = apply { this.code = code }
        fun requestId(requestId: String?) = apply { this.requestId = requestId }
        fun path(path: String?) = apply { this.path = path }

        fun build() = ResponseWrapper(
            successful = successful,
            message = message,
            technicalMessage = technicalMessage,
            data = data,
            code = code,
            requestId = requestId,
            timestamp = timestamp,
            path = path
        )
    }

    companion object {
        @JvmStatic
        fun <T> success(data: T): ResponseWrapper<T> = Builder<T>()
            .successful(true)
            .message("Operation successful")
            .code(HttpStatus.OK.value())
            .data(data)
            .build()

        @JvmStatic
        fun <T> success(message: String, data: T): ResponseWrapper<T> = Builder<T>()
            .successful(true)
            .message(message)
            .code(HttpStatus.OK.value())
            .data(data)
            .build()

        @JvmStatic
        fun <T> error(message: String, status: HttpStatus): ResponseWrapper<T> = Builder<T>()
            .successful(false)
            .message(message)
            .code(status.value())
            .build()

        @JvmStatic
        fun <T> error(message: String, technicalMessage: String, status: HttpStatus): ResponseWrapper<T> = Builder<T>()
            .successful(false)
            .message(message)
            .technicalMessage(technicalMessage)
            .code(status.value())
            .build()

        @JvmStatic
        fun <T> badRequest(message: String): ResponseWrapper<T> = error(message, HttpStatus.BAD_REQUEST)

        @JvmStatic
        fun <T> notFound(message: String): ResponseWrapper<T> = error(message, HttpStatus.NOT_FOUND)

        @JvmStatic
        fun <T> serverError(message: String): ResponseWrapper<T> = error(message, HttpStatus.INTERNAL_SERVER_ERROR)
    }
}