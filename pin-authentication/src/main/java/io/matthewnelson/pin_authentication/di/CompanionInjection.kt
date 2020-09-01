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
package io.matthewnelson.pin_authentication.di

import io.matthewnelson.pin_authentication.di.application.ApplicationComponent
import io.matthewnelson.pin_authentication.di.application.module.PrefsModule
import io.matthewnelson.pin_authentication.service.components.*
import io.matthewnelson.pin_authentication.service.PinAuthentication
import io.matthewnelson.encrypted_storage.Prefs
import javax.inject.Inject
import javax.inject.Named

/**
 * Used in [PinAuthentication] to inject the Classes into a private companion object
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
    @Inject @Named(PrefsModule.ENCRYPTED_PREFS) lateinit var encryptedPrefs: Prefs
    @Inject @Named(PrefsModule.PREFS) lateinit var prefs: Prefs

}