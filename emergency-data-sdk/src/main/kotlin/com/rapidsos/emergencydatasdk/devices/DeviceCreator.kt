package com.rapidsos.emergencydatasdk.devices

import com.rapidsos.emergencydatasdk.data.devices.Device
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
 * Create new [Device] records.
 *
 * Example:
 *
 * ```
 * private fun create(sessionToken: SessionToken) {
 *      val device = Device()
 *      device.stepsTaken = StepsTaken().apply {
 *          this.displayName = "Steps Taken"
 *          this.type = "realtime-metric"
 *
 *          val takenValue = StepsTakenValue("Fitbit", "https://www.rapidsos.com", "https",
 *          "Average 10,000 a day")
 *          this.value = listOf(takenValue)
 *      }
 *
 *      DeviceCreator().create(sessionToken, device).subscribe({
 *          error(it)
 *      }, {
 *          error(it?.message)
 *      })
 * }
 * ```
 *
 * @author Josias Sena
 * @see SessionToken
 * @see Device
 * @see Maybe
 */
class DeviceCreator : CreatorRepo<Device> {

    @Inject
    protected lateinit var api: EmgDataApi

    init {
        Injector.component?.inject(this)
    }

    /**
     * Create a new [Device] record for the authenticated user
     *
     * @param sessionToken the current sessions token
     * @param newItem the new device to create
     * @return A [Maybe] that gets notified of an error or success. On success the newly
     * created [Device] is returned.
     */
    override fun create(sessionToken: SessionToken, newItem: Device): Maybe<Device?> {
        return api.createNewDevice("Bearer ${sessionToken.accessToken}", newItem)
                .subscribeOn(Schedulers.io())
                .filter {
                    SdkInitiatedValidator.checkIfSDKIsInitialized("Error " +
                            "adding new device. Please make sure the SDK has been initialized.")
                }
                .filter {
                    SessionTokenVerifier.checkIfTokenIsExpired(sessionToken, "Cannot " +
                            "add new device. The session token provided is either invalid or expired.")
                }
                .filter { ResponseChecker.checkIfItsASuccessfulResponse(it) }
                .timeout(15, TimeUnit.SECONDS)
                .map { it.body() }
                .observeOn(AndroidSchedulers.mainThread())
    }

}