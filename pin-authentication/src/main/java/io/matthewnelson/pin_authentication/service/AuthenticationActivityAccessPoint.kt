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
package io.matthewnelson.pin_authentication.service

import android.graphics.Color
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import io.matthewnelson.pin_authentication.R
import io.matthewnelson.pin_authentication.service.components.*
import io.matthewnelson.pin_authentication.util.PrefsKeys
import io.matthewnelson.pin_authentication.util.definitions.ConfirmPinStatuss.ConfirmPinStatus
import io.matthewnelson.pin_authentication.util.definitions.PinEntryStates.PinEntryState
import io.matthewnelson.encrypted_storage.Prefs
import io.matthewnelson.pin_authentication.model.HashedPin
import io.matthewnelson.pin_authentication.model.UnsafePinHash
import java.math.BigInteger
import java.security.SecureRandom

/**
 * Methods utilized by [PinAuthentication]'s UI, _not_ needed by the Application
 * that's implementing [PinAuthentication]
 *
 * @param [appLifecycleWatcher] [AppLifecycleWatcher]
 * @param [appLockObserver] [AppLockObserver]
 * @param [confirmPinToProceed] [ConfirmPinToProceed]
 * @param [initialAppLogin] [InitialAppLogin]
 * @param [pinSecurity] [PinSecurity]
 * @param [settings] [Settings]
 * @param [wrongPinLockout] [WrongPinLockout]
 * @param [encryptedPrefs] [Prefs]
 * */
internal class AuthenticationActivityAccessPoint(
    private val appLifecycleWatcher: AppLifecycleWatcher,
    private val appLockObserver: AppLockObserver,
    private val confirmPinToProceed: ConfirmPinToProceed,
    private val initialAppLogin: InitialAppLogin,
    private val pinSecurity: PinSecurity,
    private val settings: Settings,
    private val wrongPinLockout: WrongPinLockout,
    private val encryptedPrefs: Prefs
) {

    fun authProcessComplete(finishActivity: Boolean) {
        appLockObserver.setAuthenticationStateNotRequired()
        if (finishActivity) {
            finishPinAuthenticationActivity()
        }
        initialAppLogin.initialAppLoginIsSatisfied()
    }

    /**
     * Confirm's the user's pin
     *
     * @param [hashedPin] HashedPin
     *
     * @return [ConfirmPinStatuss.ConfirmPinStatus]
     * */
    fun confirmPin(hashedPin: HashedPin): @ConfirmPinStatus Int {

        @OptIn(UnsafePinHash::class)
        if (encryptedPrefs.read(PrefsKeys.USER_PIN, Prefs.INVALID_STRING) == hashedPin.hashedPin) {

            if (wrongPinLockout.isWrongPinLockoutEnabled()) {
                wrongPinLockout.removeLockoutData()
            }
            return ConfirmPinStatus.CORRECT_PIN
        } else {

            return if (wrongPinLockout.isWrongPinLockoutEnabled()) {
                wrongPinLockout.variablyDecrementAttemptsCounter()
                wrongPinLockout.updateDataAndGetReturnString()
            } else {
                ConfirmPinStatus.WRONG_PIN
            }

        }
    }

    /**
     * Used during [PinEntryState.CONFIRM_PIN] configuration. Will be triggered if the
     * user hits the back button.
     * */
    fun confirmPinToProceedFailure() {
        confirmPinToProceed.setRequestKeyValueOfCurrentRequestKeyTo(
            pinSecurity.isCurrentRequestKeyPinSecurity()
        )

        confirmPinToProceed.setCurrentRequestKey("")
    }

    /**
     * Used during [PinEntryState.CONFIRM_PIN] configuration. Will be triggered if the
     * user successfully confirms their pin.
     * */
    fun confirmPinToProceedSuccess() {
        if (pinSecurity.isCurrentRequestKeyPinSecurity()) {
            pinSecurity.disablePinSecuritySuccess()
        } else {
            confirmPinToProceed.setRequestKeyValueOfCurrentRequestKeyTo(true)
        }

        confirmPinToProceed.setCurrentRequestKey("")

        finishPinAuthenticationActivity()
    }

    /**
     * Used during [PinEntryState.ENABLE_PIN_SECURITY] configuration. Will be triggered
     * if the user hits the back button.
     * */
    fun enablePinSecurityFailure() =
        pinSecurity.enablePinSecurityFailure()

    fun getPinAuthenticationSalt(): String {
        encryptedPrefs.read(PrefsKeys.PIN_AUTHENTICATION_SALT, Prefs.INVALID_STRING).let {
            return if (it == null || it == Prefs.INVALID_STRING) {
                createAuthenticationManagerSalt()
            } else {
                it
            }
        }
    }

    private fun createAuthenticationManagerSalt(): String {
        val salt = BigInteger(130, SecureRandom()).toString(32)
        encryptedPrefs.write(PrefsKeys.PIN_AUTHENTICATION_SALT, salt)
        return salt
    }

    fun getBuildConfigDebug(): Boolean =
        settings.getBuildConfigDebug()

    fun getLockoutDurationSeconds(): Int =
        wrongPinLockout.getLockoutDurationSeconds()

    fun getLockoutSecondsRemaining(): Int =
        wrongPinLockout.getSecondsRemaining()

    fun getMinPinLength(): Int =
        settings.getMinPinLength()

    fun isHapticFeedBackEnabled(): Boolean =
        settings.getHapticFeedbackIsEnabled()

    fun isScrambledPinEnabled(): Boolean =
        settings.getScrambledPinIsEnabled()

    fun isUserPinSet(): Boolean =
        settings.getUserPinIsSet()

    fun isWrongPinLockoutEnabled(): Boolean =
        wrongPinLockout.isWrongPinLockoutEnabled()

    /**
     * Sets the user's pin
     *
     * @param [hashedPin] String
     * */
    fun setUserPin(hashedPin: HashedPin) {

        @OptIn(UnsafePinHash::class)
        encryptedPrefs.write(PrefsKeys.USER_PIN, hashedPin.hashedPin)

        if (!settings.getUserPinIsSet()) {
            settings.setUserPinIsSet(true)
        }

        if (pinSecurity.isCurrentRequestKeyPinSecurity()) {
            pinSecurity.enablePinSecuritySuccess()
        }

    }

    private lateinit var previousToast: Toast
    /**
     * Shows a toast using the current Activity.
     * @param [message] String
     * */
    @Suppress("DEPRECATION")
    fun showToast(message: String, textColorHex: String) {

        appLifecycleWatcher.getCurrentActivity()?.apply {
            val backgroundColorValue = ContextCompat.getColor(this, R.color.pa_transparent)

            val toast = Toast.makeText(this, message, Toast.LENGTH_SHORT)

            toast.view?.setBackgroundColor(backgroundColorValue)
            toast.view?.findViewById<TextView>(android.R.id.message)?.setTextColor(Color.parseColor(textColorHex))
            val toastIcon = toast.view?.findViewById<ImageView>(android.R.id.icon)
            if (toastIcon != null) {
                toastIcon.visibility = View.GONE
            }
            if (::previousToast.isInitialized && previousToast.view?.isShown == true) {
                previousToast.cancel()
            }
            previousToast = toast
            toast.show()
        }
    }

    private fun finishPinAuthenticationActivity() {
        if (appLifecycleWatcher.isCurrentActivityPinAuthenticationActivity()) {
            appLifecycleWatcher.getCurrentActivity()?.apply {
                finish()
            }
        }
    }

}