package com.rapidsos.emergencydatasdk.auth.password

import com.rapidsos.emergencydatasdk.*
import com.rapidsos.emergencydatasdk.internal.exceptions.EraSdkUnInitializedException
import com.rapidsos.emergencydatasdk.internal.helpers.dependency_injection.Injector
import com.rapidsos.emergencydatasdk.internal.helpers.network.INTERNAL_SERVER_ERROR_CODE
import com.rapidsos.emergencydatasdk.internal.helpers.network.OK_RESPONSE_CODE
import com.rapidsos.emergencydatasdk.internal.helpers.network.UNAUTHORIZED_RESPONSE_CODE
import com.rapidsos.emergencydatasdk.internal.preferences.EmgDataPreferences
import com.rapidsos.emergencydatasdk.rules.MockWebServerRule
import com.rapidsos.emergencydatasdk.rules.RxRule
import com.rapidsos.emergencydatasdk.sdk.SdkInitiatedValidator
import io.appflate.restmock.RESTMockServer
import io.appflate.restmock.utils.RequestMatchers
import io.reactivex.observers.TestObserver
import okhttp3.ResponseBody
import okhttp3.mockwebserver.MockResponse
import org.junit.Before
import org.junit.ClassRule
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import retrofit2.Response
import javax.inject.Inject

/**
 * @author Josias Sena
 */
class PasswordHelperTest : BaseUnitTest() {

    private val tokenMatcher = RequestMatchers.pathContains("oauth/token")
    private val resetPwdMatcher = RequestMatchers.pathContains("v1/rain/password-reset")

    @Inject
    internal lateinit var preferences: EmgDataPreferences

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

        Mockito.`when`(preferences.getHost()).thenReturn(RESTMockServer.getUrl())
        Mockito.`when`(preferences.getClientId()).thenReturn("abc123")
        Mockito.`when`(preferences.getClientSecret()).thenReturn("123abc")
    }

    @Test
    fun testGetSessionTokenErrorSdkNotInit() {
        SdkInitiatedValidator.isSdkInit = false

        val testObserver = TestObserver.create<Response<ResponseBody>>()
        PasswordHelper().resetPassword("test@test.com").subscribe(testObserver)

        with(testObserver) {
            awaitTerminalEvent()
            assertError(EraSdkUnInitializedException::class.java)
            assertErrorMessage("Error resetting password. Please make " +
                    "sure the SDK has been initialized.")
            assertNoValues()
        }
    }

    @Test
    fun resetPasswordGetAccessToken200AndResetPassword200() {
        SdkInitiatedValidator.isSdkInit = true

        val tokenMockResponse = MockResponse()
                .setBody(ACCESS_TOKEN_SUCCESS_RESPONSE)
                .setResponseCode(OK_RESPONSE_CODE)

        RESTMockServer.whenPOST(tokenMatcher).thenReturn(tokenMockResponse)

        val resetPwdMockResponse = MockResponse()
                .setBody(RESET_PWD_SUCCESS_RESPONSE)
                .setResponseCode(OK_RESPONSE_CODE)

        RESTMockServer.whenPOST(resetPwdMatcher).thenReturn(resetPwdMockResponse)

        val testObserver = TestObserver.create<Response<ResponseBody>>()
        PasswordHelper().resetPassword("abc@gmail.com").subscribe(testObserver)

        with(testObserver) {
            assertNoErrors()
            awaitTerminalEvent()
            assertComplete()
        }
    }

    @Test
    fun resetPasswordGetAccessToken200AndResetPassword401() {
        SdkInitiatedValidator.isSdkInit = true

        val tokenMockResponse = MockResponse()
                .setBody(SESSION_TOKEN_INVALID_RESPONSE)
                .setResponseCode(OK_RESPONSE_CODE)

        RESTMockServer.whenPOST(tokenMatcher).thenReturn(tokenMockResponse)

        val resetPwdMockResponse = MockResponse()
                .setBody(ACCESS_TOKEN_INVALID_RESPONSE)
                .setResponseCode(UNAUTHORIZED_RESPONSE_CODE)

        RESTMockServer.whenPOST(resetPwdMatcher).thenReturn(resetPwdMockResponse)

        val testObserver = TestObserver.create<Response<ResponseBody>>()
        PasswordHelper().resetPassword("abc@gmail.com").subscribe(testObserver)

        with(testObserver) {
            awaitTerminalEvent()
            assertNoValues()
        }
    }

    @Test
    fun resetPasswordGetAccessTokenError() {
        SdkInitiatedValidator.isSdkInit = true

        RESTMockServer.whenPOST(tokenMatcher).thenReturnEmpty(INTERNAL_SERVER_ERROR_CODE)

        val testObserver = TestObserver.create<Response<ResponseBody>>()
        PasswordHelper().resetPassword("abc@gmail.com").subscribe(testObserver)

        with(testObserver) {
            awaitTerminalEvent()
            assertNotComplete()
            assertNoValues()
        }
    }

}