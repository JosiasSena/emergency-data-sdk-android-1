package com.rapidsos.emergencydatasdk.sdk

import android.content.Context
import com.rapidsos.emergencydatasdk.internal.helpers.dependency_injection.Injector
import com.rapidsos.emergencydatasdk.internal.preferences.EmgDataPreferences
import javax.inject.Inject

/**
 * The starting point for using the EMG Data SDK. The [initialize] method must be the first thing
 * called before proceeding with any other SDK usages.
 *
 * @author Josias Sena
 */
class EraSdk constructor(context: Context) {

    @Inject
    protected lateinit var preferences: EmgDataPreferences

    init {
        Injector.init(context)
        Injector.component?.inject(this)
    }

    /**
     * Initialize the SDK. This method should be the first to be called before doing
     * anything else with the SDK. If this is not the first method called, most of the
     * requests will fail.
     *
     * @param host the host to use use for all network requests
     * @param clientId the client id provided by RapidSOS
     * @param clientSecret the client secret provided by RapidSOS
     */
    fun initialize(host: String, clientId: String, clientSecret: String) {
        preferences.setHost(host)
        preferences.setClientId(clientId)
        preferences.setClientSecret(clientSecret)

        SdkInitiatedValidator.isSdkInit = true
    }
}