package com.rapidsos.emergencydatasdk.locations

import com.rapidsos.emergencydatasdk.data.location.Comment
import com.rapidsos.emergencydatasdk.data.location.Location
import com.rapidsos.emergencydatasdk.data.network_response.SessionToken
import com.rapidsos.emergencydatasdk.internal.api.EmgDataApi
import com.rapidsos.emergencydatasdk.internal.contracts.UpdaterRepo
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
 * Updates existing locations.
 *
 * Example:
 *
 * ```
 * private fun updateLocation(sessionToken: SessionToken) {
 *     val addresses = listOf(AddressValue().apply {
 *         this.countryCode = "US"
 *         this.label = "Home address"
 *         this.locality = "NYC"
 *         this.region = "NY"
 *         this.postalCode = "10457"
 *         this.streetAddress = "9372 Manhattan Ave"
 *     })
 *
 *     val address = Address().apply {
 *         this.displayName = "Personal addresses"
 *         this.type = "This is an address"
 *         this.value = addresses
 *     }
 *
 *     val location = Location().apply {
 *         this.address = address
 *         this.comment = Comment(listOf("This is some comment"))
 *     }
 *
 *     val disposable = LocationUpdater().update(sessionToken, 14, location)
 *             .subscribe({ updatedLocation ->
 *                 // On success, the new updated location is returned
 *             }, { t: Throwable? ->
 *                 // Thrown when an error occurs.
 *             })
 *
 *     disposable.dispose()
 * }
 * ```
 *
 * If after adding an object to the location we want to remove it,
 * either set it to null or don't include it.
 *
 * For example, take the example above. Lets remove the [Comment].
 *
 * ```
 * private fun updateLocation(sessionToken: SessionToken) {
 *     val addresses = listOf(AddressValue().apply {
 *         this.countryCode = "US"
 *         this.label = "Home address"
 *         this.locality = "NYC"
 *         this.region = "NY"
 *         this.postalCode = "10457"
 *         this.streetAddress = "9372 Manhattan Ave"
 *     })
 *
 *     val address = Address().apply {
 *         this.displayName = "Personal addresses"
 *         this.type = "This is an address"
 *         this.value = addresses
 *     }
 *
 *     val location = Location().apply {
 *         this.address = address
 *     }
 *
 *     val disposable = LocationUpdater().update(sessionToken, 14, location)
 *             .subscribe({ updatedLocation ->
 *                 // On success, the new updated location is returned without a comment.
 *                 // Just the address.
 *             }, { t: Throwable? ->
 *                 // Thrown when an error occurs.
 *             })
 *
 *     disposable.dispose()
 * }
 * ```
 *
 * @author Josias Sena
 * @see SessionToken
 * @see Location
 * @see Maybe
 */
class LocationUpdater : UpdaterRepo<Location> {

    @Inject
    protected lateinit var api: EmgDataApi

    init {
        Injector.component?.inject(this)
    }

    /**
     * Update only specific attributes of the record with a partial update
     *
     * @param sessionToken the current sessions token
     * @param id the id of the item to be updated
     * @param item the record with its updated fields
     * @return A [Maybe] that gets notified of an error or success. On success the newly
     * updated record is returned.
     */
    override fun update(sessionToken: SessionToken, id: Int, item: Location): Maybe<Location?> {
        return api.updateLocationById("Bearer ${sessionToken.accessToken}", id, item)
                .subscribeOn(Schedulers.io())
                .filter {
                    SdkInitiatedValidator.checkIfSDKIsInitialized("Error updating " +
                            "location with id=$id. Please make sure the SDK has" +
                            " been initialized.")
                }
                .filter {
                    SessionTokenVerifier.checkIfTokenIsExpired(sessionToken, "Cannot " +
                            "update location with id=$id. The session token provided is " +
                            "either invalid or expired.")
                }
                .filter { ResponseChecker.checkIfItsASuccessfulResponse(it) }
                .timeout(15, TimeUnit.SECONDS)
                .map { it.body() }
                .observeOn(AndroidSchedulers.mainThread())
    }
}