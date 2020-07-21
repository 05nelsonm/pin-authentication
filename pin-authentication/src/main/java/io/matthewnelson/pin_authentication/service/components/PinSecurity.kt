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
package io.matthewnelson.pin_authentication.service.components

import androidx.lifecycle.LiveData
import io.matthewnelson.pin_authentication.util.PrefsKeys
import io.matthewnelson.encrypted_storage.EncryptedStorage

/**
 * Wrapper class for [ConfirmPinToProceed] that handles everything to do with PinSecurity
 *
 * @param [confirmPinToProceed] [ConfirmPinToProceed]
 * @param [settings] [Settings]
 * @param [encryptedPrefs] [EncryptedStorage.Prefs]
 * @param [prefs] [EncryptedStorage.Prefs]
 * */
internal class PinSecurity(
    private val confirmPinToProceed: ConfirmPinToProceed,
    private val settings: Settings,
    private val encryptedPrefs: EncryptedStorage.Prefs,
    private val prefs: EncryptedStorage.Prefs
) {

    init {
        confirmPinToProceed.registerRequestKey(
            PrefsKeys.PIN_SECURITY_IS_ENABLED,
            false,
            addToBlacklist = true
        )
    }


    /////////////////////////
    // Modify Pin Security //
    /////////////////////////

    fun disablePinSecuritySuccess() {
        encryptedPrefs.remove(PrefsKeys.USER_PIN)
        encryptedPrefs.remove(PrefsKeys.PIN_AUTHENTICATION_SALT)
        settings.setUserPinIsSet(false)
        prefs.write(PrefsKeys.PIN_SECURITY_IS_ENABLED, false)
        setPinSecurityValue(false)
        confirmPinToProceed.setAllRequestKeysTo(true)
    }

    fun enablePinSecurityFailure() {
        setPinSecurityValue(false)
        confirmPinToProceed.setCurrentRequestKey("")
    }

    fun enablePinSecuritySuccess() {
        prefs.write(PrefsKeys.PIN_SECURITY_IS_ENABLED, true)
        setPinSecurityValue(true)
        confirmPinToProceed.setAllRequestKeysTo(false)
        confirmPinToProceed.setCurrentRequestKey("")
    }

    fun getPinSecurity(): LiveData<Boolean>? =
        confirmPinToProceed.getValueOfRequestKey(PrefsKeys.PIN_SECURITY_IS_ENABLED)

    fun isCurrentRequestKeyPinSecurity(): Boolean =
        confirmPinToProceed.getCurrentRequestKey() == PrefsKeys.PIN_SECURITY_IS_ENABLED

    fun isPinSecurityEnabled(): Boolean =
        confirmPinToProceed.getValueOfRequestKey(
            PrefsKeys.PIN_SECURITY_IS_ENABLED
        )?.value ?: false

    fun setCurrentRequestKeyToPinSecurity() {
        confirmPinToProceed.setCurrentRequestKey(PrefsKeys.PIN_SECURITY_IS_ENABLED)
    }

    fun setPinSecurityValue(value: Boolean) {
        confirmPinToProceed.setRequestKeyValueTo(PrefsKeys.PIN_SECURITY_IS_ENABLED, value)
    }


    ///////////////////////////////////////////////////////
    // AuthenticationManager build method initialization //
    ///////////////////////////////////////////////////////
    private var initOnceCounter = 0

    fun initializePinSecurity() {
        if (initOnceCounter < 1) {
            loadPinSecurityFromSharedPreferences()
            initOnceCounter++
        }
    }

    private fun loadPinSecurityFromSharedPreferences() {
        prefs.read(PrefsKeys.PIN_SECURITY_IS_ENABLED)?.let {
            setPinSecurityValue(it)
        }
    }

}