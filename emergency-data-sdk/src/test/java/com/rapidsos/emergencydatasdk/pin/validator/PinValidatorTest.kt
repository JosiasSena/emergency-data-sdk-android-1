package com.rapidsos.emergencydatasdk.pin.validator

import com.google.gson.Gson
import com.rapidsos.emergencydatasdk.BaseUnitTest
import com.rapidsos.emergencydatasdk.CALLER_ID_SUCCESS_RESPONSE
import com.rapidsos.emergencydatasdk.VALIDATE_PIN_INVALID_PIN_RESPONSE
import com.rapidsos.emergencydatasdk.data.network_response.CallerId
import com.rapidsos.emergencydatasdk.data.network_response.SessionToken
import com.rapidsos.emergencydatasdk.internal.exceptions.EraSdkUnInitializedException
import com.rapidsos.emergencydatasdk.internal.exceptions.SessionTokenExpiredException
import com.rapidsos.emergencydatasdk.internal.helpers.dependency_injection.Injector
import com.rapidsos.emergencydatasdk.internal.helpers.network.BAD_REQUEST_RESPONSE_CODE
import com.rapidsos.emergencydatasdk.internal.helpers.network.OK_RESPONSE_CODE
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
class PinValidatorTest : BaseUnitTest() {

    private val tokenMatcher = RequestMatchers.pathContains("v1/rain/caller-ids")

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
    fun testGetSessionTokenErrorSdkNotInit() {
        SdkInitiatedValidator.isSdkInit = false

        val mockResponse = MockResponse().setBody("{}").setResponseCode(OK_RESPONSE_CODE)

        RESTMockServer.whenPATCH(tokenMatcher).thenReturn(mockResponse)

        val testObserver = TestObserver.create<CallerId>()
        val token = Mockito.mock(SessionToken::class.java)

        PinValidator().validatePin(token, "15555555555", 123456)
                .subscribe(testObserver)

        with(testObserver) {
            awaitTerminalEvent()
            assertError(EraSdkUnInitializedException::class.java)
            assertErrorMessage("Error validating pin. Please make sure the SDK has been initialized.")
            assertNoValues()
        }
    }

    @Test
    fun testErrorBecauseOfExpiredToken() {
        SdkInitiatedValidator.isSdkInit = true

        val mockResponse = MockResponse().setBody("{}").setResponseCode(OK_RESPONSE_CODE)

        RESTMockServer.whenPATCH(tokenMatcher).thenReturn(mockResponse)

        val testObserver = TestObserver.create<CallerId>()

        val expiredToken = SessionToken().apply {
            expiresIn = "$ONE_HOUR_IN_SECONDS"
            issuedAt = "${System.currentTimeMillis().minus(ONE_DAY_MILLI_SECONDS)}"
        }

        PinValidator().validatePin(expiredToken, "15555555555", 123456)
                .subscribe(testObserver)

        with(testObserver) {
            awaitTerminalEvent()
            assertError(SessionTokenExpiredException::class.java)
            assertErrorMessage("Error validating pin. The session token provided is " +
                    "either invalid or expired.")
            assertNoValues()
        }
    }

    @Test
    fun testGettingInvalidValidationCode() {
        SdkInitiatedValidator.isSdkInit = true

        val mockResponse = MockResponse()
                .setBody(VALIDATE_PIN_INVALID_PIN_RESPONSE)
                .setResponseCode(BAD_REQUEST_RESPONSE_CODE)

        RESTMockServer.whenPATCH(tokenMatcher).thenReturn(mockResponse)

        val testObserver = TestObserver.create<CallerId>()

        val token = Mockito.mock(SessionToken::class.java)
        Mockito.`when`(token.expiresIn).thenReturn("$ONE_HOUR_IN_SECONDS")
        Mockito.`when`(token.issuedAt).thenReturn("${System.currentTimeMillis().plus(ONE_DAY_MILLI_SECONDS)}")

        PinValidator().validatePin(token, "15555555555", 123456)
                .subscribe(testObserver)

        with(testObserver) {
            awaitTerminalEvent()
            assertErrorMessage("Validation code is invalid.")
            assertNoValues()
        }
    }

    @Test
    fun testGettingValidValidationCode() {
        SdkInitiatedValidator.isSdkInit = true

        val mockResponse = MockResponse()
                .setBody(CALLER_ID_SUCCESS_RESPONSE)
                .setResponseCode(OK_RESPONSE_CODE)

        RESTMockServer.whenPATCH(tokenMatcher).thenReturn(mockResponse)

        val testObserver = TestObserver.create<CallerId>()

        val token = Mockito.mock(SessionToken::class.java)
        Mockito.`when`(token.expiresIn).thenReturn("$ONE_HOUR_IN_SECONDS")
        Mockito.`when`(token.issuedAt).thenReturn("${System.currentTimeMillis().plus(ONE_DAY_MILLI_SECONDS)}")

        PinValidator().validatePin(token, "15555555555", 123456)
                .subscribe(testObserver)

        val callerId = Gson().fromJson(CALLER_ID_SUCCESS_RESPONSE, CallerId::class.java)

        with(testObserver) {
            awaitTerminalEvent()
            assertNoErrors()
            assertNoTimeout()
            assertValue(callerId)
            assertComplete()
        }
    }

    @Test
    fun testTimeOutExceptionIsThrownOnDelayBy15Seconds() {
        SdkInitiatedValidator.isSdkInit = true

        val mockResponse = MockResponse().setBody("")
        RESTMockServer.whenPATCH(tokenMatcher).thenReturn(mockResponse)

        val testObserver = TestObserver.create<CallerId>()
        val token = Mockito.mock(SessionToken::class.java)
        Mockito.`when`(token.expiresIn).thenReturn("$ONE_HOUR_IN_SECONDS")
        Mockito.`when`(token.issuedAt).thenReturn("${System.currentTimeMillis().plus(ONE_DAY_MILLI_SECONDS)}")

        PinValidator().validatePin(token, "15555555555", 123456)
                .subscribe(testObserver)

        // Cause a timeout to happen
        rxRule.testScheduler.advanceTimeBy(15, TimeUnit.SECONDS)

        testObserver.assertError(TimeoutException::class.java)
    }

    @Test
    fun testNoTimeOutExceptionIsThrownOnDelayBelow15Seconds() {
        SdkInitiatedValidator.isSdkInit = true

        val mockResponse = MockResponse().setBody("")
        RESTMockServer.whenPATCH(tokenMatcher).thenReturn(mockResponse)

        val testObserver = TestObserver.create<CallerId>()
        val token = Mockito.mock(SessionToken::class.java)
        Mockito.`when`(token.expiresIn).thenReturn("$ONE_HOUR_IN_SECONDS")
        Mockito.`when`(token.issuedAt).thenReturn("${System.currentTimeMillis().plus(ONE_DAY_MILLI_SECONDS)}")

        PinValidator().validatePin(token, "15555555555", 123456)
                .subscribe(testObserver)

        rxRule.testScheduler.advanceTimeBy(14, TimeUnit.SECONDS)

        testObserver.assertNoTimeout()
    }
}