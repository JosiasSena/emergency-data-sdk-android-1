package com.rapidsos.emergencydatasdk.internal.contracts

import com.rapidsos.emergencydatasdk.data.network_response.SessionToken
import io.reactivex.Maybe

/**
 * @author Josias Sena
 */
interface RemoverRepo<T> {

    /**
     * Remove a full [T] record belonging to the authenticated user
     *
     * @param sessionToken the current sessions authentication token
     * @param id the id of the [T] to delete
     * @return a [Maybe] which is notified when an error has occurred, or when the deletion was
     * performed successfully. On success the deleted [T] is returned.
     */
    fun remove(sessionToken: SessionToken, id: Int): Maybe<T?>
}