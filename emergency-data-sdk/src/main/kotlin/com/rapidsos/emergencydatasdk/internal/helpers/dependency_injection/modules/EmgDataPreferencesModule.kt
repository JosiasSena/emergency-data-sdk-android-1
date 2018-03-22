package com.rapidsos.emergencydatasdk.internal.helpers.dependency_injection.modules

import android.content.Context
import com.github.rtoshiro.secure.SecureSharedPreferences
import com.rapidsos.emergencydatasdk.internal.preferences.EmgDataPreferences
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * @author Josias Sena
 */
@Module
internal open class EmgDataPreferencesModule(private val context: Context) {

    @Provides
    @Singleton
    open fun providesSecureSharedPreferences() = SecureSharedPreferences(context)

    @Provides
    @Singleton
    open fun providesRainPreferences(preferences: SecureSharedPreferences): EmgDataPreferences {
        return EmgDataPreferences(preferences)
    }
}