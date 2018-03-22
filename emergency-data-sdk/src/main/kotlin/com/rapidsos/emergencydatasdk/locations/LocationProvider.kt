package com.rapidsos.emergencydatasdk.locations

import com.rapidsos.emergencydatasdk.data.location.Location
import com.rapidsos.emergencydatasdk.data.network_response.SessionToken
import com.rapidsos.emergencydatasdk.internal.api.EmgDataApi
import com.rapidsos.emergencydatasdk.internal.contracts.ProviderRepo
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
 * Provides saved locations if any.
 *
 * Example getting locations:
 *
 * ```
 *  fun getLocations(sessionToken: SessionToken) {
 *      val locationProvider = LocationProvider()
 *      locationProvider.getAll(sessionToken)
 *              .subscribe({ locations: ArrayList<Location>? ->
 *                  // returns all available locations (if any).
 *              }, { throwable: Throwable? ->
 *                  // Called when an error occurs
 *              })
 *  }
 * ```
 *
 * @author Josias Sena
 * @see SessionToken
 * @see Location
 */
class LocationProvider : ProviderRepo<Location> {

    @Inject
    protected lateinit var api: EmgDataApi

    init {
        Injector.component?.inject(this)
    }

    /**
     * Returns all of the available locations.
     *
     * @param sessionToken the current sessions token
     * @return a [Maybe] that gets notified of an error or success. On success a list of all of
     * the available [Location]s is returned.
     */
    override fun getAll(sessionToken: SessionToken): Maybe<ArrayList<Location>?> {
        return api.getLocations("Bearer ${sessionToken.accessToken}")
                .subscribeOn(Schedulers.io())
                .filter {
                    SdkInitiatedValidator.checkIfSDKIsInitialized("Error " +
                            "getting locations. Please make sure the SDK has been initialized.")
                }
                .filter {
                    SessionTokenVerifier.checkIfTokenIsExpired(sessionToken, "Cannot " +
                            "get locations. The session token provided is either invalid or expired.")
                }
                .filter { ResponseChecker.checkIfItsASuccessfulResponse(it) }
                .timeout(15, TimeUnit.SECONDS)
                .map { it.body() }
                .observeOn(AndroidSchedulers.mainThread())
    }

    /**
     * Returns all of the available locations.
     *
     * @param sessionToken the current sessions token
     * @param id the id of the location to be retrieved
     * @return a [Maybe] that gets notified of an error or success. On success the location whose
     * id matches the id provided is returned.
     */
    override fun getById(sessionToken: SessionToken, id: Int): Maybe<Location?> {
        return api.getLocationById("Bearer ${sessionToken.accessToken}", id)
                .subscribeOn(Schedulers.io())
                .filter {
                    SdkInitiatedValidator.checkIfSDKIsInitialized("Error " +
                            "getting location with id=$id. Please make sure the SDK has " +
                            "been initialized.")
                }
                .filter {
                    SessionTokenVerifier.checkIfTokenIsExpired(sessionToken, "Cannot " +
                            "get location with id=$id. The session token provided is " +
                            "either invalid or expired.")
                }
                .filter { ResponseChecker.checkIfItsASuccessfulResponse(it) }
                .timeout(15, TimeUnit.SECONDS)
                .map { it.body() }
                .observeOn(AndroidSchedulers.mainThread())
    }

}