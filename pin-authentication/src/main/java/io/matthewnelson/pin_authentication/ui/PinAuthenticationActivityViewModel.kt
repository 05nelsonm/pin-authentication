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
package io.matthewnelson.pin_authentication.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.matthewnelson.pin_authentication.model.HashedPin
import io.matthewnelson.pin_authentication.service.AuthenticationActivityAccessPoint
import io.matthewnelson.pin_authentication.service.PinAuthentication
import io.matthewnelson.pin_authentication.service.components.Coroutines
import io.matthewnelson.pin_authentication.service.components.ViewColors
import io.matthewnelson.pin_authentication.service.components.ViewData
import io.matthewnelson.pin_authentication.util.definitions.ConfirmPinStatuss.ConfirmPinStatus
import io.matthewnelson.pin_authentication.util.definitions.PinEntryStates.PinEntryState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import javax.inject.Inject

/**
 * @suppress
 * */
internal class PinAuthenticationActivityViewModel @Inject constructor(
    private val authenticationActivityAP: AuthenticationActivityAccessPoint,
    private val viewColors: ViewColors,
    private val viewData: ViewData,
    private val coroutines: Coroutines
) : ViewModel() {

    private val pinEntry = PinEntry()

    private inner class PinEntry(private var pin: String = "") {

        private lateinit var pinEntryCompare: String

        fun isPinEntryCompareEmpty(): Boolean =
            if (::pinEntryCompare.isInitialized) pinEntryCompare.isEmpty() else true

        fun setPinEntryCompare() {
            val pinTemp = pin
            this.clear()
            pinEntryCompare = pinTemp
        }

        fun doPinsMatch(): Boolean =
            if (::pinEntryCompare.isInitialized) pin == pinEntryCompare else false

        fun clear(clearPinEntryCompare: Boolean = true) {
            pin = ""
            viewData.setPinLength(0)
            if (clearPinEntryCompare && !isPinEntryCompareEmpty()) {
                pinEntryCompare = ""
            }
        }

        fun dropLastChar() {
            pin = pin.dropLast(1)
            viewData.setPinLength(pin.length)
        }

        fun addChar(c: Char) {
            pin += c
            viewData.setPinLength(pin.length)
        }

        fun getHashedPin(pinAuthenticationSalt: String): HashedPin =
            HashedPin(getSha256Hash(pinAuthenticationSalt) ?: "$pin$pinAuthenticationSalt")

        private fun getSha256Hash(pinAuthenticationSalt: String): String? =
            try {
                val digest = MessageDigest.getInstance("SHA-256")
                digest.reset()
                bin2hex(digest.digest("$pin$pinAuthenticationSalt".toByteArray()))
            } catch (e: NoSuchAlgorithmException) {
                e.printStackTrace()
                null
            }

        private fun bin2hex(data: ByteArray): String {
            val hex = StringBuilder(data.size * 2)
            for (b in data) hex.append(String.format("%02x", b, 0xFF))
            return hex.toString()
        }

    }


    //////////////////////////////////////////////////
    //////////////////////////////////////////////////
    //////// PinAuthenticationActivity Layout ////////
    //////////////////////////////////////////////////
    //////////////////////////////////////////////////

    //////////////////////
    // Backspace Button //
    //////////////////////
    fun getBackspaceButtonImageColor(): String =
        viewColors.getBackspaceButtonImageColor()

    fun onBackspacePress() {
        toggleHapticFeedback()
        if (viewData.getPinLengthValue() > 0) {
            pinEntry.dropLastChar()
        }
    }


    ////////////////////
    // Confirm Button //
    ////////////////////
    fun getConfirmButtonBackgroundColor(): String =
        viewColors.getConfirmButtonBackgroundColor()

    fun getConfirmButtonImageColor(): String =
        viewColors.getConfirmButtonImageColor()

    fun getMinPinLength(): Int =
        authenticationActivityAP.getMinPinLength()

    fun onConfirmPress() {

        val hashedPin: HashedPin = pinEntry.getHashedPin(authenticationActivityAP.getPinAuthenticationSalt())

        toggleHapticFeedback()
        when (currentPinEntryState) {
            PinEntryState.CONFIRM_PIN -> {
                if (confirmPin(hashedPin)) {
                    authenticationActivityAP.confirmPinToProceedSuccess()
                    viewData.setPinEntryState(PinEntryState.IDLE)
                } else {
                    pinHintContainerShakeAnimation()
                    pinEntry.clear()
                }
            }
            PinEntryState.ENABLE_PIN_SECURITY -> {
                setUsersPin(hashedPin)
            }
            PinEntryState.RESET_PIN -> {
                if (confirmPin(hashedPin)) {
                    viewData.setPinEntryState(PinEntryState.SET_PIN)
                } else {
                    pinHintContainerShakeAnimation()
                }
                pinEntry.clear()
            }
            PinEntryState.SET_PIN -> {
                setUsersPin(hashedPin)
            }
            PinEntryState.SET_PIN_FIRST_TIME -> {
                setUsersPin(hashedPin)
            }
            else -> {
                if (confirmPin(hashedPin)) {
                    authenticationActivityAP.authProcessComplete(!pinResetFlowInterrupted)
                    viewData.setPinEntryState(PinEntryState.IDLE)
                } else {
                    pinHintContainerShakeAnimation()
                    pinEntry.clear()
                }
            }
        }
    }

    private fun confirmPin(hashedPin: HashedPin): Boolean {
        if (::lockoutJob.isInitialized && lockoutJob.isActive) return false

        return when (authenticationActivityAP.confirmPin(hashedPin)) {
            ConfirmPinStatus.LOCKED_OUT -> {
                launchWrongPinLockoutCountdown(authenticationActivityAP.getLockoutDurationSeconds())
                false
            }
            ConfirmPinStatus.ONE_MORE_ATTEMPT -> {
                authenticationActivityAP.showToast(viewData.getOneMoreAttemptMessage(), viewColors.getTextColor())
                false
            }
            ConfirmPinStatus.WRONG_PIN -> {
                authenticationActivityAP.showToast(viewData.getWrongPinMessage(), viewColors.getTextColor())
                false
            }
            else -> {
                true
            }
        }
    }

    private fun setUsersPin(hashedPin: HashedPin) {
        if (pinEntry.isPinEntryCompareEmpty()) {
            pinEntry.setPinEntryCompare()
            viewData.setHeaderTextSetPinStep2()

            // Todo: need to allow for some sort of restart pin reset operation.
        } else {
            if (pinEntry.doPinsMatch()) {
                authenticationActivityAP.setUserPin(hashedPin)
                authenticationActivityAP.authProcessComplete(true)
                viewData.setPinEntryState(PinEntryState.IDLE)
            } else {
                authenticationActivityAP.showToast(viewData.getPinDoesNotMatchMessage(), viewColors.getTextColor())
                pinEntry.clear(clearPinEntryCompare = false)
                pinHintContainerShakeAnimation()
            }
        }
    }


    ////////////////////
    // Number Buttons //
    ////////////////////
    fun getPinPadButtonBackgroundColor(): String =
        viewColors.getPinPadButtonBackgroundColor()

    fun getPinPadIntegers(): LiveData<MutableList<Int>> =
        viewData.getPinPadIntegers()

    fun onNumPress(int: Int) {
        toggleHapticFeedback()
        when (viewData.getPinLengthValue()) {
            14 -> {
                pinHintContainerShakeAnimation()
            }
            else -> {
                pinEntry.addChar(int.toChar())
            }
        }
    }


    //////////////
    // Pin Hint //
    //////////////
    fun getPinHintContainerColor(): String =
        viewColors.getPinHintContainerColor()

    fun getPinHintImageColor(): String =
        viewColors.getPinHintImageColor()

    fun getPinLength(): LiveData<Int> =
        viewData.getPinLength()

    private fun pinHintContainerShakeAnimation() {
        // Todo: shake the pin hint LinearLayout or something
    }


    ///////////////
    // Help Info //
    ///////////////
    fun getPinResetInfoImageColor(): String =
        viewColors.getPinResetInfoImageColor()

    fun getShowSetPinHelpInfo(): LiveData<String> =
        viewData.getShowSetPinHelpInfo()


    ///////////
    // Other //
    ///////////
    fun getHeaderText(): LiveData<String> =
        viewData.getHeaderText()

    fun getScreenBackgroundColor(): String =
        viewColors.getScreenBackgroundColor()

    fun getTextColor(): String =
        viewColors.getTextColor()


    ///////////////////////
    // Wrong Pin Lockout //
    ///////////////////////
    private lateinit var lockoutJob: Job

    init {
        if (authenticationActivityAP.isWrongPinLockoutEnabled()) {
            val secondsRemaining = authenticationActivityAP.getLockoutSecondsRemaining()
            if (secondsRemaining > 0) {
                launchWrongPinLockoutCountdown(secondsRemaining)
            } else if (!viewData.getWrongPinLockoutTime().value.isNullOrEmpty()) {
                viewData.setWrongPinLockoutTime("")
            }
        }
    }

    fun getWrongPinLockoutTime(): LiveData<String> =
        viewData.getWrongPinLockoutTime()

    private fun launchWrongPinLockoutCountdown(secondsRemaining: Int) {
        lockoutJob = viewModelScope.launch(coroutines.getDispatcherMain()) {
            updateWrongPinLockoutView(secondsRemaining)
        }
    }

    private suspend fun updateWrongPinLockoutView(seconds: Int) {
        var secondsRemaining = seconds
        while (secondsRemaining >= 0) {
            viewData.setWrongPinLockoutTime("${secondsRemaining}s")
            delay(1000)
            secondsRemaining--
        }
        viewData.setWrongPinLockoutTime("")
    }


    ///////////////////////////////////////////
    ///////////////////////////////////////////
    //////// PinAuthenticationActivity ////////
    ///////////////////////////////////////////
    ///////////////////////////////////////////

    ////////////////////////
    // Window Flag Secure //
    ////////////////////////
    fun getBuildConfigDebug(): Boolean =
        authenticationActivityAP.getBuildConfigDebug()


    /////////////////////
    // Haptic Feedback //
    /////////////////////
    private val hapticFeedback = MutableLiveData<Boolean>()

    fun getHapticFeedback(): LiveData<Boolean> {
        hapticFeedback.value = null
        return hapticFeedback
    }

    private fun toggleHapticFeedback() {
        if (authenticationActivityAP.isHapticFeedBackEnabled()) {
            hapticFeedback.value = hapticFeedback.value != true
        }
    }


    ///////////////////////
    // Protect User Data //
    ///////////////////////
    private lateinit var protectUserDataJob: Job

    fun cancelProtectUserDataJobIfNotComplete() {
        if (::protectUserDataJob.isInitialized && protectUserDataJob.isActive) {
            protectUserDataJob.cancel()
        }
    }

    /**
     * This will clear the pin as soon as the app is sent to the background.
     *
     * It ensures that if the user is doing something with [PinAuthentication] while the
     * app is sent to the background that no information is compromised.
     *
     * It will also send the user back to [PinEntryState.RESET_PIN] configuration if they are
     * in the middle of the [PinEntryState.SET_PIN] process in the event that the app is sent
     * to the background.
     * */
    fun launchProtectUserDataJobIfInactive() {
        if (!::protectUserDataJob.isInitialized || !protectUserDataJob.isActive) {
            protectUserDataJob = viewModelScope.launch(coroutines.getDispatcherMain()) {
                protectUserData()
            }
        }
    }

    private fun protectUserData() {
        if (currentPinEntryState == PinEntryState.SET_PIN && authenticationActivityAP.isUserPinSet()) {
            viewData.setPinEntryState(PinEntryState.RESET_PIN)
            pinEntry.clear()
        } else {
            pinEntry.clear(clearPinEntryCompare = false)
        }
    }


    //////////////////////////////
    // Override On Back Pressed //
    //////////////////////////////
    fun getCurrentPinEntryState(): @PinEntryState Int =
        currentPinEntryState

    fun onBackPressed() {
        if (currentPinEntryState == PinEntryState.CONFIRM_PIN) {
            authenticationActivityAP.confirmPinToProceedFailure()
        }
        if (currentPinEntryState == PinEntryState.ENABLE_PIN_SECURITY) {
            authenticationActivityAP.enablePinSecurityFailure()
        }
        viewData.setPinEntryState(PinEntryState.IDLE)
    }


    /////////////////////
    // Pin Entry Event //
    /////////////////////
    private var currentPinEntryState: @PinEntryState Int = PinEntryState.IDLE
    private var pinResetFlowInterrupted = false
    private var pinConfirmationFlowInterrupted = false

    fun getPinEntryState(): LiveData<@PinEntryState Int> =
        viewData.getPinEntryState()

    fun setPinEntryStateConfirmPin() {
        if (currentPinEntryState != PinEntryState.CONFIRM_PIN) {
            currentPinEntryState = PinEntryState.CONFIRM_PIN

            if (!pinConfirmationFlowInterrupted) {
                viewData.setHeaderTextConfirmPin()
                viewData.setShowSetPinHelpInfo(false)
                viewData.setPinPadIntegers(authenticationActivityAP.isScrambledPinEnabled())
            }
        }
    }

    fun setPinEntryStateLogin() {
        if (currentPinEntryState != PinEntryState.LOGIN) {
            pinEntry.clear()

            if (currentPinEntryState == PinEntryState.RESET_PIN ||
                currentPinEntryState == PinEntryState.SET_PIN) {
                pinResetFlowInterrupted = true
            }

            if (currentPinEntryState == PinEntryState.CONFIRM_PIN) {
                pinConfirmationFlowInterrupted = true
            }

            currentPinEntryState = PinEntryState.LOGIN
            viewData.setHeaderTextLogin()
            viewData.setShowSetPinHelpInfo(false)
            viewData.setPinPadIntegers(authenticationActivityAP.isScrambledPinEnabled())
        }
    }

    fun setPinEntryStateResetPin() {
        if (currentPinEntryState != PinEntryState.RESET_PIN) {
            currentPinEntryState = PinEntryState.RESET_PIN
            viewData.setHeaderTextResetPin()
            viewData.setShowSetPinHelpInfo(false)
            viewData.setPinPadIntegers(authenticationActivityAP.isScrambledPinEnabled())
        }
    }

    fun setPinEntryStateSetPin(pinEntryState: @PinEntryState Int) {
        if (currentPinEntryState != pinEntryState) {
            currentPinEntryState = pinEntryState
            viewData.setHeaderTextSetPinStep1()
            viewData.setShowSetPinHelpInfo(true)
            viewData.setPinPadIntegers(false)
        }
    }

    fun setPinEntryStateIdle() {
        if (currentPinEntryState != PinEntryState.IDLE) {
            currentPinEntryState = PinEntryState.IDLE
            pinEntry.clear()

            when {
                pinResetFlowInterrupted -> {
                    pinResetFlowInterrupted = false
                    viewData.setPinEntryState(PinEntryState.SET_PIN)
                }
                pinConfirmationFlowInterrupted -> {
                    pinConfirmationFlowInterrupted = false
                    authenticationActivityAP.confirmPinToProceedSuccess()
                }
                else -> {}
            }
        }
    }

}