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
internal object ApplicationModule {

    @PAApplicationScope
    @Provides
    @JvmStatic
    fun provideAppLifecycleWatcher(application: Application): AppLifecycleWatcher =
        AppLifecycleWatcher(application)

    @PAApplicationScope
    @Provides
    @JvmStatic
    fun provideAppLockObserver(
        appLifecycleWatcher: AppLifecycleWatcher,
        coroutines: Coroutines,
        pinSecurity: PinSecurity,
        settings: Settings,
        viewData: ViewData
    ): AppLockObserver =
        AppLockObserver(
            appLifecycleWatcher,
            coroutines,
            pinSecurity,
            settings,
            viewData
        )

    @PAApplicationScope
    @Provides
    @JvmStatic
    fun provideConfirmPinToProceed(
        appLifecycleWatcher: AppLifecycleWatcher,
        coroutines: Coroutines
    ): ConfirmPinToProceed =
        ConfirmPinToProceed(appLifecycleWatcher, coroutines)

    @PAApplicationScope
    @Provides
    @JvmStatic
    fun provideCoroutines(
        @Named(CoroutineDispatchersModule.MAIN) dispatcherMain: CoroutineDispatcher,
        @Named(CoroutineDispatchersModule.DEFAULT) dispatcherDefault: CoroutineDispatcher
    ): Coroutines =
        Coroutines(dispatcherDefault, dispatcherMain)

    @PAApplicationScope
    @Provides
    @JvmStatic
    fun provideInitialAppLogin(): InitialAppLogin =
        InitialAppLogin()

    @PAApplicationScope
    @Provides
    @JvmStatic
    fun providePinSecurity(
        confirmPinToProceed: ConfirmPinToProceed,
        settings: Settings,
        @Named(PrefsModule.ENCRYPTED_PREFS) encryptedPrefs: EncryptedStorage.Prefs,
        @Named(PrefsModule.PREFS) prefs: EncryptedStorage.Prefs
    ): PinSecurity =
        PinSecurity(
            confirmPinToProceed,
            settings,
            encryptedPrefs,
            prefs
        )

    @PAApplicationScope
    @Provides
    @JvmStatic
    fun provideSettings(@Named(PrefsModule.PREFS) prefs: EncryptedStorage.Prefs): Settings =
        Settings(prefs)

    @PAApplicationScope
    @Provides
    @JvmStatic
    fun provideViewColors(
        application: Application,
        @Named(PrefsModule.PREFS) prefs: EncryptedStorage.Prefs
    ): ViewColors =
        ViewColors(application.applicationContext, prefs)

    @PAApplicationScope
    @Provides
    @JvmStatic
    fun provideViewData(application: Application, settings: Settings): ViewData =
        ViewData(
            application.applicationContext,
            settings,
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
