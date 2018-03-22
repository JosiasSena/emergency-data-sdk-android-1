package com.rapidsos.emergencydatasdk.sdk

import android.content.Context
import com.rapidsos.emergencydatasdk.BaseUnitTest
import com.rapidsos.emergencydatasdk.internal.helpers.dependency_injection.Injector
import com.rapidsos.emergencydatasdk.internal.preferences.EmgDataPreferences
import junit.framework.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import javax.inject.Inject

/**
 * @author Josias Sena
 */
class EraSdkTest : BaseUnitTest() {

    @Inject
    internal lateinit var preferences: EmgDataPreferences

    @Before
    fun setUpClass() {
        super.setUp()

        Injector.component = component
        component.inject(this)
    }

    @Test
    fun init() {
        val context = Mockito.mock(Context::class.java)
        val emdDataSDK = EraSdk(context)
        emdDataSDK.initialize("https://www.rapidsos.com", "abc123", "123abc")

        Mockito.verify(preferences).setHost("https://www.rapidsos.com")
        Mockito.verify(preferences).setClientId("abc123")
        Mockito.verify(preferences).setClientSecret("123abc")

        Assert.assertTrue(SdkInitiatedValidator.isSdkInit)
    }
}