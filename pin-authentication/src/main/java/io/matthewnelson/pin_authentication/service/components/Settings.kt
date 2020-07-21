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

import io.matthewnelson.pin_authentication.util.PrefsKeys
import io.matthewnelson.encrypted_storage.EncryptedStorage

/**
 * @suppress
 * */
internal class Settings(private val prefs: EncryptedStorage.Prefs) {

    //////////////
    // Settings //
    //////////////
    private var buildConfigDebug = false
    private var appHasOnBoardProcess = false
    private var hapticFeedbackIsEnabled = false
    private var minPinLength = 4
    private var onBoardProcessIsComplete = false
    private var scrambledPinIsEnabled = false
    private var userPinIsSet = false

    fun getBuildConfigDebug(): Boolean =
        buildConfigDebug

    fun getAppHasOnBoardProcess(): Boolean =
        appHasOnBoardProcess

    fun getHapticFeedbackIsEnabled(): Boolean =
        hapticFeedbackIsEnabled

    fun getMinPinLength(): Int =
        minPinLength

    fun getOnBoardProcessIsComplete(): Boolean =
        onBoardProcessIsComplete

    fun getScrambledPinIsEnabled(): Boolean =
        scrambledPinIsEnabled

    fun getUserPinIsSet(): Boolean =
        userPinIsSet

    fun hasAppOnBoardProcessBeenSatisfied(): Boolean {
        return if (appHasOnBoardProcess) {
            onBoardProcessIsComplete
        } else {
            true
        }
    }

    fun setHapticFeedbackIsEnabled(enable: Boolean) {
        hapticFeedbackIsEnabled = enable
        prefs.write(PrefsKeys.HAPTIC_FEEDBACK_IS_ENABLED, enable)
    }

    fun setOnBoardProcessIsComplete() {
        onBoardProcessIsComplete = true
        prefs.write(PrefsKeys.ONBOARD_PROCESS_COMPLETE, true)
    }

    fun setScrambledPinIsEnabled(enable: Boolean) {
        scrambledPinIsEnabled = enable
        prefs.write(PrefsKeys.SCRAMBLED_PIN_IS_ENABLED, enable)
    }

    fun setUserPinIsSet(boolean: Boolean) {
        userPinIsSet = boolean
        if (boolean) {
            prefs.write(PrefsKeys.USER_PIN_IS_SET, boolean)
        } else {
            prefs.remove(PrefsKeys.USER_PIN_IS_SET)
        }
    }


    ///////////////////////////////////////////////////
    // PinAuthentication build method initialization //
    ///////////////////////////////////////////////////
    private var initOnceCounter = 0

    fun initializeSettings(
        buildConfigDebugInitValue: Boolean,
        appHasOnBoardProcessInitValue: Boolean,
        hapticFeedbackIsEnabledInitValue: Boolean,
        minPinLengthInitValue: Int,
        scrambledPinIsEnabledInitValue: Boolean
    ) {
        if (initOnceCounter < 1) {
            buildConfigDebug = buildConfigDebugInitValue
            appHasOnBoardProcess = appHasOnBoardProcessInitValue
            hapticFeedbackIsEnabled = hapticFeedbackIsEnabledInitValue
            minPinLength = minPinLengthInitValue
            scrambledPinIsEnabled = scrambledPinIsEnabledInitValue
            loadSettingsFromSharedPreferences()
            initOnceCounter++
        }
    }

    private fun loadSettingsFromSharedPreferences() {
        // Haptic Feedback Is Enabled
        prefs.read(PrefsKeys.HAPTIC_FEEDBACK_IS_ENABLED)?.let {
            hapticFeedbackIsEnabled = it
        }

        // On-board Process Is Complete
        if (appHasOnBoardProcess && prefs.contains(PrefsKeys.ONBOARD_PROCESS_COMPLETE)) {
            onBoardProcessIsComplete = true
        }

        // Scrambled Pin Is Enabled
        prefs.read(PrefsKeys.SCRAMBLED_PIN_IS_ENABLED)?.let {
            scrambledPinIsEnabled = it
        }

        // User Pin Is Set
        if (prefs.contains(PrefsKeys.USER_PIN_IS_SET)) {
            userPinIsSet = true
        }
    }

}