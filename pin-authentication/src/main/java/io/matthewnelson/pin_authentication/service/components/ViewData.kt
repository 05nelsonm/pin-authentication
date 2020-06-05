package io.matthewnelson.pin_authentication.service.components

import android.content.Context
import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.matthewnelson.pin_authentication.R
import io.matthewnelson.pin_authentication.util.definitions.PAPinEntryState

/**
 * @suppress
 * */
internal class ViewData(
    private val context: Context,
    private val settings: Settings,

    // String resources
    @StringRes private val confirmPin: Int,
    @StringRes private val login: Int,
    @StringRes private val resetPin: Int,
    @StringRes private val setPinHelpInfoMin: Int,
    @StringRes private val setPinHelpInfoMax: Int,
    @StringRes private val setPinStep1: Int,
    @StringRes private val setPinStep2: Int,

    // Toast messages
    @StringRes private val oneMoreAttempt: Int,
    @StringRes private val pinDoesNotMatch: Int,
    @StringRes private val wrongPin: Int
) {

    // LiveData
    private val headerText: MutableLiveData<String> = MutableLiveData()
    private val pinEntryState: MutableLiveData<@PAPinEntryState.PinEntryState Int> =
        MutableLiveData(PAPinEntryState.IDLE)
    private val pinLength: MutableLiveData<Int> = MutableLiveData(0)
    private val pinPadIntegers: MutableLiveData<MutableList<Int>> = MutableLiveData()
    private val showSetPinHelpInfo: MutableLiveData<String> = MutableLiveData("")
    private val wrongPinLockoutTime: MutableLiveData<String> = MutableLiveData("")

    private fun getStringResource(@StringRes stringRes: Int): String =
        context.resources.getString(stringRes)

    // Header Text
    fun getHeaderText(): LiveData<String> =
        headerText

    fun setHeaderTextConfirmPin() {
        headerText.value = getStringResource(confirmPin)
    }

    fun setHeaderTextLogin() {
        headerText.value = getStringResource(login)
    }

    fun setHeaderTextResetPin() {
        headerText.value = getStringResource(resetPin)
    }

    fun setHeaderTextSetPinStep1() {
        headerText.value = getStringResource(setPinStep1)
    }

    fun setHeaderTextSetPinStep2() {
        headerText.value = getStringResource(setPinStep2)
    }

    // Pin Entry Event
    fun getPinEntryState(): LiveData<@PAPinEntryState.PinEntryState Int> =
        pinEntryState

    fun setPinEntryState(pinEntryState: @PAPinEntryState.PinEntryState Int) {
        this.pinEntryState.value = pinEntryState
    }

    // Pin Length
    fun getPinLength(): LiveData<Int> =
        pinLength

    fun getPinLengthValue(): Int =
        pinLength.value ?: 0

    fun setPinLength(value: Int) {
        pinLength.value = value
    }

    // Pin Pad Integers
    fun getPinPadIntegers(): LiveData<MutableList<Int>> =
        pinPadIntegers

    fun setPinPadIntegers(scrambleNumbers: Boolean) {
        val mutableList = mutableListOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9)
        if (scrambleNumbers) {
            mutableList.shuffle()
        }
        pinPadIntegers.value = mutableList
    }

    // Show Set Pin Help Info
    fun getShowSetPinHelpInfo(): LiveData<String> =
        showSetPinHelpInfo

    fun setShowSetPinHelpInfo(show: Boolean) {
        var string = ""
        if (show) {
            string = "${getStringResource(R.string.pa_min)} ${settings.getMinPinLength()}" +
                    "\n${getStringResource(R.string.pa_max)}"
        }
        showSetPinHelpInfo.value = string
    }

    // Wrong Pin Lockout Time
    fun getWrongPinLockoutTime(): LiveData<String> =
        wrongPinLockoutTime

    fun setWrongPinLockoutTime(value: String) {
        wrongPinLockoutTime.value = value
    }

    // Toast Messages
    fun getOneMoreAttemptMessage(): String =
        getStringResource(oneMoreAttempt)

    fun getPinDoesNotMatchMessage(): String =
        getStringResource(pinDoesNotMatch)

    fun getWrongPinMessage(): String =
        getStringResource(wrongPin)

}