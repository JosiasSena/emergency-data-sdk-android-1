package com.rapidsos.emergencydatasdk.devices

import com.rapidsos.emergencydatasdk.data.devices.Device
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
 * Provides saved device information if any.
 *
 * Example getting devices:
 *
 * ```
 * private fun getAll(sessionToken: SessionToken) {
 *      DeviceProvider().getDevices(sessionToken)
 *              .subscribe({ devices: ArrayList<Device>? ->
 *                  // do something with the devices
 *              }, {
 *                  // do something on error
 *              })
 * }
 * ```
 *
 * @author Josias Sena
 * @see SessionToken
 * @see Device
 */
class DeviceProvider : ProviderRepo<Device> {

    @Inject
    protected lateinit var api: EmgDataApi

    init {
        Injector.component?.inject(this)
    }

    /**
     * Retrieve all Device records belonging to the authenticated user
     *
     * @param sessionToken the current sessions token
     * @return A [Maybe] that gets notified of an error or success. On success a list of all of
     * the available [Device]s is returned.
     */
    override fun getAll(sessionToken: SessionToken): Maybe<ArrayList<Device>?> {
        return api.getDevices("Bearer ${sessionToken.accessToken}")
                .subscribeOn(Schedulers.io())
                .filter {
                    SdkInitiatedValidator.checkIfSDKIsInitialized("Error " +
                            "getting devices. Please make sure the SDK has been initialized.")
                }
                .filter {
                    SessionTokenVerifier.checkIfTokenIsExpired(sessionToken, "Cannot " +
                            "get devices. The session token provided is either invalid or expired.")
                }
                .filter { ResponseChecker.checkIfItsASuccessfulResponse(it) }
                .timeout(15, TimeUnit.SECONDS)
                .map { it.body() }
                .observeOn(AndroidSchedulers.mainThread())
    }

    /**
     * Retrieve the full existing Device record belonging to the authenticated user by ID
     *
     * @param sessionToken the current sessions token
     * @param id the id of the device to be retrieved
     * @return A [Maybe] that gets notified of an error or success. On success the device whose
     * id matches the id provided is returned.
     */
    override fun getById(sessionToken: SessionToken, id: Int): Maybe<Device?> {
        return api.getDeviceById("Bearer ${sessionToken.accessToken}", id)
                .subscribeOn(Schedulers.io())
                .filter {
                    SdkInitiatedValidator.checkIfSDKIsInitialized("Error " +
                            "getting device with id=$id. Please make sure the SDK has been " +
                            "initialized.")
                }
                .filter {
                    SessionTokenVerifier.checkIfTokenIsExpired(sessionToken, "Cannot " +
                            "get device with id=$id. The session token provided is either " +
                            "invalid or expired.")
                }
                .filter { ResponseChecker.checkIfItsASuccessfulResponse(it) }
                .timeout(15, TimeUnit.SECONDS)
                .map { it.body() }
                .observeOn(AndroidSchedulers.mainThread())
    }

}