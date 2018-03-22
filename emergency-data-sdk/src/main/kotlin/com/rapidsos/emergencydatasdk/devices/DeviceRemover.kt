package com.rapidsos.emergencydatasdk.devices

import com.rapidsos.emergencydatasdk.data.devices.Device
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
 * Removes full [Device] records belonging to an authenticated user.
 *
 * Example:
 *
 * ```
 * private fun remove(sessionToken: SessionToken, deviceId: Int) {
 *     DeviceRemover().deleteDevice(sessionToken, deviceId)
 *             .subscribe({ device: Device? ->
 *                 // On success the removed device is returned
 *             }, { throwable: Throwable? ->
 *                 // Called when an error occurs
 *             })
 * }
 * ```
 *
 * @author Josias Sena
 * @see Device
 * @see SessionToken
 */
class DeviceRemover : RemoverRepo<Device> {

    @Inject
    protected lateinit var api: EmgDataApi

    init {
        Injector.component?.inject(this)
    }

    /**
     * Delete a full Device record belonging to the authenticated user
     *
     * @param sessionToken the current sessions authentication token
     * @param id the id of the [Device] to delete
     * @return a [Maybe] which is notified when an error has occurred, or when the deletion was
     * performed successfully. On success the deleted [Device] is returned.
     */
    override fun remove(sessionToken: SessionToken, id: Int): Maybe<Device?> {
        return api.deleteDevice("Bearer ${sessionToken.accessToken}", id)
                .subscribeOn(Schedulers.io())
                .filter {
                    SdkInitiatedValidator.checkIfSDKIsInitialized("Error " +
                            "deleting device with id=$id. Please make sure the SDK has been" +
                            " initialized.")
                }
                .filter {
                    SessionTokenVerifier.checkIfTokenIsExpired(sessionToken, "Cannot " +
                            "delete device with id=$id. The session token provided is " +
                            "either invalid or expired.")
                }
                .filter { ResponseChecker.checkIfItsASuccessfulResponse(it) }
                .timeout(15, TimeUnit.SECONDS)
                .map { it.body() }
                .observeOn(AndroidSchedulers.mainThread())
    }
}