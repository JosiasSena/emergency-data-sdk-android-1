package com.rapidsos.emergencydatasdk.devices

import com.rapidsos.emergencydatasdk.data.devices.Device
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
 * Update [Device] attributes
 *
 * Example:
 *
 * ```
 * private fun update(sessionToken: SessionToken, device: Device?) {
 *      device.apply {
 *          this?.batteryPower = BatteryPower(arrayListOf<BatteryPowerValue>())
 *          this?.stepsTaken?.apply {
 *              this.displayName = "Some new display name"
 *              this.type1 = "Some new type"
 *          }
 *      }
 *
 *      DeviceUpdater().updateDevice(sessionToken, device?.id as Int, device)
 *              .subscribe({ updatedDevice: Device? ->
 *                  // Called on success by returning the device with its updated fields.
 *              }, { throwable: Throwable? ->
 *                  // Called when an error occurs
 *              })
 *  }
 * ```
 *
 * @author Josias Sena
 * @see SessionToken
 * @see Device
 * @see Maybe
 */
class DeviceUpdater : UpdaterRepo<Device> {

    @Inject
    protected lateinit var api: EmgDataApi

    init {
        Injector.component?.inject(this)
    }

    /**
     * Update only specific attributes of the specified [Device] record with a partial update.
     *
     * @param sessionToken the current sessions token
     * @param id the id of the [Device] to be updated
     * @param item the [Device] with its updated fields
     * @return A [Maybe] that gets notified of an error or success. On success the newly
     * updated [Device] is returned.
     */
    override fun update(sessionToken: SessionToken, id: Int, item: Device): Maybe<Device?> {
        return api.updateDeviceById("Bearer ${sessionToken.accessToken}", id, item)
                .subscribeOn(Schedulers.io())
                .filter {
                    SdkInitiatedValidator.checkIfSDKIsInitialized("Error updating " +
                            "device with id=$id. Please make sure the SDK has" +
                            " been initialized.")
                }
                .filter {
                    SessionTokenVerifier.checkIfTokenIsExpired(sessionToken, "Cannot " +
                            "update device with id=$id. The session token provided is " +
                            "either invalid or expired.")
                }
                .filter { ResponseChecker.checkIfItsASuccessfulResponse(it) }
                .timeout(15, TimeUnit.SECONDS)
                .map { it.body() }
                .observeOn(AndroidSchedulers.mainThread())
    }

}