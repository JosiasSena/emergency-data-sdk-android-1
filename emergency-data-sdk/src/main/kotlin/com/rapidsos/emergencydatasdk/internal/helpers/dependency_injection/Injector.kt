package com.rapidsos.emergencydatasdk.internal.helpers.dependency_injection

import android.content.Context
import com.rapidsos.emergencydatasdk.internal.helpers.dependency_injection.modules.ApiModule
import com.rapidsos.emergencydatasdk.internal.helpers.dependency_injection.modules.EmgDataPreferencesModule

/**
 * @author Josias Sena
 */
internal object Injector {

    internal var component: DiComponent? = null

    fun init(context: Context) {

        if (component == null) {
            component = DaggerDiComponent.builder()
                    .apiModule(ApiModule(context))
                    .emgDataPreferencesModule(EmgDataPreferencesModule(context))
                    .build()
        }

    }
}