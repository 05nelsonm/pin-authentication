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
package io.matthewnelson.pin_authentication.di.activity.module

import dagger.Module
import dagger.Provides
import io.matthewnelson.encrypted_storage.EncryptedStorage
import io.matthewnelson.pin_authentication.di.activity.PAActivityScope
import io.matthewnelson.pin_authentication.di.application.module.PrefsModule
import io.matthewnelson.pin_authentication.service.AuthenticationActivityAccessPoint
import io.matthewnelson.pin_authentication.service.components.*
import javax.inject.Named

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