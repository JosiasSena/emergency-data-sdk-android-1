package com.rapidsos.emergencydatasdk.locations

import com.rapidsos.emergencydatasdk.data.location.Location
import com.rapidsos.emergencydatasdk.data.network_response.SessionToken
import com.rapidsos.emergencydatasdk.internal.api.EmgDataApi
import com.rapidsos.emergencydatasdk.internal.contracts.RemoverRepo
import com.rapidsos.emergencydatasdk.internal.helpers.dependency_injection.Injector
import com.rapidsos.emergencydatasdk.internal.helpers.network.ResponseChecker
import com.rapidsos.emergencydatasdk.internal.helpers.network.SessionTokenVerifier
import com.rapidsos.emergencydatasdk.sdk.SdkInitiatedValidator
import io.reactivex.Maybe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Removes saved locations.
 *
 * Example:
 *
 * ```
 * private fun removeLocation(sessionToken: SessionToken, locationId: Int) {
 *     LocationRemover().remove(sessionToken, locationId)
 *             .subscribe({ location: Location? ->
 *                 // On success the removed location is returned
 *             }, { throwable: Throwable? ->
 *                 // Called when an error occurs
 *             })
 * }
 * ```
 *
 * @author Josias Sena
 * @see SessionToken
 * @see Location
 */
class LocationRemover : RemoverRepo<Location> {

    @Inject
    protected lateinit var api: EmgDataApi

    init {
        Injector.component?.inject(this)
    }

    /**
     * Remove a full [Location] record belonging to the authenticated user
     *
     * @param sessionToken the current sessions authentication token
     * @param id the id of the [Location] to delete
     * @return a [Maybe] which is notified when an error has occurred, or when the deletion was
     * performed successfully. On success the deleted [Location] is returned.
     */
    override fun remove(sessionToken: SessionToken, id: Int): Maybe<Location?> {
        return api.deleteLocationById("Bearer ${sessionToken.accessToken}", id)
                .subscribeOn(Schedulers.io())
                .filter {
                    SdkInitiatedValidator.checkIfSDKIsInitialized("Error deleting " +
                            "location with id=$id. Please make sure the SDK has" +
                            " been initialized.")
                }
                .filter {
                    SessionTokenVerifier.checkIfTokenIsExpired(sessionToken, "Cannot " +
                            "delete location with id=$id. The session token provided is " +
                            "either invalid or expired.")
                }
                .filter { ResponseChecker.checkIfItsASuccessfulResponse(it) }
                .timeout(15, TimeUnit.SECONDS)
                .map { it.body() }
                .observeOn(AndroidSchedulers.mainThread())
    }
}