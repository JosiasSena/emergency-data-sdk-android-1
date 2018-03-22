package com.rapidsos.emergencydatasdk.profile

import com.rapidsos.emergencydatasdk.data.network_response.SessionToken
import com.rapidsos.emergencydatasdk.data.profile.Profile
import com.rapidsos.emergencydatasdk.internal.api.EmgDataApi
import com.rapidsos.emergencydatasdk.internal.helpers.dependency_injection.Injector
import com.rapidsos.emergencydatasdk.internal.helpers.network.ResponseChecker
import com.rapidsos.emergencydatasdk.internal.helpers.network.SessionTokenVerifier
import com.rapidsos.emergencydatasdk.sdk.SdkInitiatedValidator
import io.reactivex.Maybe
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Provides profile information for a user
 *
 * Example:
 *
 * ```
 *     private fun getProfileInformation() {
 *      val profileProvider = ProfileProvider()
 *      profileProvider.getPersonalInfo(sessionToken)
 *              .subscribe(object : MaybeObserver<Profile?> {
 *                  override fun onSubscribe(d: Disposable) {
 *                       // Called as soon as the method is called. The disposable is used to stop
 *                       // the request at any given time if needed by calling d.dispose()
 *                  }
 *
 *                  override fun onSuccess(profile: Profile) {
 *                      // Called on success. Returns a `Profile` object as confirmation that the
 *                      // request was successful.
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
 * @see Profile
 * @see SessionToken
 */
class ProfileProvider {

    @Inject
    protected lateinit var api: EmgDataApi

    init {
        Injector.component?.inject(this)
    }

    /**
     * Provides personal information for a user
     *
     * @param sessionToken the session token required to make the request
     * @return an [Observer] that gets notified of an error or success. On success a [Profile]
     * is returned
     */
    fun getPersonalInfo(sessionToken: SessionToken): Maybe<Profile?> {
        return api.getPersonalInfo("Bearer ${sessionToken.accessToken}")
                .subscribeOn(Schedulers.io())
                .filter {
                    SdkInitiatedValidator.checkIfSDKIsInitialized("Error " +
                            "getting personal information. Please make sure the SDK has " +
                            "been initialized.")
                }
                .filter {
                    SessionTokenVerifier.checkIfTokenIsExpired(sessionToken, "Cannot " +
                            "get personal information. The session token provided is either " +
                            "invalid or expired.")
                }
                .filter { ResponseChecker.checkIfItsASuccessfulResponse(it) }
                .timeout(15, TimeUnit.SECONDS)
                .map { response -> response.body() }
                .observeOn(AndroidSchedulers.mainThread())
    }

}