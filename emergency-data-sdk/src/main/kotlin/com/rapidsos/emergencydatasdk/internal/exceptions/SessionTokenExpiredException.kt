package com.rapidsos.emergencydatasdk.internal.exceptions

import com.rapidsos.emergencydatasdk.data.network_response.SessionToken
import com.rapidsos.emergencydatasdk.internal.helpers.network.SessionTokenVerifier

/**
 * Thrown when a [SessionToken] is expired.
 *
 * A session token is expired when the [SessionToken.expiresIn] is less than the current time.
 *
 * @author Josias Sena
 *
 * @see SessionToken
 * @see SessionTokenVerifier
 */
class SessionTokenExpiredException(errorMessage: String) : Exception(errorMessage)