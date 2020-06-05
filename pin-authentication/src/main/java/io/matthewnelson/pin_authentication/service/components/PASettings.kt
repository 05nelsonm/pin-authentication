package io.matthewnelson.pin_authentication.service.components

import io.matthewnelson.pin_authentication.util.PAPrefsKeys
import io.matthewnelson.encrypted_storage.EncryptedStorage

/**
 * @suppress
 * */
internal class PASettings(private val prefs: EncryptedStorage.Prefs) {

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
        prefs.write(PAPrefsKeys.HAPTIC_FEEDBACK_IS_ENABLED, enable)
    }

    fun setOnBoardProcessIsComplete() {
        onBoardProcessIsComplete = true
        prefs.write(PAPrefsKeys.ONBOARD_PROCESS_COMPLETE, true)
    }

    fun setScrambledPinIsEnabled(enable: Boolean) {
        scrambledPinIsEnabled = enable
        prefs.write(PAPrefsKeys.SCRAMBLED_PIN_IS_ENABLED, enable)
    }

    fun setUserPinIsSet(boolean: Boolean) {
        userPinIsSet = boolean
        if (boolean) {
            prefs.write(PAPrefsKeys.USER_PIN_IS_SET, boolean)
        } else {
            prefs.remove(PAPrefsKeys.USER_PIN_IS_SET)
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
        prefs.read(PAPrefsKeys.HAPTIC_FEEDBACK_IS_ENABLED)?.let {
            hapticFeedbackIsEnabled = it
        }

        // On-board Process Is Complete
        if (appHasOnBoardProcess && prefs.contains(PAPrefsKeys.ONBOARD_PROCESS_COMPLETE)) {
            onBoardProcessIsComplete = true
        }

        // Scrambled Pin Is Enabled
        prefs.read(PAPrefsKeys.SCRAMBLED_PIN_IS_ENABLED)?.let {
            scrambledPinIsEnabled = it
        }

        // User Pin Is Set
        if (prefs.contains(PAPrefsKeys.USER_PIN_IS_SET)) {
            userPinIsSet = true
        }
    }

}