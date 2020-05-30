package io.matthewnelson.pin_authentication.service

import android.graphics.Color
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import io.matthewnelson.pin_authentication.R
import io.matthewnelson.pin_authentication.service.components.*
import io.matthewnelson.pin_authentication.util.PAPrefsKeys
import io.matthewnelson.pin_authentication.util.definitions.PAConfirmPinStatus
import io.matthewnelson.pin_authentication.util.definitions.PAPinEntryState
import io.matthewnelson.encrypted_storage.EncryptedStorage
import io.matthewnelson.pin_authentication.model.HashedPin
import io.matthewnelson.pin_authentication.util.annotations.NotForPublicConsumption
import io.matthewnelson.pin_authentication.util.annotations.UnsafePinHash
import java.math.BigInteger
import java.security.SecureRandom

/**
 * @suppress
 * Methods utilized by [PinAuthentication]'s UI, _not_ needed by the Application
 * that's implementing [PinAuthentication]
 *
 * @param [paAppLifecycleWatcher] [PAAppLifecycleWatcher]
 * @param [paAppLockObserver] [PAAppLockObserver]
 * @param [paConfirmPinToProceed] [PAConfirmPinToProceed]
 * @param [paInitialAppLogin] [PAInitialAppLogin]
 * @param [paPinSecurity] [PAPinSecurity]
 * @param [paSettings] [PASettings]
 * @param [paWrongPinLockout] [PAWrongPinLockout]
 * @param [encryptedPrefs] [EncryptedStorage.Prefs]
 * */
@NotForPublicConsumption
class PAActivityAccessPoint(
    private val paAppLifecycleWatcher: PAAppLifecycleWatcher,
    private val paAppLockObserver: PAAppLockObserver,
    private val paConfirmPinToProceed: PAConfirmPinToProceed,
    private val paInitialAppLogin: PAInitialAppLogin,
    private val paPinSecurity: PAPinSecurity,
    private val paSettings: PASettings,
    private val paWrongPinLockout: PAWrongPinLockout,
    private val encryptedPrefs: EncryptedStorage.Prefs
) {

    fun authProcessComplete(finishActivity: Boolean) {
        paAppLockObserver.setAuthenticationStateNotRequired()
        if (finishActivity) {
            finishPinAuthenticationActivity()
        }
        paInitialAppLogin.initialAppLoginIsSatisfied()
    }

    /**
     * Confirm's the user's pin
     *
     * @param [hashedPin] HashedPin
     *
     * @return [PAConfirmPinStatus.ConfirmPinStatus]
     * */
    fun confirmPin(hashedPin: HashedPin): @PAConfirmPinStatus.ConfirmPinStatus Int {

        @OptIn(UnsafePinHash::class)
        if (encryptedPrefs.read(
                PAPrefsKeys.USER_PIN,
                EncryptedStorage.Prefs.INVALID_STRING) == hashedPin.hashedPin
        ) {

            if (paWrongPinLockout.isWrongPinLockoutEnabled()) {
                paWrongPinLockout.removeLockoutData()
            }
            return PAConfirmPinStatus.CORRECT_PIN
        } else {

            return if (paWrongPinLockout.isWrongPinLockoutEnabled()) {
                paWrongPinLockout.variablyDecrementAttemptsCounter()
                paWrongPinLockout.updateDataAndGetReturnString()
            } else {
                PAConfirmPinStatus.WRONG_PIN
            }

        }
    }

    /**
     * Used during [PAPinEntryState.CONFIRM_PIN] configuration. Will be triggered if the
     * user hits the back button.
     * */
    fun confirmPinToProceedFailure() {
        paConfirmPinToProceed.setRequestKeyValueOfCurrentRequestKeyTo(
            paPinSecurity.isCurrentRequestKeyPinSecurity()
        )

        paConfirmPinToProceed.setCurrentRequestKey("")
    }

    /**
     * Used during [PAPinEntryState.CONFIRM_PIN] configuration. Will be triggered if the
     * user successfully confirms their pin.
     * */
    fun confirmPinToProceedSuccess() {
        if (paPinSecurity.isCurrentRequestKeyPinSecurity()) {
            paPinSecurity.disablePinSecuritySuccess()
        } else {
            paConfirmPinToProceed.setRequestKeyValueOfCurrentRequestKeyTo(true)
        }

        paConfirmPinToProceed.setCurrentRequestKey("")

        finishPinAuthenticationActivity()
    }

    /**
     * Used during [PAPinEntryState.ENABLE_PIN_SECURITY] configuration. Will be triggered
     * if the user hits the back button.
     * */
    fun enablePinSecurityFailure() =
        paPinSecurity.enablePinSecurityFailure()

    fun getPinAuthenticationSalt(): String {
        encryptedPrefs.read(PAPrefsKeys.PIN_AUTHENTICATION_SALT, EncryptedStorage.Prefs.INVALID_STRING).let {
            return if (it == null || it == EncryptedStorage.Prefs.INVALID_STRING) {
                createAuthenticationManagerSalt()
            } else {
                it
            }
        }
    }

    private fun createAuthenticationManagerSalt(): String {
        val salt = BigInteger(130, SecureRandom()).toString(32)
        encryptedPrefs.write(PAPrefsKeys.PIN_AUTHENTICATION_SALT, salt)
        return salt
    }

    fun getBuildConfigDebug(): Boolean =
        paSettings.getBuildConfigDebug()

    fun getLockoutDurationSeconds(): Int =
        paWrongPinLockout.getLockoutDurationSeconds()

    fun getLockoutSecondsRemaining(): Int =
        paWrongPinLockout.getSecondsRemaining()

    fun getMinPinLength(): Int =
        paSettings.getMinPinLength()

    fun isHapticFeedBackEnabled(): Boolean =
        paSettings.getHapticFeedbackIsEnabled()

    fun isScrambledPinEnabled(): Boolean =
        paSettings.getScrambledPinIsEnabled()

    fun isUserPinSet(): Boolean =
        paSettings.getUserPinIsSet()

    fun isWrongPinLockoutEnabled(): Boolean =
        paWrongPinLockout.isWrongPinLockoutEnabled()

    /**
     * Sets the user's pin
     *
     * @param [hashedPin] String
     * */
    fun setUserPin(hashedPin: HashedPin) {

        @OptIn(UnsafePinHash::class)
        encryptedPrefs.write(PAPrefsKeys.USER_PIN, hashedPin.hashedPin)

        if (!paSettings.getUserPinIsSet()) {
            paSettings.setUserPinIsSet(true)
        }

        if (paPinSecurity.isCurrentRequestKeyPinSecurity()) {
            paPinSecurity.enablePinSecuritySuccess()
        }

    }

    private lateinit var previousToast: Toast
    /**
     * Shows a toast using the current Activity.
     * @param [message] String
     * */
    fun showToast(message: String, textColorHex: String) {

        paAppLifecycleWatcher.getCurrentActivity()?.apply {
            val backgroundColorValue = ContextCompat.getColor(this, R.color.pa_transparent)

            val toast = Toast.makeText(this, message, Toast.LENGTH_SHORT)

            toast.view.setBackgroundColor(backgroundColorValue)
            toast.view.findViewById<TextView>(android.R.id.message).setTextColor(Color.parseColor(textColorHex))
            val toastIcon = toast.view.findViewById<ImageView>(android.R.id.icon)
            if (toastIcon != null) {
                toastIcon.visibility = View.GONE
            }
            if (::previousToast.isInitialized && previousToast.view.isShown) {
                previousToast.cancel()
            }
            previousToast = toast
            toast.show()
        }
    }

    private fun finishPinAuthenticationActivity() {
        if (paAppLifecycleWatcher.isCurrentActivityPinAuthenticationActivity()) {
            paAppLifecycleWatcher.getCurrentActivity()?.apply {
                finish()
            }
        }
    }

}