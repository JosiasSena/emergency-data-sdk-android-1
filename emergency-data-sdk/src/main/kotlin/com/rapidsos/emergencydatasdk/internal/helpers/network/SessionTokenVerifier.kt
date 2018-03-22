package com.rapidsos.emergencydatasdk.internal.helpers.network

import com.rapidsos.emergencydatasdk.data.network_response.SessionToken
import com.rapidsos.emergencydatasdk.internal.exceptions.SessionTokenExpiredException
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Verifies if a Oauth token is valid or not
 *
 * @author Josias Sena
 */
object SessionTokenVerifier {

    /**
     * Checks if the [token] is expired or not based on its expiration date
     *
     * @param token the token to verify
     * @return true if the token is a valid token, and has not expired yet. False otherwise.
     */
    @JvmStatic
    fun isTokenExpired(token: SessionToken): Boolean {
        val currentDate = Date(System.currentTimeMillis())
        val expiresInMillis = TimeUnit.SECONDS.toMillis(token.expiresIn.toLong())
        val expiryDate = Date(token.issuedAt.toLong().plus(expiresInMillis))
        return currentDate.after(expiryDate)
    }

    /**
     * Checks if the [token] is expired or not based on its expiration date
     *
     * @param token the token to verify
     * @param errorMessage the error message to use in the exception that is thrown
     * @return true if the token is a valid token, and has not expired yet. If the token is
     * expired, an exception is thrown with the message provided.
     */
    @Throws(SessionTokenExpiredException::class)
    internal fun checkIfTokenIsExpired(token: SessionToken, errorMessage: String): Boolean {
        return if (!isTokenExpired(token)) {
            true
        } else {
            throw SessionTokenExpiredException(errorMessage)
        }
    }

}