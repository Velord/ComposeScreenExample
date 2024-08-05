package com.velord.util.exception

sealed class BaseException(override val message: String? = null) : Exception(message) {

    data object Unknown : BaseException()
    data object NoInternet : BaseException()

    sealed class Http(
        open val value: String
    ) : BaseException(value) {

        sealed class Client(override val value: String) : Http(value) {
            class Unauthorized(override val value: String) : Client(value)
            class AccessDenied(override val value: String) : Client(value)
            class NotFound(override val value: String) : Client(value)
        }

        sealed class Server(override val value: String) : Http(value) {
            class ServiceUnavailable(override val value: String) : Server(value)
        }
    }
}