package com.rapidsos.emergencydatasdk

import android.content.Context
import com.github.rtoshiro.secure.SecureSharedPreferences
import com.rapidsos.emergencydatasdk.internal.helpers.dependency_injection.modules.EmgDataPreferencesModule
import com.rapidsos.emergencydatasdk.internal.preferences.EmgDataPreferences
import org.mockito.Mockito

/**
 * @author Josias Sena
 */
internal class MockEmgDataPreferencesModule(context: Context) : EmgDataPreferencesModule(context) {

    override fun providesSecureSharedPreferences(): SecureSharedPreferences {
        val secureSharedPreferences = Mockito.mock(SecureSharedPreferences::class.java)
        val editor = Mockito.mock(SecureSharedPreferences.Editor::class.java)

        Mockito.`when`(secureSharedPreferences.edit()).thenReturn(editor)

        Mockito.`when`(editor.putString(Mockito.anyString(), Mockito.anyString())).thenReturn(editor)

        return secureSharedPreferences
    }

    override fun providesRainPreferences(preferences: SecureSharedPreferences): EmgDataPreferences {
        return Mockito.mock(EmgDataPreferences::class.java)
    }

}