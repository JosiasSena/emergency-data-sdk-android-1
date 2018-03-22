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
 * Updates a users profile information. If the profile does not exists, it will be created instead.
 *
 * Example:
 *
 * ```
 * // Create `PhoneNumber` object
 *  private val phoneNumber: PhoneNumber
 *      get() {
 *          val phoneNumbers = ArrayList<PhoneNumberValue>()
 *          val phoneNumberValue = PhoneNumberValue("Cell", "+15555555555")
 *          phoneNumbers.add(phoneNumberValue)
 *
 *          return PhoneNumber(phoneNumbers)
 *      }
 *
 *  // Create an `Email` object
 *  private val email: Email
 *      get() {
 *          val emails = ArrayList<EmailValue>()
 *          val emailValue = EmailValue("Personal", "email@email.com")
 *          emails.add(emailValue)
 *
 *          return Email(emails)
 *      }
 *
 *  private fun createProfile() {
 *      val profileUpdater = ProfileUpdater()
 *      val profile = Profile().apply {
 *          email = email
 *          phoneNumber = phoneNumber
 *      }
 *
 *      profileUpdater.updatePersonalInfo(sessionToken, profile)
 *              .subscribe(object : MaybeObserver<Profile?> {
 *                  override fun onSubscribe(d: Disposable) {
 *                       // Called as soon as the method is called. The disposable is used to stop
 *                       // the request at any given time if needed by calling d.dispose()
 *                  }
 *
 *                  override fun onSuccess(profile: Profile) {
 *                      // Called on success. Returns a `Profile` object as confirmation that the
 *                      // request was successful. The profile will also have an auto generated
 *                      // unique id.
 *                  }

 *                  override fun onError(e: Throwable) {
 *                      // Called when an error occurs
 *                  }
 *
 *                  override fun onComplete() {
 *                      // Called once the deferred computation completes normally.
 *                  }
 *              })
 *  }
 *
 * ```
 *
 * @author Josias Sena
 * @see Profile
 * @see SessionToken
 */
class ProfileUpdater {

    @Inject
    protected lateinit var api: EmgDataApi

    init {
        Injector.component?.inject(this)
    }

    /**
     * Updates a users personal information. If the profile does not exist, a new profile with
     * the provided information will be created instead.
     *
     * @param sessionToken the session token required to make the request
     * @param profileToUpdate the profile information to update
     * @return an [Observer] that gets notified of an error or success
     */
    fun updatePersonalInfo(sessionToken: SessionToken, profileToUpdate: Profile): Maybe<Profile?> {
        return api.updatePersonalInfo("Bearer " + sessionToken.accessToken, profileToUpdate)
                .subscribeOn(Schedulers.io())
                .filter {
                    SdkInitiatedValidator.checkIfSDKIsInitialized("Error " +
                            "updating personal information. Please make sure the SDK has " +
                            "been initialized.")
                }
                .filter {
                    SessionTokenVerifier.checkIfTokenIsExpired(sessionToken, "Cannot update " +
                            "personal information. The session token provided is either " +
                            "invalid or expired.")
                }
                .filter { ResponseChecker.checkIfItsASuccessfulResponse(it) }
                .timeout(15, TimeUnit.SECONDS)
                .map { response -> response.body() }
                .observeOn(AndroidSchedulers.mainThread())
    }

}