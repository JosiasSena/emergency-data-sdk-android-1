package com.rapidsos.emergencydatasdk.auth.login

import com.google.gson.Gson
import com.rapidsos.emergencydatasdk.BaseUnitTest
import com.rapidsos.emergencydatasdk.SESSION_TOKEN_SUCCESS_RESPONSE
import com.rapidsos.emergencydatasdk.TEST_TOO_MANY_REQUESTS_RESPONSE
import com.rapidsos.emergencydatasdk.TEST_UNAUTHORIZED_RESPONSE
import com.rapidsos.emergencydatasdk.data.network_response.SessionToken
import com.rapidsos.emergencydatasdk.internal.exceptions.EraSdkUnInitializedException
import com.rapidsos.emergencydatasdk.internal.helpers.dependency_injection.Injector
import com.rapidsos.emergencydatasdk.internal.helpers.network.OK_RESPONSE_CODE
import com.rapidsos.emergencydatasdk.internal.helpers.network.TOO_MANY_REQUEST_CODE
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
import javax.inject.Inject

/**
 * @author Josias Sena
 */
class SessionManagerTest : BaseUnitTest() {

    private val matcher = RequestMatchers.pathContains("oauth/token")
    private val gson = Gson()

    @Inject
    lateinit var preference: EmgDataPreferences

    @Rule
    @JvmField
    var mockWebServerRule = MockWebServerRule()

    companion object {
        @ClassRule
        @JvmField
        var rxRule = RxRule()
    }

    @Before
    fun setUpClass() {
        super.setUp()
        Injector.component = component
        component.inject(this)

        Mockito.`when`(preference.getHost()).thenReturn(RESTMockServer.getUrl())
        Mockito.`when`(preference.getClientId()).thenReturn("abc123")
        Mockito.`when`(preference.getClientSecret()).thenReturn("123abc")
    }

    @Test
    fun testGetSessionTokenErrorSdkNotInit() {
        SdkInitiatedValidator.isSdkInit = false

        val testObserver = TestObserver.create<SessionToken>()

        SessionManager().getSessionToken("username", "pwd").subscribe(testObserver)

        with(testObserver) {
            awaitTerminalEvent()
            assertError(EraSdkUnInitializedException::class.java)
            assertErrorMessage("Error logging user in. Please make sure the SDK has been initialized.")
            assertNoValues()
        }
    }

    @Test
    fun testGetSessionTokenSuccess200() {
        SdkInitiatedValidator.isSdkInit = true

        val mockResponse = MockResponse()
                .setBody(SESSION_TOKEN_SUCCESS_RESPONSE)
                .setResponseCode(OK_RESPONSE_CODE)

        RESTMockServer.whenPOST(matcher).thenReturn(mockResponse)

        val testObserver = TestObserver.create<SessionToken>()
        SessionManager().getSessionToken("username", "pwd").subscribe(testObserver)

        val token = gson.fromJson(SESSION_TOKEN_SUCCESS_RESPONSE, SessionToken::class.java)

        with(testObserver) {
            assertNoErrors()
            awaitTerminalEvent()
            assertValue(token)
        }
    }

    @Test
    fun testLoginErrorInvalidCredentials401() {
        SdkInitiatedValidator.isSdkInit = true

        val mockResponse = MockResponse()
                .setBody(TEST_UNAUTHORIZED_RESPONSE)
                .setResponseCode(UNAUTHORIZED_RESPONSE_CODE)

        RESTMockServer.whenPOST(matcher).thenReturn(mockResponse)

        val testObserver = TestObserver.create<SessionToken>()
        SessionManager().getSessionToken("username", "pwd").subscribe(testObserver)

        with(testObserver) {
            awaitTerminalEvent()
            assertErrorMessage("Invalid user credentials.")
            assertNoValues()
        }
    }

    @Test
    fun testLoginErrorTooManyRequests429() {
        SdkInitiatedValidator.isSdkInit = true

        val mockResponse = MockResponse()
                .setBody(TEST_TOO_MANY_REQUESTS_RESPONSE)
                .setResponseCode(TOO_MANY_REQUEST_CODE)

        RESTMockServer.whenPOST(matcher).thenReturn(mockResponse)

        val testObserver = TestObserver.create<SessionToken>()
        SessionManager().getSessionToken("username", "pwd").subscribe(testObserver)

        with(testObserver) {
            awaitTerminalEvent()
            assertErrorMessage("Too many requests. Please try again later.")
            assertNoValues()
        }
    }
}