package com.rapidsos.emergencydatasdk.auth.register

import com.rapidsos.emergencydatasdk.*
import com.rapidsos.emergencydatasdk.data.user.User
import com.rapidsos.emergencydatasdk.internal.exceptions.EraSdkUnInitializedException
import com.rapidsos.emergencydatasdk.internal.helpers.dependency_injection.Injector
import com.rapidsos.emergencydatasdk.internal.helpers.network.BAD_REQUEST_RESPONSE_CODE
import com.rapidsos.emergencydatasdk.internal.helpers.network.CREATED_SUCCESS_RESPONSE_CODE
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
class RegistrationControllerTest : BaseUnitTest() {

    private val tokenMatcher = RequestMatchers.pathContains("oauth/token")
    private val registerUserMatcher = RequestMatchers.pathContains("v1/rain/user")

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

        val testObserver = TestObserver.create<User>()
        val user = Mockito.mock(User::class.java)
        RegistrationController().register(user).subscribe(testObserver)

        with(testObserver) {
            awaitTerminalEvent()
            assertError(EraSdkUnInitializedException::class.java)
            assertErrorMessage("Error registering the user. Please make " +
                    "sure the SDK has been initialized.")
            assertNoValues()
        }
    }

    @Test
    fun testSuccessfulRegistration() {
        SdkInitiatedValidator.isSdkInit = true

        val tokenMockResponse = MockResponse()
                .setBody(ACCESS_TOKEN_SUCCESS_RESPONSE)
                .setResponseCode(OK_RESPONSE_CODE)

        RESTMockServer.whenPOST(tokenMatcher).thenReturn(tokenMockResponse)

        val registrationMockResponse = MockResponse()
                .setBody(REGISTRATION_SUCCESS_RESPONSE)
                .setResponseCode(CREATED_SUCCESS_RESPONSE_CODE)

        RESTMockServer.whenPOST(registerUserMatcher).thenReturn(registrationMockResponse)

        val testObserver = TestObserver.create<User>()
        val user = User().apply {
            email = "test@email.com"
            username = "testusername"
            password = "testpassword"
        }

        RegistrationController().register(user).subscribe(testObserver)

        with(testObserver) {
            awaitTerminalEvent()
            assertNoErrors()
            assertComplete()
            assertValueCount(1)
        }
    }

    @Test
    fun testErroneousRegistrationUserWithUsernameAlreadyExists() {
        SdkInitiatedValidator.isSdkInit = true

        val tokenMockResponse = MockResponse()
                .setBody(ACCESS_TOKEN_SUCCESS_RESPONSE)
                .setResponseCode(OK_RESPONSE_CODE)

        RESTMockServer.whenPOST(tokenMatcher).thenReturn(tokenMockResponse)

        val registrationMockResponse = MockResponse()
                .setBody(REGISTRATION_USER_WITH_USERNAME_EXISTS_RESPONSE)
                .setResponseCode(BAD_REQUEST_RESPONSE_CODE)

        RESTMockServer.whenPOST(registerUserMatcher).thenReturn(registrationMockResponse)

        val testObserver = TestObserver.create<User>()
        val user = User().apply {
            email = "test@email.com"
            username = "testusername"
            password = "testpassword"
        }

        RegistrationController().register(user).subscribe(testObserver)

        with(testObserver) {
            awaitTerminalEvent()
            assertErrorMessage("User with the same username or email already exists")
            assertNoValues()
        }
    }

    @Test
    fun testErroneousRegistrationInvalidUsernameLength() {
        SdkInitiatedValidator.isSdkInit = true

        val tokenMockResponse = MockResponse()
                .setBody(ACCESS_TOKEN_SUCCESS_RESPONSE)
                .setResponseCode(OK_RESPONSE_CODE)

        RESTMockServer.whenPOST(tokenMatcher).thenReturn(tokenMockResponse)

        val registrationMockResponse = MockResponse()
                .setBody(REGISTRATION_INVALID_USERNAME_LENGTH_RESPONSE)
                .setResponseCode(BAD_REQUEST_RESPONSE_CODE)

        RESTMockServer.whenPOST(registerUserMatcher).thenReturn(registrationMockResponse)

        val testObserver = TestObserver.create<User>()
        val user = User().apply {
            email = "test@email.com"
            username = "test123456"
            password = "pwd"
        }

        RegistrationController().register(user).subscribe(testObserver)

        with(testObserver) {
            awaitTerminalEvent()
            assertErrorMessage(REGISTRATION_INVALID_USERNAME_LENGTH_RESPONSE)
            assertNoValues()
        }
    }

    @Test
    fun testErroneousRegistrationInvalidPasswordLength() {
        SdkInitiatedValidator.isSdkInit = true

        val tokenMockResponse = MockResponse()
                .setBody(ACCESS_TOKEN_SUCCESS_RESPONSE)
                .setResponseCode(OK_RESPONSE_CODE)

        RESTMockServer.whenPOST(tokenMatcher).thenReturn(tokenMockResponse)

        val registrationMockResponse = MockResponse()
                .setBody(REGISTRATION_INVALID_PASSWORD_LENGTH_RESPONSE)
                .setResponseCode(BAD_REQUEST_RESPONSE_CODE)

        RESTMockServer.whenPOST(registerUserMatcher).thenReturn(registrationMockResponse)

        val testObserver = TestObserver.create<User>()
        val user = User().apply {
            email = "test@email.com"
            username = "test123456"
            password = "pwd"
        }

        RegistrationController().register(user).subscribe(testObserver)

        with(testObserver) {
            awaitTerminalEvent()
            assertErrorMessage(REGISTRATION_INVALID_PASSWORD_LENGTH_RESPONSE)
            assertNoValues()
        }
    }

    @Test
    fun testErroneousRegistrationInvalidEmail() {
        SdkInitiatedValidator.isSdkInit = true

        val tokenMockResponse = MockResponse()
                .setBody(ACCESS_TOKEN_SUCCESS_RESPONSE)
                .setResponseCode(OK_RESPONSE_CODE)

        RESTMockServer.whenPOST(tokenMatcher).thenReturn(tokenMockResponse)

        val registrationMockResponse = MockResponse()
                .setBody(REGISTRATION_INVALID_EMAIL_RESPONSE)
                .setResponseCode(BAD_REQUEST_RESPONSE_CODE)

        RESTMockServer.whenPOST(registerUserMatcher).thenReturn(registrationMockResponse)

        val testObserver = TestObserver.create<User>()
        val user = User().apply {
            email = "test@"
            username = "test123456"
            password = "testpassword"
        }

        RegistrationController().register(user).subscribe(testObserver)

        with(testObserver) {
            awaitTerminalEvent()
            assertErrorMessage(REGISTRATION_INVALID_EMAIL_RESPONSE)
            assertNoValues()
        }
    }

    @Test
    fun testErroneousRegistrationAllFieldsAreInvalid() {
        SdkInitiatedValidator.isSdkInit = true

        val tokenMockResponse = MockResponse()
                .setBody(ACCESS_TOKEN_SUCCESS_RESPONSE)
                .setResponseCode(OK_RESPONSE_CODE)

        RESTMockServer.whenPOST(tokenMatcher).thenReturn(tokenMockResponse)

        val registrationMockResponse = MockResponse()
                .setBody(REGISTRATION_ALL_FIELDS_INVALID__RESPONSE)
                .setResponseCode(BAD_REQUEST_RESPONSE_CODE)

        RESTMockServer.whenPOST(registerUserMatcher).thenReturn(registrationMockResponse)

        val testObserver = TestObserver.create<User>()
        val user = User().apply {
            email = "test@"
            username = "test123456"
            password = "testpassword"
        }

        RegistrationController().register(user).subscribe(testObserver)

        with(testObserver) {
            awaitTerminalEvent()
            assertErrorMessage(REGISTRATION_ALL_FIELDS_INVALID__RESPONSE)
            assertNoValues()
        }
    }

    @Test
    fun testErroneousRegistrationTimedOutAt15Seconds() {
        SdkInitiatedValidator.isSdkInit = true

        val tokenMockResponse = MockResponse()
                .setBody(ACCESS_TOKEN_SUCCESS_RESPONSE)
                .setResponseCode(OK_RESPONSE_CODE)

        RESTMockServer.whenPOST(tokenMatcher).thenReturn(tokenMockResponse)

        val registrationMockResponse = MockResponse()
                .setBody(REGISTRATION_SUCCESS_RESPONSE)
                .setResponseCode(CREATED_SUCCESS_RESPONSE_CODE)

        RESTMockServer.whenPOST(registerUserMatcher).thenReturn(registrationMockResponse)

        val testObserver = TestObserver.create<User>()
        val user = Mockito.mock(User::class.java)

        RegistrationController().register(user).subscribe(testObserver)

        rxRule.testScheduler.advanceTimeBy(15, TimeUnit.SECONDS)

        with(testObserver) {
            awaitTerminalEvent()
            assertNoValues()
            assertError(TimeoutException::class.java)
        }
    }

}