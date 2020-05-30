package io.matthewnelson.pin_authentication.service.components

import androidx.lifecycle.LiveData
import io.matthewnelson.pin_authentication.util.PAPrefsKeys
import io.matthewnelson.encrypted_storage.EncryptedStorage
import io.matthewnelson.pin_authentication.util.annotations.NotForPublicConsumption

/**
 * @suppress
 * Wrapper class for [PAConfirmPinToProceed] that handles everything to do with PinSecurity
 *
 * @param [paConfirmPinToProceed] [PAConfirmPinToProceed]
 * @param [paSettings] [PASettings]
 * @param [encryptedPrefs] [EncryptedStorage.Prefs]
 * @param [prefs] [EncryptedStorage.Prefs]
 * */
@NotForPublicConsumption
class PAPinSecurity(
    private val paConfirmPinToProceed: PAConfirmPinToProceed,
    private val paSettings: PASettings,
    private val encryptedPrefs: EncryptedStorage.Prefs,
    private val prefs: EncryptedStorage.Prefs
) {

    init {
        paConfirmPinToProceed.registerRequestKey(
            PAPrefsKeys.PIN_SECURITY_IS_ENABLED,
            false,
            addToBlacklist = true
        )
    }


    /////////////////////////
    // Modify Pin Security //
    /////////////////////////

    fun disablePinSecuritySuccess() {
        encryptedPrefs.remove(PAPrefsKeys.USER_PIN)
        encryptedPrefs.remove(PAPrefsKeys.PIN_AUTHENTICATION_SALT)
        paSettings.setUserPinIsSet(false)
        prefs.write(PAPrefsKeys.PIN_SECURITY_IS_ENABLED, false)
        setPinSecurityValue(false)
        paConfirmPinToProceed.setAllRequestKeysTo(true)
    }

    fun enablePinSecurityFailure() {
        setPinSecurityValue(false)
        paConfirmPinToProceed.setCurrentRequestKey("")
    }

    fun enablePinSecuritySuccess() {
        prefs.write(PAPrefsKeys.PIN_SECURITY_IS_ENABLED, true)
        setPinSecurityValue(true)
        paConfirmPinToProceed.setAllRequestKeysTo(false)
        paConfirmPinToProceed.setCurrentRequestKey("")
    }

    fun getPinSecurity(): LiveData<Boolean>? =
        paConfirmPinToProceed.getValueOfRequestKey(PAPrefsKeys.PIN_SECURITY_IS_ENABLED)

    fun isCurrentRequestKeyPinSecurity(): Boolean =
        paConfirmPinToProceed.getCurrentRequestKey() == PAPrefsKeys.PIN_SECURITY_IS_ENABLED

    fun isPinSecurityEnabled(): Boolean =
        paConfirmPinToProceed.getValueOfRequestKey(
            PAPrefsKeys.PIN_SECURITY_IS_ENABLED
        )?.value ?: false

    fun setCurrentRequestKeyToPinSecurity() {
        paConfirmPinToProceed.setCurrentRequestKey(PAPrefsKeys.PIN_SECURITY_IS_ENABLED)
    }

    fun setPinSecurityValue(value: Boolean) {
        paConfirmPinToProceed.setRequestKeyValueTo(PAPrefsKeys.PIN_SECURITY_IS_ENABLED, value)
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
        prefs.read(PAPrefsKeys.PIN_SECURITY_IS_ENABLED)?.let {
            setPinSecurityValue(it)
        }
    }

}