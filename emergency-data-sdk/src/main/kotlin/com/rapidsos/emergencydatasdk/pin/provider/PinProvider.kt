package com.rapidsos.emergencydatasdk.pin.provider

import com.google.gson.JsonObject
import com.rapidsos.emergencydatasdk.data.network_response.CallerId
import com.rapidsos.emergencydatasdk.data.network_response.SessionToken
import com.rapidsos.emergencydatasdk.internal.api.EmgDataApi
import com.rapidsos.emergencydatasdk.internal.helpers.dependency_injection.Injector
import com.rapidsos.emergencydatasdk.internal.helpers.network.ResponseChecker
import com.rapidsos.emergencydatasdk.internal.helpers.network.SessionTokenVerifier
import com.rapidsos.emergencydatasdk.pin.validator.PinValidator
import com.rapidsos.emergencydatasdk.sdk.SdkInitiatedValidator
import io.reactivex.Maybe
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Response
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Sends an SMS message to a device with a pin to validate using the [PinValidator]
 *
 * Example:
 *
 * ```
 * private fun requestPin() {
 *      val pinProvider = PinProvider()
 *      pinProvider.requestPin(sessionToken, "15555555555")
 *              .subscribe(object : MaybeObserver<Response<CallerId>> {
 *                  override fun onSubscribe(d: Disposable) {
 *                       // Called as soon as the method is called. The disposable is used to stop
 *                       // the request at any given time if needed by calling d.dispose()
 *                  }
 *
 *                  override fun onSuccess(response: Response<CallerId>) {
 *                      // Called on success. Returns a response object with a `CallerId` object as
 *                      // its body as confirmation that the request was successful
 *                  }
 *
 *                  override fun onError(e: Throwable) {
 *                      // Called when an error occurs
 *                  }
 *
 *                  override fun onComplete() {
 *                      // Called once the deferred computation completes normally.
 *                  }
 *              })
 *  }
 * ```
 *
 * @author Josias Sena
 * @see PinValidator
 */
class PinProvider {

    @Inject
    protected lateinit var api: EmgDataApi

    init {
        Injector.component?.inject(this)
    }

    /**
     * Request for a new pin. A pin is sent as an SMS message to the phone number provided.
     *
     * @param token the session token to make the network request
     * @param phoneNumber the phone number to send the pin to
     * @return an [Observer] the observer to be notified of error, success, and completion
     */
    fun requestPin(token: SessionToken, phoneNumber: String): Maybe<Response<CallerId>> {
        val accessToken = "Bearer ${token.accessToken}"
        val body = JsonObject().apply {
            addProperty("caller_id", phoneNumber)
        }

        return api.createCallerId(accessToken, body)
                .subscribeOn(Schedulers.io())
                .filter {
                    SdkInitiatedValidator.checkIfSDKIsInitialized("Error " +
                            "requesting pin. Please make sure the SDK has been initialized.")
                }
                .filter {
                    SessionTokenVerifier.checkIfTokenIsExpired(token, "Cannot " +
                            "request a pin. The session token provided is either invalid or expired.")
                }
                .filter { ResponseChecker.checkIfItsASuccessfulResponse(it) }
                .timeout(15, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
    }

}