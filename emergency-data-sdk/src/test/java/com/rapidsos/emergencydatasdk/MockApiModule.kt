package com.rapidsos.emergencydatasdk

import android.content.Context
import com.rapidsos.emergencydatasdk.internal.api.ApiBuilder
import com.rapidsos.emergencydatasdk.internal.api.EmgDataApi
import com.rapidsos.emergencydatasdk.internal.api.RetrofitConfigurations
import com.rapidsos.emergencydatasdk.internal.helpers.dependency_injection.modules.ApiModule
import com.rapidsos.emergencydatasdk.internal.preferences.EmgDataPreferences
import io.appflate.restmock.RESTMockServer
import okhttp3.Cache
import org.mockito.Mockito

/**
 * @author Josias Sena
 */
internal class MockApiModule(context: Context) : ApiModule(context) {

    override fun providesEmgDataApi(preferences: EmgDataPreferences): EmgDataApi {
        val cache = Mockito.mock(Cache::class.java)
        val retrofitConfigurations = RetrofitConfigurations(cache)
        return ApiBuilder(retrofitConfigurations).buildApi(RESTMockServer.getUrl())
    }

}