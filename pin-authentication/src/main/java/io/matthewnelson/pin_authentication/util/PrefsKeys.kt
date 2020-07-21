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
package io.matthewnelson.pin_authentication.util

/**
 * @suppress
 * */
internal object PrefsKeys {

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
     * */
    const val USER_PIN = "USER_PIN"
    const val PIN_AUTHENTICATION_SALT = "PIN_AUTHENTICATION_SALT"

}