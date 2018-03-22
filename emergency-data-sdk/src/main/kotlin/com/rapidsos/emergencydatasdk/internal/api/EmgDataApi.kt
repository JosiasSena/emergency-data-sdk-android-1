package com.rapidsos.emergencydatasdk.internal.api

import com.google.gson.JsonObject
import com.rapidsos.emergencydatasdk.auth.login.SessionManager
import com.rapidsos.emergencydatasdk.auth.password.PasswordHelper
import com.rapidsos.emergencydatasdk.auth.register.RegistrationController
import com.rapidsos.emergencydatasdk.data.devices.Device
import com.rapidsos.emergencydatasdk.data.location.Location
import com.rapidsos.emergencydatasdk.data.network_response.CallerId
import com.rapidsos.emergencydatasdk.data.network_response.OauthResponse
import com.rapidsos.emergencydatasdk.data.network_response.SessionToken
import com.rapidsos.emergencydatasdk.data.profile.Profile
import com.rapidsos.emergencydatasdk.devices.DeviceCreator
import com.rapidsos.emergencydatasdk.devices.DeviceProvider
import com.rapidsos.emergencydatasdk.devices.DeviceRemover
import com.rapidsos.emergencydatasdk.devices.DeviceUpdater
import com.rapidsos.emergencydatasdk.locations.LocationCreator
import com.rapidsos.emergencydatasdk.locations.LocationProvider
import com.rapidsos.emergencydatasdk.locations.LocationRemover
import com.rapidsos.emergencydatasdk.locations.LocationUpdater
import com.rapidsos.emergencydatasdk.pin.provider.PinProvider
import com.rapidsos.emergencydatasdk.pin.validator.PinValidator
import com.rapidsos.emergencydatasdk.profile.ProfileProvider
import com.rapidsos.emergencydatasdk.profile.ProfileUpdater
import io.reactivex.Maybe
import io.reactivex.Single
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

/**
 * The main interface that provides access to all of the RAIN network requests. This interface
 * should not be used directly, instead the wrapper classes should be used instead.
 *
 * Authentication:
 * - For getting a session token (logging in), the [SessionManager] should be used.
 * - For resetting your password, the [PasswordHelper] should be used.
 * - For registering a new user, the [RegistrationController] should be used.
 *
 * Profile:
 * - For getting profile information, the [ProfileProvider] should be used
 * - For updating profile information, the [ProfileUpdater] should be used
 *
 *
 * Location:
 * - For adding new locations, the [LocationCreator] should be used
 * - For getting existing locations, the [LocationProvider] should be used
 * - For removing existing locations, the [LocationRemover] should be used
 * - For updating existing locations, the [LocationUpdater] should be used
 *
 * Pin verification:
 * - For getting a pin to validate a users device use the [PinProvider]
 * - For validating a pin received use the [PinValidator]
 *
 * @author Josias Sena
 * @see SessionManager
 * @see PasswordHelper
 * @see RegistrationController
 * @see ProfileProvider
 * @see ProfileUpdater
 * @see LocationCreator
 * @see LocationProvider
 * @see LocationRemover
 * @see LocationUpdater
 * @see PinProvider
 * @see PinProvider
 * @see DeviceCreator
 * @see DeviceProvider
 * @see DeviceRemover
 * @see DeviceUpdater
 * @see Single
 * @see Maybe
 */
interface EmgDataApi {

    //region Authorization
    @FormUrlEncoded
    @POST("oauth/token")
    fun getAccessToken(@Header("Authorization") credentials: String,
                       @Field(value = "grant_type", encoded = true) grantType: String = "client_credentials"):
            Single<Response<OauthResponse>>

    @FormUrlEncoded
    @POST("oauth/token")
    fun getSessionToken(@Field(value = "grant_type", encoded = true) grantType: String = "password",
                        @Field(value = "client_id", encoded = true) clientId: String,
                        @Field(value = "client_secret", encoded = true) clientSecret: String,
                        @Field(value = "username", encoded = true) username: String?,
                        @Field(value = "password", encoded = true) password: String?):
            Single<Response<SessionToken>>

    @POST("v1/rain/user")
    fun register(@Header("Authorization") accessToken: String,
                 @Body user: JsonObject): Single<Response<ResponseBody>>

    @POST("v1/rain/password-reset")
    fun resetPassword(@Header("Authorization") accessToken: String,
                      @Body email: JsonObject): Single<Response<ResponseBody>>

    //endregion

    //region CallerID
    @POST("v1/rain/caller-ids")
    fun createCallerId(@Header("Authorization") accessToken: String,
                       @Body user: JsonObject): Single<Response<CallerId>>

    @PATCH("v1/rain/caller-ids")
    fun validateCallerId(@Header("Authorization") accessToken: String,
                         @Body user: JsonObject): Single<Response<CallerId>>
    //endregion

    //region Personal Information
    @GET("v1/rain/personal-info")
    fun getPersonalInfo(@Header("Authorization") accessToken: String):
            Single<Response<Profile>>

    @PATCH("v1/rain/personal-info")
    fun updatePersonalInfo(@Header("Authorization") accessToken: String,
                           @Body profile: Profile): Single<Response<Profile>>
    //endregion

    //region Location
    @GET("v1/rain/locations")
    fun getLocations(@Header("Authorization") accessToken: String):
            Maybe<Response<ArrayList<Location>>>

    @GET("v1/rain/locations/{locationId}")
    fun getLocationById(@Header("Authorization") accessToken: String,
                        @Path("locationId") locationId: Int): Single<Response<Location>>

    @POST("v1/rain/locations")
    fun addNewLocation(@Header("Authorization") accessToken: String,
                       @Body location: Location): Single<Response<Location>>

    @DELETE("v1/rain/locations/{locationId}")
    fun deleteLocationById(@Header("Authorization") accessToken: String,
                           @Path("locationId") locationId: Int): Single<Response<Location>>

    @PATCH("v1/rain/locations/{locationId}")
    fun updateLocationById(@Header("Authorization") accessToken: String,
                           @Path("locationId") locationId: Int,
                           @Body location: Location): Single<Response<Location>>

    @PUT("v1/rain/locations/{locationId}")
    fun updateAllFieldsByLocationId(@Header("Authorization") accessToken: String,
                                    @Path("locationId") locationId: Int,
                                    @Body location: Location): Single<Response<Location>>
    //endregion

    //region Devices
    @GET("v1/rain/devices")
    fun getDevices(@Header("Authorization") accessToken: String):
            Maybe<Response<ArrayList<Device>>>

    @GET("v1/rain/devices/{deviceId}")
    fun getDeviceById(@Header("Authorization") accessToken: String,
                      @Path("deviceId") id: Int): Single<Response<Device>>

    @POST("v1/rain/devices")
    fun createNewDevice(@Header("Authorization") accessToken: String,
                        @Body device: Device): Single<Response<Device>>

    @DELETE("v1/rain/devices/{deviceId}")
    fun deleteDevice(@Header("Authorization") accessToken: String,
                     @Path("deviceId") deviceId: Int): Single<Response<Device>>

    @PATCH("v1/rain/devices/{deviceId}")
    fun updateDeviceById(@Header("Authorization") accessToken: String,
                         @Path("deviceId") deviceId: Int,
                         @Body device: Device): Single<Response<Device>>
    //endregion
}