package com.rapidsos.emergencydatasdk.internal.helpers.network

import retrofit2.Response

/**
 * @author Josias Sena
 */
internal object ResponseChecker {

    @Throws(RuntimeException::class)
    fun <T> checkIfItsASuccessfulResponse(response: Response<T>): Boolean {
        return if (response.isSuccessful) {
            true
        } else {
            val errorBody = response.errorBody()?.string()
            val message = response.message()
            val code = response.code()

            when (code) {
                BAD_REQUEST_RESPONSE_CODE -> throw RuntimeException(errorBody)
                TOO_MANY_REQUEST_CODE -> throw RuntimeException("Too many requests. Please try again later")
                else -> throw RuntimeException("Something went wrong, please try again: $message $errorBody")
            }
        }
    }

}