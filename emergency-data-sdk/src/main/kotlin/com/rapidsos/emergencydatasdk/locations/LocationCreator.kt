package com.rapidsos.emergencydatasdk.locations

import com.rapidsos.emergencydatasdk.data.location.Location
import com.rapidsos.emergencydatasdk.data.network_response.SessionToken
import com.rapidsos.emergencydatasdk.internal.api.EmgDataApi
import com.rapidsos.emergencydatasdk.internal.contracts.CreatorRepo
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
 * Adds a new location to the current profile.
 *
 * Example:
 *
 *```
 *   private fun createNewLocation(sessionToken: SessionToken) {
 *       val addresses = listOf(AddressValue().apply {
 *           this.countryCode = "US"
 *           this.label = "Home address"
 *           this.locality = "NYC"
 *           this.region = "NY"
 *           this.postalCode = "10457"
 *           this.streetAddress = "9372 Manhattan Ave"
 *       })
 *
 *       val address = Address().apply {
 *           this.displayName = "Personal addresses"
 *           this.type = "This is an address"
 *           this.value = addresses
 *       }
 *
 *       val location = Location().apply {
 *           this.address = address
 *           this.comment = Comment(listOf("This is some comment"))
 *       }
 *
 *       val disposable: Disposable = LocationCreator().create(sessionToken, location)
 *               .subscribe({ locationCreated: Location? ->
 *                   // do something with the new added address.
 *               }, { throwable: Throwable ->
 *                   // do something on error
 *               })
 *
 *       disposable.dispose()
 *   }
 *```
 *
 * @author Josias Sena
 * @see SessionToken
 * @see Location
 * @see Maybe
 */
class LocationCreator : CreatorRepo<Location> {

    @Inject
    protected lateinit var api: EmgDataApi

    init {
        Injector.component?.inject(this)
    }

    /**
     * Create a new [Location] record for the authenticated user
     *
     * @param sessionToken the current sessions token
     * @param newItem the new [Location] to create
     * @return A [Maybe] that gets notified of an error or success. On success the newly
     * created [Location] is returned.
     */
    override fun create(sessionToken: SessionToken, newItem: Location): Maybe<Location?> {
        return api.addNewLocation("Bearer ${sessionToken.accessToken}", newItem)
                .subscribeOn(Schedulers.io())
                .filter {
                    SdkInitiatedValidator.checkIfSDKIsInitialized("Error " +
                            "adding new location. Please make sure the SDK has been initialized.")
                }
                .filter {
                    SessionTokenVerifier.checkIfTokenIsExpired(sessionToken, "Cannot " +
                            "add new location. The session token provided is either invalid or expired.")
                }
                .filter { ResponseChecker.checkIfItsASuccessfulResponse(it) }
                .timeout(15, TimeUnit.SECONDS)
                .map { it.body() }
                .observeOn(AndroidSchedulers.mainThread())
    }

}