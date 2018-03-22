package com.rapidsos.emergencydatasdk.internal.helpers.dependency_injection

import com.rapidsos.emergencydatasdk.auth.login.SessionManager
import com.rapidsos.emergencydatasdk.auth.password.PasswordHelper
import com.rapidsos.emergencydatasdk.auth.register.RegistrationController
import com.rapidsos.emergencydatasdk.devices.DeviceCreator
import com.rapidsos.emergencydatasdk.devices.DeviceProvider
import com.rapidsos.emergencydatasdk.devices.DeviceRemover
import com.rapidsos.emergencydatasdk.devices.DeviceUpdater
import com.rapidsos.emergencydatasdk.internal.helpers.dependency_injection.modules.ApiModule
import com.rapidsos.emergencydatasdk.internal.helpers.dependency_injection.modules.EmgDataPreferencesModule
import com.rapidsos.emergencydatasdk.locations.LocationCreator
import com.rapidsos.emergencydatasdk.locations.LocationProvider
import com.rapidsos.emergencydatasdk.locations.LocationRemover
import com.rapidsos.emergencydatasdk.locations.LocationUpdater
import com.rapidsos.emergencydatasdk.pin.provider.PinProvider
import com.rapidsos.emergencydatasdk.pin.validator.PinValidator
import com.rapidsos.emergencydatasdk.profile.ProfileProvider
import com.rapidsos.emergencydatasdk.profile.ProfileUpdater
import com.rapidsos.emergencydatasdk.sdk.EraSdk
import dagger.Component
import javax.inject.Singleton

/**
 * @author Josias Sena
 */
@Singleton
@Component(modules = [ApiModule::class, EmgDataPreferencesModule::class])
interface DiComponent {
    fun inject(eraSdk: EraSdk)
    fun inject(sessionManager: SessionManager)
    fun inject(passwordHelper: PasswordHelper)
    fun inject(registrationController: RegistrationController)
    fun inject(pinProvider: PinProvider)
    fun inject(pinValidator: PinValidator)
    fun inject(profileProvider: ProfileProvider)
    fun inject(profileUpdater: ProfileUpdater)
    fun inject(locationProvider: LocationProvider)
    fun inject(locationCreator: LocationCreator)
    fun inject(locationRemover: LocationRemover)
    fun inject(locationUpdater: LocationUpdater)
    fun inject(deviceProvider: DeviceProvider)
    fun inject(deviceRemover: DeviceRemover)
    fun inject(deviceCreator: DeviceCreator)
    fun inject(deviceUpdater: DeviceUpdater)
}