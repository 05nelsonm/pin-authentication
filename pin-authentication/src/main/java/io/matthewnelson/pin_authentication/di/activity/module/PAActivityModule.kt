package io.matthewnelson.pin_authentication.di.activity.module

import dagger.Module
import dagger.Provides
import io.matthewnelson.encrypted_storage.EncryptedStorage
import io.matthewnelson.pin_authentication.di.activity.PAActivityScope
import io.matthewnelson.pin_authentication.di.application.module.PAPrefsModule
import io.matthewnelson.pin_authentication.service.PAActivityAccessPoint
import io.matthewnelson.pin_authentication.service.components.*
import javax.inject.Named

/**
 * @suppress
 * */
@Module
internal object PAActivityModule {

    @PAActivityScope
    @Provides
    @JvmStatic
    fun providePAActivityAccessPoint(
        paAppLifecycleWatcher: PAAppLifecycleWatcher,
        paAppLockObserver: PAAppLockObserver,
        paConfirmPinToProceed: PAConfirmPinToProceed,
        paInitialAppLogin: PAInitialAppLogin,
        paPinSecurity: PAPinSecurity,
        paSettings: PASettings,
        paWrongPinLockout: PAWrongPinLockout,
        @Named(PAPrefsModule.ENCRYPTED_PREFS) encryptedPrefs: EncryptedStorage.Prefs
    ): PAActivityAccessPoint =
        PAActivityAccessPoint(
            paAppLifecycleWatcher,
            paAppLockObserver,
            paConfirmPinToProceed,
            paInitialAppLogin,
            paPinSecurity,
            paSettings,
            paWrongPinLockout,
            encryptedPrefs
        )

    @PAActivityScope
    @Provides
    @JvmStatic
    fun providePAWrongPinLockoutTimer(
        @Named(PAPrefsModule.PREFS) prefs: EncryptedStorage.Prefs
    ): PAWrongPinLockout =
        PAWrongPinLockout(prefs)

}