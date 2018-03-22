package com.rapidsos.emergencydatasdk.internal.contracts

import com.rapidsos.emergencydatasdk.data.network_response.SessionToken
import io.reactivex.Maybe

/**
 * @author Josias Sena
 */
interface CreatorRepo<T> {

    /**
     * Create a new [T] record for the authenticated user
     *
     * @param sessionToken the current sessions token
     * @param newItem the new [T] to create
     * @return A [Maybe] that gets notified of an error or success. On success the newly
     * created [T] is returned.
     */
    fun create(sessionToken: SessionToken, newItem: T): Maybe<T?>
}