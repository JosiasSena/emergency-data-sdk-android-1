package com.rapidsos.emergencydatasdk.internal.contracts

import com.rapidsos.emergencydatasdk.data.network_response.SessionToken
import io.reactivex.Maybe

/**
 * @author Josias Sena
 */
interface UpdaterRepo<T> {

    /**
     * Update only specific attributes of the record with a partial update
     *
     * @param sessionToken the current sessions token
     * @param id the id of the item to be updated
     * @param item the record with its updated fields
     * @return A [Maybe] that gets notified of an error or success. On success the newly
     * updated record is returned.
     */
    fun update(sessionToken: SessionToken, id: Int, item: T): Maybe<T?>
}