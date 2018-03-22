package com.rapidsos.emergencydatasdk.auth.login

import com.rapidsos.emergencydatasdk.data.network_response.SessionToken
import com.rapidsos.emergencydatasdk.data.user.User
import com.rapidsos.emergencydatasdk.internal.api.EmgDataApi
import com.rapidsos.emergencydatasdk.internal.helpers.dependency_injection.Injector
import com.rapidsos.emergencydatasdk.internal.helpers.network.TOO_MANY_REQUEST_CODE
import com.rapidsos.emergencydatasdk.internal.helpers.network.UNAUTHORIZED_RESPONSE_CODE
import com.rapidsos.emergencydatasdk.internal.preferences.EmgDataPreferences
import com.rapidsos.emergencydatasdk.sdk.SdkInitiatedValidator
import io.reactivex.Maybe
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Response
import javax.inject.Inject

/**
 * Supplies session tokens to be used while using some of the other RAIN Sdk classes.
 *
 * Session tokens are short lived (1 hour) so a new one will be required when the current one
 * expires.
 *
 * Example:
 *
 * ```
 * private fun getSessionToken() {
 *       val sessionManager = SessionManager()
 *       sessionManager.getSessionToken(username, password)
 *               .subscribe(object : MaybeObserver<SessionToken?> {
 *                   override fun onSubscribe(d: Disposable) {
 *                       // Called as soon as the method is called. The disposable is used to stop
 *                       // the request at any given time if needed by calling d.dispose()
 *                   }
 *
 *                   override fun onSuccess(sessionToken: SessionToken) {
 *                       // Called when a successful response has been returned. The SessionToken
 *                       // to be used is returned.
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
 * @see SessionToken
 * @see User
 */
class SessionManager {

    @Inject
    protected lateinit var api: EmgDataApi

    @Inject
    protected lateinit var preferences: EmgDataPreferences

    init {
        Injector.component?.inject(this)
    }

    /**
     * Supplies a session token for the user provided.
     *
     * @param username the users username
     * @param password the users password
     * @return An [Observer] that gets notified when the user has been successfully
     * login User or if there is an error when trying to log the user in
     */
    fun getSessionToken(username: String, password: String): Maybe<SessionToken> {
        return api.getSessionToken(username = username, password = password,
                clientId = preferences.getClientId(), clientSecret = preferences.getClientSecret())
                .subscribeOn(Schedulers.io())
                .filter {
                    SdkInitiatedValidator.checkIfSDKIsInitialized("Error " +
                            "logging user in. Please make sure the SDK has been initialized.")
                }
                .filter { filter(it) }
                .map { it.body() as SessionToken }
                .observeOn(AndroidSchedulers.mainThread())
    }

    private fun filter(response: Response<SessionToken>): Boolean {
        return if (response.isSuccessful) {
            true
        } else {
            val errorBody = response.errorBody()?.string()
            val message = response.message()
            val code = response.code()

            when (code) {
                UNAUTHORIZED_RESPONSE_CODE -> throw Exception("Invalid user credentials.")
                TOO_MANY_REQUEST_CODE -> throw Exception("Too many requests. Please try again later.")
                else -> throw Exception("Login error: $code $message $errorBody")
            }
        }
    }
}