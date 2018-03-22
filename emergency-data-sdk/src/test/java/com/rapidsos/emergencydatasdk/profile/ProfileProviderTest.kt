package com.rapidsos.emergencydatasdk.profile

import com.google.gson.Gson
import com.rapidsos.emergencydatasdk.ACCESS_TOKEN_EXPIRED_RESPONSE
import com.rapidsos.emergencydatasdk.ACCESS_TOKEN_INVALID_RESPONSE
import com.rapidsos.emergencydatasdk.BaseUnitTest
import com.rapidsos.emergencydatasdk.PROFILE_SUCCESS_RESPONSE
import com.rapidsos.emergencydatasdk.data.network_response.SessionToken
import com.rapidsos.emergencydatasdk.data.profile.Profile
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
class ProfileProviderTest : BaseUnitTest() {

    private val tokenMatcher = RequestMatchers.pathEndsWith("v1/rain/personal-info")

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

        val mockResponse = MockResponse()
                .setBody(PROFILE_SUCCESS_RESPONSE)
                .setResponseCode(OK_RESPONSE_CODE)

        RESTMockServer.whenGET(tokenMatcher).thenReturn(mockResponse)

        val testObserver = TestObserver.create<Profile>()
        val token = Mockito.mock(SessionToken::class.java)

        ProfileProvider().getPersonalInfo(token).subscribe(testObserver)

        with(testObserver) {
            awaitTerminalEvent()
            assertError(EraSdkUnInitializedException::class.java)
            assertErrorMessage("Error getting personal information. Please make sure the " +
                    "SDK has been initialized.")
            assertNoValues()
        }
    }

    @Test
    fun testErrorBecauseOfExpiredToken() {
        SdkInitiatedValidator.isSdkInit = true

        val mockResponse = MockResponse()
                .setBody(ACCESS_TOKEN_EXPIRED_RESPONSE)
                .setResponseCode(UNAUTHORIZED_RESPONSE_CODE)

        RESTMockServer.whenGET(tokenMatcher).thenReturn(mockResponse)

        val testObserver = TestObserver.create<Profile>()

        val expiredToken = SessionToken().apply {
            expiresIn = "$ONE_HOUR_IN_SECONDS"
            issuedAt = "${System.currentTimeMillis().minus(ONE_DAY_MILLI_SECONDS)}"
        }

        ProfileProvider().getPersonalInfo(expiredToken).subscribe(testObserver)

        with(testObserver) {
            awaitTerminalEvent()
            assertError(SessionTokenExpiredException::class.java)
            assertErrorMessage("Cannot get personal information. The session token " +
                    "provided is either invalid or expired.")
            assertNoValues()
        }
    }

    @Test
    fun testErrorBecauseOfInvalidAccessToken() {
        SdkInitiatedValidator.isSdkInit = true

        val mockResponse = MockResponse()
                .setBody(ACCESS_TOKEN_INVALID_RESPONSE)
                .setResponseCode(UNAUTHORIZED_RESPONSE_CODE)

        RESTMockServer.whenGET(tokenMatcher).thenReturn(mockResponse)

        val testObserver = TestObserver.create<Profile>()
        val token = SessionToken().apply {
            expiresIn = "$ONE_HOUR_IN_SECONDS"
            issuedAt = "${System.currentTimeMillis().plus(ONE_DAY_MILLI_SECONDS)}"
        }

        ProfileProvider().getPersonalInfo(token).subscribe(testObserver)

        with(testObserver) {
            awaitTerminalEvent()
            assertNoValues()
            assertError { throwable: Throwable ->
                throwable.message?.contains(ACCESS_TOKEN_INVALID_RESPONSE) as Boolean
            }
        }
    }

    @Test
    fun testGetProfileInformationSuccess() {
        SdkInitiatedValidator.isSdkInit = true

        val mockResponse = MockResponse()
                .setBody(PROFILE_SUCCESS_RESPONSE)
                .setResponseCode(OK_RESPONSE_CODE)

        RESTMockServer.whenGET(tokenMatcher).thenReturn(mockResponse)

        val testObserver = TestObserver.create<Profile>()
        val token = SessionToken().apply {
            expiresIn = "$ONE_HOUR_IN_SECONDS"
            issuedAt = "${System.currentTimeMillis().plus(ONE_DAY_MILLI_SECONDS)}"
        }

        ProfileProvider().getPersonalInfo(token).subscribe(testObserver)

        val profile = Gson().fromJson(PROFILE_SUCCESS_RESPONSE, Profile::class.java)
        with(testObserver) {
            awaitTerminalEvent()
            assertNoErrors()
            assertValue(profile)
            assertComplete()
        }
    }

    @Test
    fun testGetProfileInformationTimeoutOver14Seconds() {
        SdkInitiatedValidator.isSdkInit = true

        val mockResponse = MockResponse()
                .setBody(PROFILE_SUCCESS_RESPONSE)
                .setResponseCode(OK_RESPONSE_CODE)

        RESTMockServer.whenGET(tokenMatcher).thenReturn(mockResponse)

        val testObserver = TestObserver.create<Profile>()
        val token = SessionToken().apply {
            expiresIn = "$ONE_HOUR_IN_SECONDS"
            issuedAt = "${System.currentTimeMillis().plus(ONE_DAY_MILLI_SECONDS)}"
        }

        ProfileProvider().getPersonalInfo(token).subscribe(testObserver)

        rxRule.testScheduler.advanceTimeBy(15, TimeUnit.SECONDS)

        with(testObserver) {
            awaitTerminalEvent()
            assertError(TimeoutException::class.java)
            assertNoValues()
            assertNotComplete()
        }
    }
}