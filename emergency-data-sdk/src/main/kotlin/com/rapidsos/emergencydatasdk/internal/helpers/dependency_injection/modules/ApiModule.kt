package com.rapidsos.emergencydatasdk.internal.helpers.dependency_injection.modules

import android.content.Context
import com.rapidsos.emergencydatasdk.internal.api.ApiBuilder
import com.rapidsos.emergencydatasdk.internal.api.EmgDataApi
import com.rapidsos.emergencydatasdk.internal.api.RetrofitConfigurations
import com.rapidsos.emergencydatasdk.internal.preferences.EmgDataPreferences
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import javax.inject.Inject

/**
 * @author Josias Sena
 */
@Module
internal open class ApiModule(private val context: Context) {

    @Provides
    @Inject
    open fun providesEmgDataApi(preferences: EmgDataPreferences): EmgDataApi {
        val cache = Cache(context.cacheDir, (12 * 1024 * 1024).toLong())
        val configurations = RetrofitConfigurations(cache)
        return ApiBuilder(configurations).buildApi(preferences.getHost())
    }

}