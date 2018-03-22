package com.rapidsos.emergencydatasdk.auth.register

import android.util.Base64
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.rapidsos.emergencydatasdk.data.user.User
import com.rapidsos.emergencydatasdk.internal.api.EmgDataApi
import com.rapidsos.emergencydatasdk.internal.helpers.dependency_injection.Injector
import com.rapidsos.emergencydatasdk.internal.helpers.network.BAD_REQUEST_RESPONSE_CODE
import com.rapidsos.emergencydatasdk.internal.helpers.network.ResponseChecker
import com.rapidsos.emergencydatasdk.internal.helpers.network.TOO_MANY_REQUEST_CODE
import com.rapidsos.emergencydatasdk.internal.preferences.EmgDataPreferences
import com.rapidsos.emergencydatasdk.sdk.SdkInitiatedValidator
import io.reactivex.Maybe
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import retrofit2.Response
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Used for registering new additional data user accounts that will provide personal and device
 * information.
 *
 * Example:
 *
 * ```
 * private fun registerNewUser() {
 *      val registrationController = RegistrationController()
 *
 *      // username, password, and email are required
 *      val user = User(username = "username", password = "password", email = "test@email.com")
 *      registrationController.register(user).subscribe(object : MaybeObserver<User> {
 *          override fun onSubscribe(d: Disposable) {
 *              // Called as soon as the method is called. The disposable is used to stop the
 *              // request at any given time if needed by calling d.dispose()
 *          }
 *
 *          override fun onSuccess(user: User) {
 *              // Called when a successful response has been returned. The new registered User is
 *              // returned for confirmation.
 *          }
 *
 *          override fun onError(e: Throwable) {
 *              // Called when an error occurs
 *          }
 *
 *          override fun onComplete() {
 *              // Called once the deferred computation completes normally.
 *          }
 *      })
 * }
 * ```
 *
 * @author Josias Sena
 * @see User
 */
class RegistrationController {

    @Inject
    protected lateinit var api: EmgDataApi

    @Inject
    protected lateinit var preferences: EmgDataPreferences

    init {
        Injector.component?.inject(this)
    }

    companion object {
        private const val USERNAME = "username"
        private const val EMAIL = "email"
        private const val PWD = "password"
    }

    /**
     * Register the user with the EMG Data API. The users [User.username], [User.email],
     * and [User.password] are required for registration.
     *
     * On success the user that was successfully registered is returned.
     *
     * @param user the user to register
     * @return An [Observer] that gets notified when the user has been successfully
     * registered or if there is an error when registering the user. The for security
     * reasons the user returned will not contain the password provided.
     */
    fun register(user: User): Maybe<User?> {
        return api.getAccessToken(getBasicCredentials())
                .subscribeOn(Schedulers.io())
                .filter {
                    SdkInitiatedValidator.checkIfSDKIsInitialized("Error " +
                            "registering the user. Please make sure the SDK has been initialized.")
                }
                .filter { ResponseChecker.checkIfItsASuccessfulResponse(it) }
                .flatMapSingle({ response ->
                    val accessToken = response.body()?.accessToken
                    val userAsJson = JsonObject().apply {
                        addProperty(USERNAME, user.username)
                        addProperty(EMAIL, user.email)
                        addProperty(PWD, user.password)
                    }

                    api.register("Bearer $accessToken", userAsJson)
                })
                .filter { filter(it) }
                .timeout(15, TimeUnit.SECONDS)
                .map { Gson().fromJson(it.body()?.string(), User::class.java) }
                .observeOn(AndroidSchedulers.mainThread())
    }

    private fun getBasicCredentials(): String {
        return try {
            val credentials = preferences.getClientId() + ":" + preferences.getClientSecret()
            "Basic " + Base64.encodeToString(credentials.toByteArray(), Base64.NO_WRAP)
        } catch (exception: RuntimeException) {
            // This will only throw an exception during a unit test.
            "Basic error!"
        }
    }

    private fun filter(response: Response<ResponseBody>): Boolean {
        return if (response.isSuccessful) {
            true
        } else {
            val errorBody = response.errorBody()?.string()
            val message = response.message()
            val code = response.code()

            when (code) {
                BAD_REQUEST_RESPONSE_CODE -> errorBody?.let {
                    if (it.contains("already exists", ignoreCase = true)) {
                        throw Exception("User with the same username or email already exists")
                    } else {
                        throw Exception(it)
                    }
                }
                TOO_MANY_REQUEST_CODE -> throw Exception("Too many requests. Please try again later")
                else -> throw Exception("Something went wrong, please try again: $message")
            }

            false
        }
    }
}