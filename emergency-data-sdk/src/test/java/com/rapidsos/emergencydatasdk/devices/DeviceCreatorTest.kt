package com.rapidsos.emergencydatasdk.devices

import com.google.gson.Gson
import com.rapidsos.emergencydatasdk.ACCESS_TOKEN_INVALID_RESPONSE
import com.rapidsos.emergencydatasdk.BaseUnitTest
import com.rapidsos.emergencydatasdk.DEVICES_SUCCESS_RESPONSE
import com.rapidsos.emergencydatasdk.DEVICE_SUCCESS_RESPONSE
import com.rapidsos.emergencydatasdk.data.devices.Device
import com.rapidsos.emergencydatasdk.data.network_response.SessionToken
import com.rapidsos.emergencydatasdk.internal.exceptions.EraSdkUnInitializedException
import com.rapidsos.emergencydatasdk.internal.exceptions.SessionTokenExpiredException
import com.rapidsos.emergencydatasdk.internal.helpers.dependency_injection.Injector
import com.rapidsos.emergencydatasdk.internal.helpers.network.OK_RESPONSE_CODE
import com.rapidsos.emergencydatasdk.internal.helpers.network.UNAUTHORIZED_RESPONSE_CODE
import com.rapidsos.emergencydatasdk.internal.preferences.EmgDataPreferences
import com.rapidsos.emergencydatasdk.rules.MockWebServerRule
import com.rapidsos.emergencydatasdk.rules.RxRule
import com.rapidsos.emergencydatasdk.sdk.SdkInitiatedValidator
import io.appflate.restmock.RESTMockServer
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
class DeviceCreatorTest : BaseUnitTest() {

    private val createDeviceTokenMatcher = RequestMatchers.pathEndsWith("v1/rain/devices")

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
    fun testCreateNewDeviceErrorSdkNotInit() {
        SdkInitiatedValidator.isSdkInit = false

        val mockResponse = MockResponse()
                .setBody(DEVICE_SUCCESS_RESPONSE)
                .setResponseCode(OK_RESPONSE_CODE)

        RESTMockServer.whenPOST(createDeviceTokenMatcher).thenReturn(mockResponse)

        val testObserver = TestObserver.create<Device>()
        val sessionToken = Mockito.mock(SessionToken::class.java)

        val device = Gson().fromJson(DEVICE_SUCCESS_RESPONSE, Device::class.java)
        DeviceCreator().create(sessionToken, device).subscribe(testObserver)

        with(testObserver) {
            awaitTerminalEvent()
            assertError(EraSdkUnInitializedException::class.java)
            assertErrorMessage("Error adding new device. Please make sure the SDK has been " +
                    "initialized.")
            assertNoValues()
        }
    }

    @Test
    fun testErrorAddingNewDeviceBecauseOfExpiredToken() {
        SdkInitiatedValidator.isSdkInit = true

        val mockResponse = MockResponse()
                .setBody(DEVICES_SUCCESS_RESPONSE)
                .setResponseCode(UNAUTHORIZED_RESPONSE_CODE)

        RESTMockServer.whenPOST(createDeviceTokenMatcher).thenReturn(mockResponse)

        val testObserver = TestObserver.create<Device>()
        val expiredSessionToken = SessionToken().apply {
            expiresIn = "$ONE_HOUR_IN_SECONDS"
            issuedAt = "${System.currentTimeMillis().minus(ONE_DAY_MILLI_SECONDS)}"
        }

        val device = Gson().fromJson(DEVICE_SUCCESS_RESPONSE, Device::class.java)
        DeviceCreator().create(expiredSessionToken, device).subscribe(testObserver)

        with(testObserver) {
            awaitTerminalEvent()
            assertError(SessionTokenExpiredException::class.java)
            assertErrorMessage("Cannot add new device. The session token provided is either " +
                    "invalid or expired.")
            assertNoValues()
        }
    }

    @Test
    fun testErrorAddingNewDeviceBecauseOfInvalidAccessToken() {
        SdkInitiatedValidator.isSdkInit = true

        val mockResponse = MockResponse()
                .setBody(ACCESS_TOKEN_INVALID_RESPONSE)
                .setResponseCode(UNAUTHORIZED_RESPONSE_CODE)

        RESTMockServer.whenPOST(createDeviceTokenMatcher).thenReturn(mockResponse)

        val testObserver = TestObserver.create<Device>()
        val invalidSessionToken = SessionToken().apply {
            expiresIn = "$ONE_HOUR_IN_SECONDS"
            issuedAt = "${System.currentTimeMillis().plus(ONE_DAY_MILLI_SECONDS)}"
        }

        val device = Gson().fromJson(DEVICE_SUCCESS_RESPONSE, Device::class.java)
        DeviceCreator().create(invalidSessionToken, device).subscribe(testObserver)

        with(testObserver) {
            awaitTerminalEvent()
            assertNoValues()
            assertError { throwable: Throwable ->
                throwable.message?.contains(ACCESS_TOKEN_INVALID_RESPONSE) as Boolean
            }
        }
    }

    @Test
    fun testSuccessAddingNewDevice() {
        SdkInitiatedValidator.isSdkInit = true

        val mockResponse = MockResponse()
                .setBody(DEVICE_SUCCESS_RESPONSE)
                .setResponseCode(OK_RESPONSE_CODE)

        RESTMockServer.whenPOST(createDeviceTokenMatcher).thenReturn(mockResponse)

        val testObserver = TestObserver.create<Device>()
        val sessionToken = SessionToken().apply {
            expiresIn = "$ONE_HOUR_IN_SECONDS"
            issuedAt = "${System.currentTimeMillis().plus(ONE_DAY_MILLI_SECONDS)}"
        }

        val device = Gson().fromJson(DEVICE_SUCCESS_RESPONSE, Device::class.java)
        DeviceCreator().create(sessionToken, device).subscribe(testObserver)

        with(testObserver) {
            awaitTerminalEvent()
            assertNoErrors()
            assertValueCount(1)
            assertValue(device)
        }
    }

    @Test
    fun testErrorAddingNewDeviceBecauseOfTimeout() {
        SdkInitiatedValidator.isSdkInit = true

        val mockResponse = MockResponse()
                .setBody(DEVICE_SUCCESS_RESPONSE)
                .setResponseCode(OK_RESPONSE_CODE)

        RESTMockServer.whenPOST(createDeviceTokenMatcher).thenReturn(mockResponse)

        val testObserver = TestObserver.create<Device>()
        val sessionToken = SessionToken().apply {
            expiresIn = "$ONE_HOUR_IN_SECONDS"
            issuedAt = "${System.currentTimeMillis().plus(ONE_DAY_MILLI_SECONDS)}"
        }

        val device = Gson().fromJson(DEVICE_SUCCESS_RESPONSE, Device::class.java)
        DeviceCreator().create(sessionToken, device).subscribe(testObserver)

        rxRule.testScheduler.advanceTimeBy(15, TimeUnit.SECONDS)

        with(testObserver) {
            awaitTerminalEvent()
            assertError(TimeoutException::class.java)
            assertNoValues()
            assertNotComplete()
        }
    }

    @Test
    fun testSuccessAddingNewDeviceRightBeforeTimeout() {
        SdkInitiatedValidator.isSdkInit = true

        val mockResponse = MockResponse()
                .setBody(DEVICE_SUCCESS_RESPONSE)
                .setResponseCode(OK_RESPONSE_CODE)

        RESTMockServer.whenPOST(createDeviceTokenMatcher).thenReturn(mockResponse)

        val testObserver = TestObserver.create<Device>()
        val sessionToken = SessionToken().apply {
            expiresIn = "$ONE_HOUR_IN_SECONDS"
            issuedAt = "${System.currentTimeMillis().plus(ONE_DAY_MILLI_SECONDS)}"
        }

        val device = Gson().fromJson(DEVICE_SUCCESS_RESPONSE, Device::class.java)
        DeviceCreator().create(sessionToken, device).subscribe(testObserver)

        rxRule.testScheduler.advanceTimeBy(14, TimeUnit.SECONDS)

        with(testObserver) {
            awaitTerminalEvent()
            assertNoErrors()
            assertValueCount(1)
            assertValue(device)
        }
    }
}