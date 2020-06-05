package io.matthewnelson.pin_authentication.di

import io.matthewnelson.pin_authentication.di.application.ApplicationComponent
import io.matthewnelson.pin_authentication.di.application.module.PrefsModule
import io.matthewnelson.pin_authentication.service.components.*
import io.matthewnelson.pin_authentication.service.PinAuthentication
import io.matthewnelson.encrypted_storage.EncryptedStorage
import javax.inject.Inject
import javax.inject.Named

/**
 * @suppress
 * Used in [PinAuthentication] to inject the Classes into a private companion objects
 * which is then used by the open classes which are to be accessed by the Application, as needed.
 * @see [PinAuthentication.Companion.injected]
 *
 * @param [applicationComponent] [ApplicationComponent]
 * */
internal class CompanionInjection(private val applicationComponent: ApplicationComponent) {

    init {
        applicationComponent.inject(this)
    }

    @Inject lateinit var appLockObserver: AppLockObserver
    @Inject lateinit var confirmPinToProceed: ConfirmPinToProceed
    @Inject lateinit var initialAppLogin: InitialAppLogin
    @Inject lateinit var pinSecurity: PinSecurity
    @Inject lateinit var settings: Settings
    @Inject lateinit var viewColors: ViewColors
    @Inject lateinit var appLifecycleWatcher: AppLifecycleWatcher
    @Inject @Named(PrefsModule.ENCRYPTED_PREFS) lateinit var encryptedPrefs: EncryptedStorage.Prefs
    @Inject @Named(PrefsModule.PREFS) lateinit var prefs: EncryptedStorage.Prefs

}