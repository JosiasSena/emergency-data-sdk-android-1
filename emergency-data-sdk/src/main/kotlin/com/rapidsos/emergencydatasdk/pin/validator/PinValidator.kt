package com.rapidsos.emergencydatasdk.pin.validator

import com.google.gson.JsonObject
import com.rapidsos.emergencydatasdk.data.network_response.CallerId
import com.rapidsos.emergencydatasdk.data.network_response.SessionToken
import com.rapidsos.emergencydatasdk.internal.api.EmgDataApi
import com.rapidsos.emergencydatasdk.internal.helpers.dependency_injection.Injector
import com.rapidsos.emergencydatasdk.internal.helpers.network.ResponseChecker
import com.rapidsos.emergencydatasdk.internal.helpers.network.SessionTokenVerifier
import com.rapidsos.emergencydatasdk.pin.provider.PinProvider
import com.rapidsos.emergencydatasdk.sdk.SdkInitiatedValidator
import io.reactivex.Maybe
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Response
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Validate a pin that was sent to a phone number using the [PinProvider]
 *
 * Example:
 *
 * ```
 * private fun validatePin() {
 *       val pinValidator = PinValidator()
 *       pinValidator.validatePin(sessionToken, "15555555555", pinProvided)
 *               .subscribe(object : MaybeObserver<CallerId?> {
 *                   override fun onSubscribe(d: Disposable) {
 *                       // Called as soon as the method is called. The disposable is used to stop
 *                       // the request at any given time if needed by calling d.dispose()
 *                   }
 *
 *                   override fun onSuccess(callerId: CallerId) {
 *                       // Called on success. Returns a `CallerId` object as confirmation that the
 *                       // request was successful
 *                   }
 *
 *                   override fun onError(e: Throwable) {
 *                       // Called when an error occurs
 *                   }
 *
 *                   override fun onComplete() {
 *                       // Called once the deferred computation completes normally.
 *                   }
 *               })
 * }
 * ```
 *
 * @author Josias Sena
 * @see PinProvider
 */
class PinValidator {

    @Inject
    protected lateinit var api: EmgDataApi

    init {
        Injector.component?.inject(this)
    }

    /**
     * Validate a pin number
     *
     * @param token the session token to make the network request
     * @param phoneNumber the phone number the pin was sent to
     * @param pin the pin number to validate
     * @return an [Observer] the observer to be notified of error, or success. On Success a [CallerId]
     * is returned with an id attached to it.
     */
    fun validatePin(token: SessionToken, phoneNumber: String, pin: Int): Maybe<CallerId?> {
        val body = getBody(phoneNumber, pin)
        val accessToken = "Bearer ${token.accessToken}"

        return api.validateCallerId(accessToken, body)
                .subscribeOn(Schedulers.io())
                .filter {
                    SdkInitiatedValidator.checkIfSDKIsInitialized("Error validating " +
                            "pin. Please make sure the SDK has been initialized.")
                }
                .filter {
                    SessionTokenVerifier.checkIfTokenIsExpired(token, "Error validating pin. " +
                            "The session token provided is either invalid or expired.")
                }
                .filter { filterValidation(it) }
                .timeout(15, TimeUnit.SECONDS)
                .map { response: Response<CallerId> -> response.body() }
                .observeOn(AndroidSchedulers.mainThread())
    }

    private fun getBody(callerId: String, pin: Int): JsonObject {
        return JsonObject().apply {
            addProperty("caller_id", callerId)
            addProperty("validation_code", pin.toString())
        }
    }

    private fun filterValidation(response: Response<CallerId>): Boolean {
        return if (response.isSuccessful) {
            true
        } else {
            val errorBody = response.errorBody()?.string()

            if (errorBody?.contains("Validation code is invalid") as Boolean) {
                throw Exception("Validation code is invalid.")
            } else {
                ResponseChecker.checkIfItsASuccessfulResponse(response)
            }
        }
    }

}