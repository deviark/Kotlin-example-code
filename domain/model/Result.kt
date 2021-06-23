package com.lotteryadviser.domain.model


sealed class Result<out T>(val data: T) {
    class Loading<out T>(data: T): Result<T>(data)
    class Success<out T>(data: T): Result<T>(data)
    class Failure<out T>(data: T, val error: Error): Result<T>(data)
}

sealed class Error {
    object NetworkError : Error()
    object BillingError : Error()
    object GenericError : Error()
    object ResponseError : Error()
    object PersistenceError : Error()
}

