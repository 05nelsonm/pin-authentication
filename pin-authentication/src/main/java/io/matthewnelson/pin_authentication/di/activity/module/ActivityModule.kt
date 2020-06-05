package io.matthewnelson.pin_authentication.di.activity.module

import dagger.Module
import dagger.Provides
import io.matthewnelson.encrypted_storage.EncryptedStorage
import io.matthewnelson.pin_authentication.di.activity.PAActivityScope
import io.matthewnelson.pin_authentication.di.application.module.PrefsModule
import io.matthewnelson.pin_authentication.service.AuthenticationActivityAccessPoint
import io.matthewnelson.pin_authentication.service.components.*
import javax.inject.Named

/**
 * @suppress
 * */
@Module
internal object ActivityModule {

    @PAActivityScope
    @Provides
    @JvmStatic
    fun provideAuthenticationActivityAccessPoint(
        appLifecycleWatcher: AppLifecycleWatcher,
        appLockObserver: AppLockObserver,
        confirmPinToProceed: ConfirmPinToProceed,
        initialAppLogin: InitialAppLogin,
        pinSecurity: PinSecurity,
        settings: Settings,
        wrongPinLockout: WrongPinLockout,
        @Named(PrefsModule.ENCRYPTED_PREFS) encryptedPrefs: EncryptedStorage.Prefs
    ): AuthenticationActivityAccessPoint =
        AuthenticationActivityAccessPoint(
            appLifecycleWatcher,
            appLockObserver,
            confirmPinToProceed,
            initialAppLogin,
            pinSecurity,
            settings,
            wrongPinLockout,
            encryptedPrefs
        )

    @PAActivityScope
    @Provides
    @JvmStatic
    fun provideWrongPinLockout(
        @Named(PrefsModule.PREFS) prefs: EncryptedStorage.Prefs
    ): WrongPinLockout =
        WrongPinLockout(prefs)

}