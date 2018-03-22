package com.rapidsos.emergencydatasdk.auth.password

import android.util.Base64
import com.google.gson.JsonObject
import com.rapidsos.emergencydatasdk.data.network_response.OauthResponse
import com.rapidsos.emergencydatasdk.internal.api.EmgDataApi
import com.rapidsos.emergencydatasdk.internal.helpers.dependency_injection.Injector
import com.rapidsos.emergencydatasdk.internal.helpers.network.ResponseChecker
import com.rapidsos.emergencydatasdk.internal.preferences.EmgDataPreferences
import com.rapidsos.emergencydatasdk.sdk.SdkInitiatedValidator
import io.reactivex.Maybe
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject

/**
 * Supplies a link to reset the users password. The link is emailed to an email address provided.
 *
 * Example:
 *
 * ```
 * private fun resetPassword() {
 *       val passwordHelper = PasswordHelper()
 *       passwordHelper.resetPassword("email@email.com")
 *               .subscribe(object : MaybeObserver<Response<ResponseBody>> {
 *                   override fun onSubscribe(d: Disposable) {
 *                       // Called as soon as the method is called. The disposable is used to stop
 *                       // the request at any given time if needed by calling d.dispose()
 *                   }
 *
 *                   override fun onSuccess(response: Response<ResponseBody>) {
 *                       // Called on success. At this point an email will be sent the the
 *                       // email provided.
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
 */
class PasswordHelper {

    @Inject
    protected lateinit var api: EmgDataApi

    @Inject
    protected lateinit var preferences: EmgDataPreferences

    init {
        Injector.component?.inject(this)
    }

    /**
     * Sends an email to the email provided with a link to reset account password.
     *
     * @param email the email address to email the password reset link to
     * @return an [Observer] that gets notified on successful reset email sent,
     * or on error.
     */
    fun resetPassword(email: String): Maybe<Response<ResponseBody>> {
        return api.getAccessToken(getBasicCredentials())
                .subscribeOn(Schedulers.io())
                .filter {
                    SdkInitiatedValidator.checkIfSDKIsInitialized("Error " +
                            "resetting password. Please make sure the SDK has been initialized.")
                }
                .filter { ResponseChecker.checkIfItsASuccessfulResponse(it) }
                .map { it.body() }
                .flatMapSingle {
                    val emailJson = JsonObject().apply {
                        addProperty("email", email)
                    }

                    api.resetPassword(getBearerCredentials(it), emailJson)
                }
                .filter { ResponseChecker.checkIfItsASuccessfulResponse(it) }
                .observeOn(AndroidSchedulers.mainThread())
    }

    private fun getBearerCredentials(response: OauthResponse) = "Bearer ${response.accessToken}"

    private fun getBasicCredentials(): String {
        return try {
            val credentials = preferences.getClientId() + ":" + preferences.getClientSecret()
            "Basic " + Base64.encodeToString(credentials.toByteArray(), Base64.NO_WRAP)
        } catch (exception: RuntimeException) {
            // This will only throw an exception during a unit test.
            "Basic error!"
        }
    }

}