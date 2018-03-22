package com.rapidsos.emergencydatasdk.locations

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.rapidsos.emergencydatasdk.*
import com.rapidsos.emergencydatasdk.data.location.Location
import com.rapidsos.emergencydatasdk.data.network_response.SessionToken
import com.rapidsos.emergencydatasdk.internal.exceptions.EraSdkUnInitializedException
import com.rapidsos.emergencydatasdk.internal.exceptions.SessionTokenExpiredException
import com.rapidsos.emergencydatasdk.internal.helpers.dependency_injection.Injector
import com.rapidsos.emergencydatasdk.internal.helpers.network.NOT_FOUND_REQUEST_CODE
import com.rapidsos.emergencydatasdk.internal.helpers.network.OK_RESPONSE_CODE
import com.rapidsos.emergencydatasdk.internal.helpers.network.UNAUTHORIZED_RESPONSE_CODE
import com.rapidsos.emergencydatasdk.internal.preferences.EmgDataPreferences
import com.rapidsos.emergencydatasdk.rules.MockWebServerRule
import com.rapidsos.emergencydatasdk.rules.RxRule
import com.rapidsos.emergencydatasdk.sdk.SdkInitiatedValidator
import io.appflate.restmock.RESTMockServer
import io.appflate.restmock.RequestsVerifier
import io.appflate.restmock.utils.RequestMatchers
import io.reactivex.observers.TestObserver
import okhttp3.mockwebserver.MockResponse
import org.junit.Before
import org.junit.ClassRule
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException
import javax.inject.Inject

/**
 * @author Josias Sena
 */
class LocationProviderTest : BaseUnitTest() {

    private val getLocationsTokenMatcher = RequestMatchers.pathEndsWith("v1/rain/locations")
    private val getLocationsByIdTokenMatcher = RequestMatchers
            .pathEndsWith("v1/rain/locations/$TEST_LOCATION_ID")

    @Inject
    internal lateinit var preferences: EmgDataPreferences

    @Rule
    @JvmField
    var mockWebServerRule = MockWebServerRule()

    companion object {

        @ClassRule
        @JvmField
        var rxRule = RxRule()

        private const val ONE_DAY_MILLI_SECONDS = 86400000
        private const val ONE_HOUR_IN_SECONDS = 3600

        // The '1' at the end of this matcher can be the id of any request.
        // For testing purposes it will be 1
        private const val TEST_LOCATION_ID = 1
    }

    @Before
    fun setUpClass() {
        super.setUp()
        Injector.component = component
        component.inject(this)

        Mockito.`when`(preferences.getHost()).thenReturn(RESTMockServer.getUrl())
        Mockito.`when`(preferences.getClientId()).thenReturn("abc123")
        Mockito.`when`(preferences.getClientSecret()).thenReturn("123abc")
    }

    @Test
    fun testGetLocationsErrorSdkNotInit() {
        SdkInitiatedValidator.isSdkInit = false

        val mockResponse = MockResponse()
                .setBody(LOCATIONS_SUCCESS_RESPONSE)
                .setResponseCode(OK_RESPONSE_CODE)

        RESTMockServer.whenGET(getLocationsTokenMatcher).thenReturn(mockResponse)

        val testObserver = TestObserver.create<ArrayList<Location>>()
        val sessionToken = Mockito.mock(SessionToken::class.java)

        LocationProvider().getAll(sessionToken).subscribe(testObserver)

        RequestsVerifier.verifyGET(getLocationsByIdTokenMatcher).never()

        with(testObserver) {
            awaitTerminalEvent()
            assertError(EraSdkUnInitializedException::class.java)
            assertErrorMessage("Error getting locations. Please make sure the SDK has been " +
                    "initialized.")
            assertNoValues()
        }
    }

    @Test
    fun testErrorGettingLocationsBecauseOfExpiredToken() {
        SdkInitiatedValidator.isSdkInit = true

        val mockResponse = MockResponse()
                .setBody(LOCATIONS_SUCCESS_RESPONSE)
                .setResponseCode(UNAUTHORIZED_RESPONSE_CODE)

        RESTMockServer.whenGET(getLocationsTokenMatcher).thenReturn(mockResponse)

        val testObserver = TestObserver.create<ArrayList<Location>>()

        val expiredSessionToken = SessionToken().apply {
            expiresIn = "$ONE_HOUR_IN_SECONDS"
            issuedAt = "${System.currentTimeMillis().minus(ONE_DAY_MILLI_SECONDS)}"
        }

        LocationProvider().getAll(expiredSessionToken).subscribe(testObserver)

        RequestsVerifier.verifyGET(getLocationsByIdTokenMatcher).never()

        with(testObserver) {
            awaitTerminalEvent()
            assertError(SessionTokenExpiredException::class.java)
            assertErrorMessage("Cannot get locations. The session token provided is either " +
                    "invalid or expired.")
            assertNoValues()
        }
    }

    @Test
    fun testErrorGettingLocationsBecauseOfInvalidAccessToken() {
        SdkInitiatedValidator.isSdkInit = true

        val mockResponse = MockResponse()
                .setBody(ACCESS_TOKEN_INVALID_RESPONSE)
                .setResponseCode(UNAUTHORIZED_RESPONSE_CODE)

        RESTMockServer.whenGET(getLocationsTokenMatcher).thenReturn(mockResponse)

        val testObserver = TestObserver.create<ArrayList<Location>>()
        val invalidSessionToken = SessionToken().apply {
            expiresIn = "$ONE_HOUR_IN_SECONDS"
            issuedAt = "${System.currentTimeMillis().plus(ONE_DAY_MILLI_SECONDS)}"
        }

        LocationProvider().getAll(invalidSessionToken).subscribe(testObserver)

        RequestsVerifier.verifyGET(getLocationsByIdTokenMatcher).never()

        with(testObserver) {
            awaitTerminalEvent()
            assertNoValues()
            assertError { throwable: Throwable ->
                throwable.message?.contains(ACCESS_TOKEN_INVALID_RESPONSE) as Boolean
            }
        }
    }

    @Test
    fun testSuccessGettingLocationsNotFound() {
        SdkInitiatedValidator.isSdkInit = true

        val mockResponse = MockResponse()
                .setBody("[]")
                .setResponseCode(OK_RESPONSE_CODE)

        RESTMockServer.whenGET(getLocationsTokenMatcher).thenReturn(mockResponse)

        val testObserver = TestObserver.create<ArrayList<Location>>()
        val sessionToken = SessionToken().apply {
            expiresIn = "$ONE_HOUR_IN_SECONDS"
            issuedAt = "${System.currentTimeMillis().plus(ONE_DAY_MILLI_SECONDS)}"
        }

        LocationProvider().getAll(sessionToken).subscribe(testObserver)

        RequestsVerifier.verifyGET(getLocationsByIdTokenMatcher).never()

        with(testObserver) {
            awaitTerminalEvent()
            assertNoErrors()
            assertValue { value: ArrayList<Location> ->
                value.isEmpty() // an empty array is returned
            }
        }
    }

    @Test
    fun testSuccessGettingLocations() {
        SdkInitiatedValidator.isSdkInit = true

        val mockResponse = MockResponse()
                .setBody(LOCATIONS_SUCCESS_RESPONSE)
                .setResponseCode(OK_RESPONSE_CODE)

        RESTMockServer.whenGET(getLocationsTokenMatcher).thenReturn(mockResponse)

        val testObserver = TestObserver.create<ArrayList<Location>>()
        val sessionToken = SessionToken().apply {
            expiresIn = "$ONE_HOUR_IN_SECONDS"
            issuedAt = "${System.currentTimeMillis().plus(ONE_DAY_MILLI_SECONDS)}"
        }

        LocationProvider().getAll(sessionToken).subscribe(testObserver)

        val listType = object : TypeToken<ArrayList<Location>>() {}.type
        val locations: ArrayList<Location> = Gson().fromJson(LOCATIONS_SUCCESS_RESPONSE, listType)

        RequestsVerifier.verifyGET(getLocationsByIdTokenMatcher).never()

        with(testObserver) {
            awaitTerminalEvent()
            assertNoErrors()
            assertValueCount(1)
            assertValue(locations)
        }
    }

    @Test
    fun testGettingLocationsErrorBecauseOfTimeout() {
        SdkInitiatedValidator.isSdkInit = true

        val mockResponse = MockResponse()
                .setBody(LOCATIONS_SUCCESS_RESPONSE)
                .setResponseCode(OK_RESPONSE_CODE)

        RESTMockServer.whenGET(getLocationsTokenMatcher).thenReturn(mockResponse)

        val testObserver = TestObserver.create<ArrayList<Location>>()
        val sessionToken = SessionToken().apply {
            expiresIn = "$ONE_HOUR_IN_SECONDS"
            issuedAt = "${System.currentTimeMillis().plus(ONE_DAY_MILLI_SECONDS)}"
        }

        LocationProvider().getAll(sessionToken).subscribe(testObserver)

        rxRule.testScheduler.advanceTimeBy(15, TimeUnit.SECONDS)

        RequestsVerifier.verifyGET(getLocationsByIdTokenMatcher).never()

        with(testObserver) {
            awaitTerminalEvent()
            assertError(TimeoutException::class.java)
            assertNoValues()
            assertNotComplete()
        }
    }

    @Test
    fun testSuccessGettingLocationsRightBeforeTimeout() {
        SdkInitiatedValidator.isSdkInit = true

        val mockResponse = MockResponse()
                .setBody(LOCATIONS_SUCCESS_RESPONSE)
                .setResponseCode(OK_RESPONSE_CODE)

        RESTMockServer.whenGET(getLocationsTokenMatcher).thenReturn(mockResponse)

        val testObserver = TestObserver.create<ArrayList<Location>>()
        val sessionToken = SessionToken().apply {
            expiresIn = "$ONE_HOUR_IN_SECONDS"
            issuedAt = "${System.currentTimeMillis().plus(ONE_DAY_MILLI_SECONDS)}"
        }

        LocationProvider().getAll(sessionToken).subscribe(testObserver)

        val listType = object : TypeToken<ArrayList<Location>>() {}.type
        val locations: ArrayList<Location> = Gson().fromJson(LOCATIONS_SUCCESS_RESPONSE, listType)

        rxRule.testScheduler.advanceTimeBy(14, TimeUnit.SECONDS)

        RequestsVerifier.verifyGET(getLocationsByIdTokenMatcher).never()

        with(testObserver) {
            awaitTerminalEvent()
            assertNoErrors()
            assertValueCount(1)
            assertValue(locations)
        }
    }

    @Test
    fun testGetLocationByIdErrorSdkNotInit() {
        SdkInitiatedValidator.isSdkInit = false

        val mockResponse = MockResponse()
                .setBody(LOCATION_SUCCESS_RESPONSE)
                .setResponseCode(OK_RESPONSE_CODE)

        RESTMockServer.whenGET(getLocationsByIdTokenMatcher).thenReturn(mockResponse)

        val testObserver = TestObserver.create<Location>()
        val sessionToken = Mockito.mock(SessionToken::class.java)

        LocationProvider().getById(sessionToken, TEST_LOCATION_ID).subscribe(testObserver)

        RequestsVerifier.verifyGET(getLocationsTokenMatcher).never()

        with(testObserver) {
            awaitTerminalEvent()
            assertError(EraSdkUnInitializedException::class.java)
            assertErrorMessage("Error getting location with id=$TEST_LOCATION_ID. Please make " +
                    "sure the SDK has been initialized.")
            assertNoValues()
        }
    }

    @Test
    fun testErrorGetLocationByIdBecauseOfExpiredToken() {
        SdkInitiatedValidator.isSdkInit = true

        val mockResponse = MockResponse()
                .setBody(LOCATION_SUCCESS_RESPONSE)
                .setResponseCode(UNAUTHORIZED_RESPONSE_CODE)

        RESTMockServer.whenGET(getLocationsByIdTokenMatcher).thenReturn(mockResponse)

        val testObserver = TestObserver.create<Location>()
        val expiredSessionToken = SessionToken().apply {
            expiresIn = "$ONE_HOUR_IN_SECONDS"
            issuedAt = "${System.currentTimeMillis().minus(ONE_DAY_MILLI_SECONDS)}"
        }

        LocationProvider().getById(expiredSessionToken, TEST_LOCATION_ID)
                .subscribe(testObserver)

        RequestsVerifier.verifyGET(getLocationsTokenMatcher).never()

        with(testObserver) {
            awaitTerminalEvent()
            assertError(SessionTokenExpiredException::class.java)
            assertErrorMessage("Cannot get location with id=$TEST_LOCATION_ID. The session token " +
                    "provided is either invalid or expired.")
            assertNoValues()
        }
    }

    @Test
    fun testErrorGetLocationByIdBecauseOfInvalidAccessToken() {
        SdkInitiatedValidator.isSdkInit = true

        val mockResponse = MockResponse()
                .setBody(ACCESS_TOKEN_INVALID_RESPONSE)
                .setResponseCode(UNAUTHORIZED_RESPONSE_CODE)

        RESTMockServer.whenGET(getLocationsByIdTokenMatcher).thenReturn(mockResponse)

        val testObserver = TestObserver.create<Location>()
        val invalidSessionToken = SessionToken().apply {
            expiresIn = "$ONE_HOUR_IN_SECONDS"
            issuedAt = "${System.currentTimeMillis().plus(ONE_DAY_MILLI_SECONDS)}"
        }

        LocationProvider().getById(invalidSessionToken, TEST_LOCATION_ID)
                .subscribe(testObserver)

        RequestsVerifier.verifyGET(getLocationsTokenMatcher).never()

        with(testObserver) {
            awaitTerminalEvent()
            assertNoValues()
            assertError { throwable: Throwable ->
                throwable.message?.contains(ACCESS_TOKEN_INVALID_RESPONSE) as Boolean
            }
        }
    }

    @Test
    fun testSuccessGetLocationByIdNotFound() {
        SdkInitiatedValidator.isSdkInit = true

        val mockResponse = MockResponse()
                .setBody(NOT_FOUND_RESPONSE)
                .setResponseCode(NOT_FOUND_REQUEST_CODE)

        RESTMockServer.whenGET(getLocationsByIdTokenMatcher).thenReturn(mockResponse)

        val testObserver = TestObserver.create<Location>()
        val sessionToken = SessionToken().apply {
            expiresIn = "$ONE_HOUR_IN_SECONDS"
            issuedAt = "${System.currentTimeMillis().plus(ONE_DAY_MILLI_SECONDS)}"
        }

        LocationProvider().getById(sessionToken, TEST_LOCATION_ID).subscribe(testObserver)

        RequestsVerifier.verifyGET(getLocationsTokenMatcher).never()

        with(testObserver) {
            awaitTerminalEvent()
            assertError { throwable: Throwable ->
                throwable.message?.contains(NOT_FOUND_RESPONSE) as Boolean
            }
            assertNoValues()
        }
    }

    @Test
    fun testSuccessGetLocationById() {
        SdkInitiatedValidator.isSdkInit = true

        val mockResponse = MockResponse()
                .setBody(LOCATION_SUCCESS_RESPONSE)
                .setResponseCode(OK_RESPONSE_CODE)

        RESTMockServer.whenGET(getLocationsByIdTokenMatcher).thenReturn(mockResponse)

        val testObserver = TestObserver.create<Location>()
        val sessionToken = SessionToken().apply {
            expiresIn = "$ONE_HOUR_IN_SECONDS"
            issuedAt = "${System.currentTimeMillis().plus(ONE_DAY_MILLI_SECONDS)}"
        }

        LocationProvider().getById(sessionToken, TEST_LOCATION_ID).subscribe(testObserver)

        val location: Location = Gson()
                .fromJson(LOCATION_SUCCESS_RESPONSE, Location::class.java)

        RequestsVerifier.verifyGET(getLocationsTokenMatcher).never()

        with(testObserver) {
            awaitTerminalEvent()
            assertNoErrors()
            assertValueCount(1)
            assertValue(location)
        }
    }

    @Test
    fun testGetLocationByIdErrorBecauseOfTimeout() {
        SdkInitiatedValidator.isSdkInit = true

        val mockResponse = MockResponse()
                .setBody(LOCATION_SUCCESS_RESPONSE)
                .setResponseCode(OK_RESPONSE_CODE)

        RESTMockServer.whenGET(getLocationsByIdTokenMatcher).thenReturn(mockResponse)

        val testObserver = TestObserver.create<Location>()
        val sessionToken = SessionToken().apply {
            expiresIn = "$ONE_HOUR_IN_SECONDS"
            issuedAt = "${System.currentTimeMillis().plus(ONE_DAY_MILLI_SECONDS)}"
        }

        LocationProvider().getById(sessionToken, TEST_LOCATION_ID).subscribe(testObserver)

        rxRule.testScheduler.advanceTimeBy(15, TimeUnit.SECONDS)

        RequestsVerifier.verifyGET(getLocationsTokenMatcher).never()

        with(testObserver) {
            awaitTerminalEvent()
            assertError(TimeoutException::class.java)
            assertNoValues()
            assertNotComplete()
        }
    }

    @Test
    fun testSuccessGetLocationByIdRightBeforeTimeout() {
        SdkInitiatedValidator.isSdkInit = true

        val mockResponse = MockResponse()
                .setBody(LOCATION_SUCCESS_RESPONSE)
                .setResponseCode(OK_RESPONSE_CODE)

        RESTMockServer.whenGET(getLocationsByIdTokenMatcher).thenReturn(mockResponse)

        val testObserver = TestObserver.create<Location>()
        val sessionToken = SessionToken().apply {
            expiresIn = "$ONE_HOUR_IN_SECONDS"
            issuedAt = "${System.currentTimeMillis().plus(ONE_DAY_MILLI_SECONDS)}"
        }

        LocationProvider().getById(sessionToken, TEST_LOCATION_ID).subscribe(testObserver)

        val location: Location = Gson()
                .fromJson(LOCATION_SUCCESS_RESPONSE, Location::class.java)

        rxRule.testScheduler.advanceTimeBy(14, TimeUnit.SECONDS)

        RequestsVerifier.verifyGET(getLocationsTokenMatcher).never()

        with(testObserver) {
            awaitTerminalEvent()
            assertNoErrors()
            assertValueCount(1)
            assertValue(location)
        }
    }

}