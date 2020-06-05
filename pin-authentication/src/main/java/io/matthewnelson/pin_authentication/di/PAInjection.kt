package io.matthewnelson.pin_authentication.di

import io.matthewnelson.pin_authentication.di.application.PAApplicationComponent
import io.matthewnelson.pin_authentication.di.application.module.PAPrefsModule
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
 * @param [paApplicationComponent] [PAApplicationComponent]
 * */
internal class PAInjection(private val paApplicationComponent: PAApplicationComponent) {

    init {
        paApplicationComponent.inject(this)
    }

    @Inject lateinit var paAppLockObserver: PAAppLockObserver
    @Inject lateinit var paConfirmPinToProceed: PAConfirmPinToProceed
    @Inject lateinit var paInitialAppLogin: PAInitialAppLogin
    @Inject lateinit var paPinSecurity: PAPinSecurity
    @Inject lateinit var paSettings: PASettings
    @Inject lateinit var paViewColors: PAViewColors
    @Inject lateinit var paAppLifecycleWatcher: PAAppLifecycleWatcher
    @Inject @Named(PAPrefsModule.ENCRYPTED_PREFS) lateinit var encryptedPrefs: EncryptedStorage.Prefs
    @Inject @Named(PAPrefsModule.PREFS) lateinit var prefs: EncryptedStorage.Prefs

}