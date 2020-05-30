package io.matthewnelson.pin_authentication.util

import io.matthewnelson.pin_authentication.service.PinAuthentication
import io.matthewnelson.pin_authentication.util.annotations.NotForPublicConsumption

/**
 * @suppress
 * */
@NotForPublicConsumption
object PAPrefsKeys {

    // Settings
    const val HAPTIC_FEEDBACK_IS_ENABLED = "HAPTIC_FEEDBACK_IS_ENABLED"
    const val ONBOARD_PROCESS_COMPLETE = "ONBOARD_PROCESS_COMPLETE"
    const val PIN_SECURITY_IS_ENABLED = "PIN_SECURITY_IS_ENABLED"
    const val SCRAMBLED_PIN_IS_ENABLED = "SCRAMBLED_PIN_IS_ENABLED"
    const val USER_PIN_IS_SET = "USER_PIN_IS_SET"
    const val WRONG_PIN_OCCURRENCE = "WRONG_PIN_OCCURRENCE"
    const val WRONG_PIN_ATTEMPT_COUNTER = "WRONG_PIN_ATTEMPT_COUNTER"

    // Custom Colors
    const val CUSTOM_COLORS_ARE_SET = "CUSTOM_COLORS_ARE_SET"
    const val BACKSPACE_BUTTON_IMAGE_COLOR = "BACKSPACE_BUTTON_IMAGE_COLOR"
    const val CONFIRM_BUTTON_BACKGROUND_COLOR = "CONFIRM_BUTTON_BACKGROUND_COLOR"
    const val CONFIRM_BUTTON_IMAGE_COLOR = "CONFIRM_BUTTON_IMAGE_COLOR"
    const val PIN_HINT_CONTAINER_COLOR = "PIN_HINT_CONTAINER_COLOR"
    const val PIN_HINT_IMAGE_COLOR = "PIN_HINT_IMAGE_COLOR"
    const val PIN_PAD_BUTTON_BACKGROUND_COLOR = "PIN_PAD_BUTTON_BACKGROUND_COLOR"
    const val PIN_RESET_INFO_IMAGE_COLOR = "PIN_RESET_INFO_IMAGE_COLOR"
    const val SCREEN_BACKGROUND_COLOR = "SCREEN_BACKGROUND_COLOR"
    const val TEXT_COLOR = "TEXT_COLOR"

    /**
     * Keys for EncryptedSharedPreferences
     *
     * If adding a key below, be sure to update the following methods:
     * @see [PinAuthentication.EncryptedPrefs.clear]
     * @see [PinAuthentication.EncryptedPrefs.getAll]
     *
     * */
    const val USER_PIN = "USER_PIN"
    const val PIN_AUTHENTICATION_SALT = "PIN_AUTHENTICATION_SALT"

    private val blacklistedPrefsKeys = arrayOf(USER_PIN, PIN_AUTHENTICATION_SALT)

    /**
     * @suppress
     * Checks if methods being called contain keys used by [PinAuthentication], and will return
     * `false`, if that is the case. Ensures that data which [PinAuthentication] needs remains
     * in tact and unabated.
     * @param [key] String
     *
     * @return Boolean
     * */
    fun isPrefsKeyBlacklisted(key: String): Boolean =
        blacklistedPrefsKeys.contains(key)

    fun getBlacklistedKeys(): Array<String> =
        blacklistedPrefsKeys

}