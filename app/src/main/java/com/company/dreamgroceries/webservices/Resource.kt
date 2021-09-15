package com.company.dreamgroceries.webservices


data class Resource<out T>(
    val status: Status,
    val data: T?,
    val message: String?,
    val errorCode: Int = 0
) {
    companion object {
        fun <T> success(data: T): Resource<T> =
            Resource(status = Status.SUCCESS, data = data, message = null)

        fun <T> error(data: T?, message: String,errorCode :Int = 0): Resource<T> =
            Resource(status = Status.ERROR, data = data, message = message,errorCode = errorCode)

        fun <T> loading(data: T?): Resource<T> =
            Resource(status = Status.LOADING, data = data, message = null)
    }
}