package nl.vslcatena.vslcatena.util

sealed class Result<T> {

    fun isSuccesful(): Boolean = this is Success
    fun isFailure(): Boolean = this is Failure

    fun getValueOrNull(): T? = if (this is Success) value else null
    fun getExceptionOrNull(): Throwable? = if (this is Failure) exception else null


    class Success<T>(val value: T) : Result<T>()
    class Failure<T>(val exception: Throwable) : Result<T>()
}