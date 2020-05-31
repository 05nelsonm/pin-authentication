package io.matthewnelson.pin_authentication.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.matthewnelson.pin_authentication.model.HashedPin
import io.matthewnelson.pin_authentication.service.PAActivityAccessPoint
import io.matthewnelson.pin_authentication.service.PinAuthentication
import io.matthewnelson.pin_authentication.service.components.PACoroutines
import io.matthewnelson.pin_authentication.service.components.PAViewColors
import io.matthewnelson.pin_authentication.service.components.PAViewData
import io.matthewnelson.pin_authentication.util.annotations.NotForPublicConsumption
import io.matthewnelson.pin_authentication.util.definitions.PAConfirmPinStatus
import io.matthewnelson.pin_authentication.util.definitions.PAPinEntryState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import javax.inject.Inject

/**
 * @suppress
 * */
@NotForPublicConsumption
class PinAuthenticationActivityViewModel @Inject constructor(
    private val paActivityAP: PAActivityAccessPoint,
    private val paViewColors: PAViewColors,
    private val paViewData: PAViewData,
    private val paCoroutines: PACoroutines
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
            paViewData.setPinLength(0)
            if (clearPinEntryCompare && !isPinEntryCompareEmpty()) {
                pinEntryCompare = ""
            }
        }

        fun dropLastChar() {
            pin = pin.dropLast(1)
            paViewData.setPinLength(pin.length)
        }

        fun addChar(c: Char) {
            pin += c
            paViewData.setPinLength(pin.length)
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
        paViewColors.getBackspaceButtonImageColor()

    fun onBackspacePress() {
        toggleHapticFeedback()
        if (paViewData.getPinLengthValue() > 0) {
            pinEntry.dropLastChar()
        }
    }


    ////////////////////
    // Confirm Button //
    ////////////////////
    fun getConfirmButtonBackgroundColor(): String =
        paViewColors.getConfirmButtonBackgroundColor()

    fun getConfirmButtonImageColor(): String =
        paViewColors.getConfirmButtonImageColor()

    fun getMinPinLength(): Int =
        paActivityAP.getMinPinLength()

    fun onConfirmPress() {

        val hashedPin: HashedPin = pinEntry.getHashedPin(paActivityAP.getPinAuthenticationSalt())

        toggleHapticFeedback()
        when (currentPinEntryState) {
            PAPinEntryState.CONFIRM_PIN -> {
                if (confirmPin(hashedPin)) {
                    paActivityAP.confirmPinToProceedSuccess()
                    paViewData.setPinEntryState(PAPinEntryState.IDLE)
                } else {
                    pinHintContainerShakeAnimation()
                    pinEntry.clear()
                }
            }
            PAPinEntryState.ENABLE_PIN_SECURITY -> {
                setUsersPin(hashedPin)
            }
            PAPinEntryState.RESET_PIN -> {
                if (confirmPin(hashedPin)) {
                    paViewData.setPinEntryState(PAPinEntryState.SET_PIN)
                } else {
                    pinHintContainerShakeAnimation()
                }
                pinEntry.clear()
            }
            PAPinEntryState.SET_PIN -> {
                setUsersPin(hashedPin)
            }
            PAPinEntryState.SET_PIN_FIRST_TIME -> {
                setUsersPin(hashedPin)
            }
            else -> {
                if (confirmPin(hashedPin)) {
                    paActivityAP.authProcessComplete(!pinResetFlowInterrupted)
                    paViewData.setPinEntryState(PAPinEntryState.IDLE)
                } else {
                    pinHintContainerShakeAnimation()
                    pinEntry.clear()
                }
            }
        }
    }

    private fun confirmPin(hashedPin: HashedPin): Boolean {
        if (::lockoutJob.isInitialized && lockoutJob.isActive) return false

        return when (paActivityAP.confirmPin(hashedPin)) {
            PAConfirmPinStatus.LOCKED_OUT -> {
                launchWrongPinLockoutCountdown(paActivityAP.getLockoutDurationSeconds())
                false
            }
            PAConfirmPinStatus.ONE_MORE_ATTEMPT -> {
                paActivityAP.showToast(paViewData.getOneMoreAttemptMessage(), paViewColors.getTextColor())
                false
            }
            PAConfirmPinStatus.WRONG_PIN -> {
                paActivityAP.showToast(paViewData.getWrongPinMessage(), paViewColors.getTextColor())
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
            paViewData.setHeaderTextSetPinStep2()

            // Todo: need to allow for some sort of restart pin reset operation.
        } else {
            if (pinEntry.doPinsMatch()) {
                paActivityAP.setUserPin(hashedPin)
                paActivityAP.authProcessComplete(true)
                paViewData.setPinEntryState(PAPinEntryState.IDLE)
            } else {
                paActivityAP.showToast(paViewData.getPinDoesNotMatchMessage(), paViewColors.getTextColor())
                pinEntry.clear(clearPinEntryCompare = false)
                pinHintContainerShakeAnimation()
            }
        }
    }


    ////////////////////
    // Number Buttons //
    ////////////////////
    fun getPinPadButtonBackgroundColor(): String =
        paViewColors.getPinPadButtonBackgroundColor()

    fun getPinPadIntegers(): LiveData<MutableList<Int>> =
        paViewData.getPinPadIntegers()

    fun onNumPress(int: Int) {
        toggleHapticFeedback()
        when (paViewData.getPinLengthValue()) {
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
        paViewColors.getPinHintContainerColor()

    fun getPinHintImageColor(): String =
        paViewColors.getPinHintImageColor()

    fun getPinLength(): LiveData<Int> =
        paViewData.getPinLength()

    private fun pinHintContainerShakeAnimation() {
        // Todo: shake the pin hint LinearLayout or something
    }


    ///////////////
    // Help Info //
    ///////////////
    fun getPinResetInfoImageColor(): String =
        paViewColors.getPinResetInfoImageColor()

    fun getShowSetPinHelpInfo(): LiveData<String> =
        paViewData.getShowSetPinHelpInfo()


    ///////////
    // Other //
    ///////////
    fun getHeaderText(): LiveData<String> =
        paViewData.getHeaderText()

    fun getScreenBackgroundColor(): String =
        paViewColors.getScreenBackgroundColor()

    fun getTextColor(): String =
        paViewColors.getTextColor()


    ///////////////////////
    // Wrong Pin Lockout //
    ///////////////////////
    private lateinit var lockoutJob: Job

    init {
        if (paActivityAP.isWrongPinLockoutEnabled()) {
            val secondsRemaining = paActivityAP.getLockoutSecondsRemaining()
            if (secondsRemaining > 0) {
                launchWrongPinLockoutCountdown(secondsRemaining)
            } else if (!paViewData.getWrongPinLockoutTime().value.isNullOrEmpty()) {
                paViewData.setWrongPinLockoutTime("")
            }
        }
    }

    fun getWrongPinLockoutTime(): LiveData<String> =
        paViewData.getWrongPinLockoutTime()

    private fun launchWrongPinLockoutCountdown(secondsRemaining: Int) {
        lockoutJob = viewModelScope.launch(paCoroutines.getDispatcherMain()) {
            updateWrongPinLockoutView(secondsRemaining)
        }
    }

    private suspend fun updateWrongPinLockoutView(seconds: Int) {
        var secondsRemaining = seconds
        while (secondsRemaining >= 0) {
            paViewData.setWrongPinLockoutTime("${secondsRemaining}s")
            delay(1000)
            secondsRemaining--
        }
        paViewData.setWrongPinLockoutTime("")
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
        paActivityAP.getBuildConfigDebug()


    /////////////////////
    // Haptic Feedback //
    /////////////////////
    private val hapticFeedback = MutableLiveData<Boolean>()

    fun getHapticFeedback(): LiveData<Boolean> {
        hapticFeedback.value = null
        return hapticFeedback
    }

    private fun toggleHapticFeedback() {
        if (paActivityAP.isHapticFeedBackEnabled()) {
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
     * It will also send the user back to [PAPinEntryState.RESET_PIN] configuration if they are
     * in the middle of the [PAPinEntryState.SET_PIN] process in the event that the app is sent
     * to the background.
     * */
    fun launchProtectUserDataJobIfInactive() {
        if (!::protectUserDataJob.isInitialized || !protectUserDataJob.isActive) {
            protectUserDataJob = viewModelScope.launch(paCoroutines.getDispatcherMain()) {
                protectUserData()
            }
        }
    }

    private fun protectUserData() {
        if (currentPinEntryState == PAPinEntryState.SET_PIN && paActivityAP.isUserPinSet()) {
            paViewData.setPinEntryState(PAPinEntryState.RESET_PIN)
        }
        pinEntry.clear()
    }


    //////////////////////////////
    // Override On Back Pressed //
    //////////////////////////////
    fun getCurrentPinEntryState(): @PAPinEntryState.PinEntryState Int =
        currentPinEntryState

    fun onBackPressed() {
        if (currentPinEntryState == PAPinEntryState.CONFIRM_PIN) {
            paActivityAP.confirmPinToProceedFailure()
        }
        if (currentPinEntryState == PAPinEntryState.ENABLE_PIN_SECURITY) {
            paActivityAP.enablePinSecurityFailure()
        }
        paViewData.setPinEntryState(PAPinEntryState.IDLE)
    }


    /////////////////////
    // Pin Entry Event //
    /////////////////////
    private var currentPinEntryState: @PAPinEntryState.PinEntryState Int = PAPinEntryState.IDLE
    private var pinResetFlowInterrupted = false
    private var pinConfirmationFlowInterrupted = false

    fun getPinEntryState(): LiveData<@PAPinEntryState.PinEntryState Int> =
        paViewData.getPinEntryState()

    fun setPinEntryStateConfirmPin() {
        if (currentPinEntryState != PAPinEntryState.CONFIRM_PIN) {
            currentPinEntryState = PAPinEntryState.CONFIRM_PIN

            if (!pinConfirmationFlowInterrupted) {
                paViewData.setHeaderTextConfirmPin()
                paViewData.setShowSetPinHelpInfo(false)
                paViewData.setPinPadIntegers(paActivityAP.isScrambledPinEnabled())
            }
        }
    }

    fun setPinEntryStateLogin() {
        if (currentPinEntryState != PAPinEntryState.LOGIN) {
            pinEntry.clear()

            if (currentPinEntryState == PAPinEntryState.RESET_PIN ||
                currentPinEntryState == PAPinEntryState.SET_PIN) {
                pinResetFlowInterrupted = true
            }

            if (currentPinEntryState == PAPinEntryState.CONFIRM_PIN) {
                pinConfirmationFlowInterrupted = true
            }

            currentPinEntryState = PAPinEntryState.LOGIN
            paViewData.setHeaderTextLogin()
            paViewData.setShowSetPinHelpInfo(false)
            paViewData.setPinPadIntegers(paActivityAP.isScrambledPinEnabled())
        }
    }

    fun setPinEntryStateResetPin() {
        if (currentPinEntryState != PAPinEntryState.RESET_PIN) {
            currentPinEntryState = PAPinEntryState.RESET_PIN
            paViewData.setHeaderTextResetPin()
            paViewData.setShowSetPinHelpInfo(false)
            paViewData.setPinPadIntegers(paActivityAP.isScrambledPinEnabled())
        }
    }

    fun setPinEntryStateSetPin(pinEntryState: @PAPinEntryState.PinEntryState Int) {
        if (currentPinEntryState != pinEntryState) {
            currentPinEntryState = pinEntryState
            paViewData.setHeaderTextSetPinStep1()
            paViewData.setShowSetPinHelpInfo(true)
            paViewData.setPinPadIntegers(false)
        }
    }

    fun setPinEntryStateIdle() {
        if (currentPinEntryState != PAPinEntryState.IDLE) {
            currentPinEntryState = PAPinEntryState.IDLE
            pinEntry.clear()

            when {
                pinResetFlowInterrupted -> {
                    pinResetFlowInterrupted = false
                    paViewData.setPinEntryState(PAPinEntryState.SET_PIN)
                }
                pinConfirmationFlowInterrupted -> {
                    pinConfirmationFlowInterrupted = false
                    paActivityAP.confirmPinToProceedSuccess()
                }
                else -> {}
            }
        }
    }

}