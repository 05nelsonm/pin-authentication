/*
* Copyright (C) 2020 Matthew Nelson
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program.  If not, see <https://www.gnu.org/licenses/>.
* */
package io.matthewnelson.pin_authentication.di.application.module

import android.app.Application
import dagger.Module
import dagger.Provides
import io.matthewnelson.encrypted_storage.Prefs
import io.matthewnelson.pin_authentication.R
import io.matthewnelson.pin_authentication.di.application.PAApplicationScope
import io.matthewnelson.pin_authentication.service.components.*
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Named

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
        @Named(PrefsModule.ENCRYPTED_PREFS) encryptedPrefs: Prefs,
        @Named(PrefsModule.PREFS) prefs: Prefs
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
    fun provideSettings(@Named(PrefsModule.PREFS) prefs: Prefs): Settings =
        Settings(prefs)

    @PAApplicationScope
    @Provides
    @JvmStatic
    fun provideViewColors(
        application: Application,
        @Named(PrefsModule.PREFS) prefs: Prefs
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
