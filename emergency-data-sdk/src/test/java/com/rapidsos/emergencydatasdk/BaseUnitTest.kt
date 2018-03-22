package com.rapidsos.emergencydatasdk

import android.content.Context
import com.rapidsos.emergencydatasdk.auth.login.SessionManagerTest
import com.rapidsos.emergencydatasdk.auth.password.PasswordHelperTest
import com.rapidsos.emergencydatasdk.auth.register.RegistrationControllerTest
import com.rapidsos.emergencydatasdk.devices.DeviceCreatorTest
import com.rapidsos.emergencydatasdk.devices.DeviceProviderTest
import com.rapidsos.emergencydatasdk.devices.DeviceRemoverTest
import com.rapidsos.emergencydatasdk.devices.DeviceUpdaterTest
import com.rapidsos.emergencydatasdk.internal.helpers.dependency_injection.DiComponent
import com.rapidsos.emergencydatasdk.internal.helpers.dependency_injection.modules.ApiModule
import com.rapidsos.emergencydatasdk.internal.helpers.dependency_injection.modules.EmgDataPreferencesModule
import com.rapidsos.emergencydatasdk.locations.LocationCreatorTest
import com.rapidsos.emergencydatasdk.locations.LocationProviderTest
import com.rapidsos.emergencydatasdk.locations.LocationRemoverTest
import com.rapidsos.emergencydatasdk.locations.LocationUpdaterTest
import com.rapidsos.emergencydatasdk.pin.provider.PinProviderTest
import com.rapidsos.emergencydatasdk.pin.validator.PinValidatorTest
import com.rapidsos.emergencydatasdk.profile.ProfileProviderTest
import com.rapidsos.emergencydatasdk.profile.ProfileUpdaterTest
import com.rapidsos.emergencydatasdk.sdk.EraSdkTest
import dagger.Component
import org.junit.Before
import org.mockito.Mockito
import javax.inject.Singleton

/**
 * @author Josias Sena
 */
open class BaseUnitTest {

    lateinit var component: TestComponent

    @Singleton
    @Component(modules = [ApiModule::class, EmgDataPreferencesModule::class])
    interface TestComponent : DiComponent {
        fun inject(emgDataSDKTest: EraSdkTest)
        fun inject(sessionManagerTest: SessionManagerTest)
        fun inject(passwordHelperTest: PasswordHelperTest)
        fun inject(registrationControllerTest: RegistrationControllerTest)
        fun inject(pinValidatorTest: PinValidatorTest)
        fun inject(pinProviderTest: PinProviderTest)
        fun inject(profileProviderTest: ProfileProviderTest)
        fun inject(profileUpdaterTest: ProfileUpdaterTest)
        fun inject(locationProviderTest: LocationProviderTest)
        fun inject(locationCreatorTest: LocationCreatorTest)
        fun inject(deviceProviderTest: DeviceProviderTest)
        fun inject(locationRemoverTest: LocationRemoverTest)
        fun inject(locationUpdater: LocationUpdaterTest)
        fun inject(deviceRemoverTest: DeviceRemoverTest)
        fun inject(deviceCreatorTest: DeviceCreatorTest)
        fun inject(deviceUpdaterTest: DeviceUpdaterTest)
    }

    @Before
    fun setUp() {
        val context = Mockito.mock(Context::class.java)

        component = DaggerBaseUnitTest_TestComponent.builder()
                .apiModule(MockApiModule(context))
                .emgDataPreferencesModule(MockEmgDataPreferencesModule(context))
                .build()
    }
}