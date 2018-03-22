package com.rapidsos.emergencydatasdk.internal.api

/**
 * @author Josias Sena
 */
internal class ApiBuilder(private val configurations: RetrofitConfigurations) {

    /**
     * Builds the api with the host passed in
     *
     * @param host the URL to use as the base url for each network call made
     */
    fun buildApi(host: String): EmgDataApi {
        val retrofitInstance = configurations.getRetrofitInstance(host)
        return retrofitInstance.create(EmgDataApi::class.java)
    }

}