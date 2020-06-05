package io.matthewnelson.pin_authentication.di.application.module

import android.app.Application
import dagger.Module
import dagger.Provides
import io.matthewnelson.encrypted_storage.EncryptedStorage
import io.matthewnelson.pin_authentication.R
import io.matthewnelson.pin_authentication.di.application.PAApplicationScope
import io.matthewnelson.pin_authentication.service.components.*
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Named

/**
 * @suppress
 * */
@Module
internal object PAApplicationModule {

    @PAApplicationScope
    @Provides
    @JvmStatic
    fun providePAAppLifecycleWatcher(application: Application): PAAppLifecycleWatcher =
        PAAppLifecycleWatcher(application)

    @PAApplicationScope
    @Provides
    @JvmStatic
    fun providePAAppLockObserver(
        paAppLifecycleWatcher: PAAppLifecycleWatcher,
        paCoroutines: PACoroutines,
        paPinSecurity: PAPinSecurity,
        paSettings: PASettings,
        paViewData: PAViewData
    ): PAAppLockObserver =
        PAAppLockObserver(
            paAppLifecycleWatcher,
            paCoroutines,
            paPinSecurity,
            paSettings,
            paViewData
        )

    @PAApplicationScope
    @Provides
    @JvmStatic
    fun providePAConfirmPinToProceed(
        paAppLifecycleWatcher: PAAppLifecycleWatcher,
        paCoroutines: PACoroutines
    ): PAConfirmPinToProceed =
        PAConfirmPinToProceed(paAppLifecycleWatcher, paCoroutines)

    @PAApplicationScope
    @Provides
    @JvmStatic
    fun providePACoroutines(
        @Named(PACoroutineDispatchersModule.MAIN) dispatcherMain: CoroutineDispatcher,
        @Named(PACoroutineDispatchersModule.DEFAULT) dispatcherDefault: CoroutineDispatcher
    ): PACoroutines =
        PACoroutines(dispatcherDefault, dispatcherMain)

    @PAApplicationScope
    @Provides
    @JvmStatic
    fun providePAInitialAppLogin(): PAInitialAppLogin =
        PAInitialAppLogin()

    @PAApplicationScope
    @Provides
    @JvmStatic
    fun providePAPinSecurity(
        paConfirmPinToProceed: PAConfirmPinToProceed,
        paSettings: PASettings,
        @Named(PAPrefsModule.ENCRYPTED_PREFS) encryptedPrefs: EncryptedStorage.Prefs,
        @Named(PAPrefsModule.PREFS) prefs: EncryptedStorage.Prefs
    ): PAPinSecurity =
        PAPinSecurity(
            paConfirmPinToProceed,
            paSettings,
            encryptedPrefs,
            prefs
        )

    @PAApplicationScope
    @Provides
    @JvmStatic
    fun providePASettings(@Named(PAPrefsModule.PREFS) prefs: EncryptedStorage.Prefs): PASettings =
        PASettings(prefs)

    @PAApplicationScope
    @Provides
    @JvmStatic
    fun providePAViewColors(
        application: Application,
        @Named(PAPrefsModule.PREFS) prefs: EncryptedStorage.Prefs
    ): PAViewColors =
        PAViewColors(application.applicationContext, prefs)

    @PAApplicationScope
    @Provides
    @JvmStatic
    fun providePAViewData(application: Application, paSettings: PASettings): PAViewData =
        PAViewData(
            application.applicationContext,
            paSettings,
            R.string.pa_confirm_pin,
            R.string.pa_login,
            R.string.pa_reset_pin,
            R.string.pa_min,
            R.string.pa_max,
            R.string.pa_set_pin_step_1,
            R.string.pa_set_pin_step_2,
            R.string.pa_one_more_attempt,
            R.string.pa_pin_does_not_match,
            R.string.pa_wrong_pin
        )

}
