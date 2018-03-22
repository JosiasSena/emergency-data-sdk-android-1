package com.rapidsos.emergencydatasdk.internal.preferences

import com.github.rtoshiro.secure.SecureSharedPreferences

/**
 * @author Josias Sena
 */
class EmgDataPreferences constructor(private val preferences: SecureSharedPreferences) {

    fun getHost(): String = preferences.getString(KEY_HOST, "")
    fun setHost(host: String) = preferences.edit().putString(KEY_HOST, host).apply()

    fun getClientId(): String = preferences.getString(KEY_CLIENT_ID, "")
    fun setClientId(clientId: String) {
        preferences.edit().putString(KEY_CLIENT_ID, clientId).apply()
    }

    fun getClientSecret(): String = preferences.getString(KEY_CLIENT_SECRET, "")
    fun setClientSecret(clientSecret: String) {
        preferences.edit().putString(KEY_CLIENT_SECRET, clientSecret).apply()
    }

    companion object {
        const val KEY_CLIENT_ID = "key_client_id"
        const val KEY_CLIENT_SECRET = "key_client_secret"
        const val KEY_HOST = "key_host"
    }
}