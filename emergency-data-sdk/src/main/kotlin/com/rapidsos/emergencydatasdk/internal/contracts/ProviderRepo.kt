package com.rapidsos.emergencydatasdk.internal.contracts

import com.rapidsos.emergencydatasdk.data.network_response.SessionToken
import io.reactivex.Maybe

/**
 * @author Josias Sena
 */
interface ProviderRepo<T> {

    /**
     * Retrieve [T] record/records belonging to the authenticated user
     *
     * @param sessionToken the current sessions token
     * @return A [Maybe] that gets notified of an error or success.
     */
    fun getAll(sessionToken: SessionToken): Maybe<ArrayList<T>?>

    /**
     * Retrieve the full existing [T] record belonging to the authenticated user by ID
     *
     * @param sessionToken the current sessions token
     * @param id the [T] id to be retrieved
     * @return A [Maybe] that gets notified of an error or success.
     */
    fun getById(sessionToken: SessionToken, id: Int): Maybe<T?>
}