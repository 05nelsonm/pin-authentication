package io.matthewnelson.pin_authentication.service.components

import android.content.Context
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import io.matthewnelson.pin_authentication.R
import io.matthewnelson.pin_authentication.util.PAPrefsKeys
import io.matthewnelson.encrypted_storage.EncryptedStorage
import io.matthewnelson.pin_authentication.util.annotations.NotForPublicConsumption

/**
 * @suppress
 * */
@NotForPublicConsumption
class PAViewColors(
    private val context: Context,
    private val prefs: EncryptedStorage.Prefs
) {

    //////////////////////////////////
    // AuthenticationManager Colors //
    //////////////////////////////////
    private val currentColors: PAColors = getPinAuthenticationDefaultColors()
    private val applicationColors: PAColors = getPinAuthenticationDefaultColors()

    /**
     * Retrieves the hex string value of a color resource.
     * @param [colorRes] Int
     *
     * @return String
     * */
    private fun colorResToHexString(@ColorRes colorRes: Int): String =
        "#${Integer.toHexString(ContextCompat.getColor(context, colorRes))}"

    /**
     * Generates an [PAColors] object set to the default colors.
     *
     * @return [PAColors]
     * */
    private fun getPinAuthenticationDefaultColors(): PAColors =
        PAColors(
            colorResToHexString(R.color.pa_white),
            colorResToHexString(R.color.pa_super_green),
            colorResToHexString(R.color.pa_white),
            colorResToHexString(R.color.pa_sea_blue),
            colorResToHexString(R.color.pa_white),
            colorResToHexString(R.color.pa_sea_blue),
            colorResToHexString(R.color.pa_white),
            colorResToHexString(R.color.pa_deep_sea_blue),
            colorResToHexString(R.color.pa_white)
        )

    fun getBackspaceButtonImageColor(): String =
        currentColors.backspaceButtonImage

    fun getConfirmButtonBackgroundColor(): String =
        currentColors.confirmButtonBackground

    fun getConfirmButtonImageColor(): String =
        currentColors.confirmButtonImage

    fun getPinHintContainerColor(): String =
        currentColors.pinHintContainer

    fun getPinHintImageColor(): String =
        currentColors.pinHintImage

    fun getPinPadButtonBackgroundColor(): String =
        currentColors.pinPadButtonBackground

    fun getPinResetInfoImageColor(): String =
        currentColors.pinResetInfoImage

    fun getScreenBackgroundColor(): String =
        currentColors.screenBackground

    fun getTextColor(): String =
        currentColors.text

    /**
     * Sets the current colors to the Application specified colors.
     * */
    fun resetColorsToApplicationDefaults() {
        currentColors.backspaceButtonImage = applicationColors.backspaceButtonImage
        currentColors.confirmButtonBackground = applicationColors.confirmButtonBackground
        currentColors.confirmButtonImage = applicationColors.confirmButtonImage
        currentColors.pinHintContainer = applicationColors.pinHintContainer
        currentColors.pinHintImage = applicationColors.pinHintImage
        currentColors.pinPadButtonBackground = applicationColors.pinPadButtonBackground
        currentColors.pinResetInfoImage = applicationColors.pinResetInfoImage
        currentColors.screenBackground = applicationColors.screenBackground
        currentColors.text = applicationColors.text

        prefs.remove(PAPrefsKeys.CUSTOM_COLORS_ARE_SET)

        prefs.remove(PAPrefsKeys.BACKSPACE_BUTTON_IMAGE_COLOR)
        prefs.remove(PAPrefsKeys.CONFIRM_BUTTON_BACKGROUND_COLOR)
        prefs.remove(PAPrefsKeys.CONFIRM_BUTTON_IMAGE_COLOR)
        prefs.remove(PAPrefsKeys.PIN_HINT_CONTAINER_COLOR)
        prefs.remove(PAPrefsKeys.PIN_HINT_IMAGE_COLOR)
        prefs.remove(PAPrefsKeys.PIN_PAD_BUTTON_BACKGROUND_COLOR)
        prefs.remove(PAPrefsKeys.PIN_RESET_INFO_IMAGE_COLOR)
        prefs.remove(PAPrefsKeys.SCREEN_BACKGROUND_COLOR)
        prefs.remove(PAPrefsKeys.TEXT_COLOR)
    }

    private inner class PAColors(
        var backspaceButtonImage: String,
        var confirmButtonBackground: String,
        var confirmButtonImage: String,
        var pinHintContainer: String,
        var pinHintImage: String,
        var pinPadButtonBackground: String,
        var pinResetInfoImage: String,
        var screenBackground: String,
        var text: String
    )


    ///////////////////////////////////////////////////////
    // PinAuthentication.Builder.PAColorsBuilder methods //
    ///////////////////////////////////////////////////////
    fun setBackspaceButtonImageColor(@ColorRes colorRes: Int, appColorsBeingSet: Boolean) {
        val colorHex = colorResToHexString(colorRes)

        if (appColorsBeingSet) {
            applicationColors.backspaceButtonImage = colorHex
        } else {
            prefs.write(PAPrefsKeys.BACKSPACE_BUTTON_IMAGE_COLOR, colorHex)
        }

        currentColors.backspaceButtonImage = colorHex
    }

    fun setConfirmButtonBackgroundColor(@ColorRes colorRes: Int, appColorsBeingSet: Boolean) {
        val colorHex = colorResToHexString(colorRes)

        if (appColorsBeingSet) {
            applicationColors.confirmButtonBackground = colorHex
        } else {
            prefs.write(PAPrefsKeys.CONFIRM_BUTTON_BACKGROUND_COLOR, colorHex)
        }

        currentColors.confirmButtonBackground = colorHex
    }

    fun setConfirmButtonImageColor(@ColorRes colorRes: Int, appColorsBeingSet: Boolean) {
        val colorHex = colorResToHexString(colorRes)

        if (appColorsBeingSet) {
            applicationColors.confirmButtonImage = colorHex
        } else {
            prefs.write(PAPrefsKeys.CONFIRM_BUTTON_IMAGE_COLOR, colorHex)
        }

        currentColors.confirmButtonImage = colorHex
    }

    fun setPinHintContainerColor(@ColorRes colorRes: Int, appColorsBeingSet: Boolean) {
        val colorHex = colorResToHexString(colorRes)

        if (appColorsBeingSet) {
            applicationColors.pinHintContainer = colorHex
        } else {
            prefs.write(PAPrefsKeys.PIN_HINT_CONTAINER_COLOR, colorHex)
        }

        currentColors.pinHintContainer = colorHex
    }

    fun setPinHintImageColor(@ColorRes colorRes: Int, appColorsBeingSet: Boolean) {
        val colorHex = colorResToHexString(colorRes)

        if (appColorsBeingSet) {
            applicationColors.pinHintImage = colorHex
        } else {
            prefs.write(PAPrefsKeys.PIN_HINT_IMAGE_COLOR, colorHex)
        }

        currentColors.pinHintImage = colorHex
    }

    fun setPinPadButtonBackgroundColor(@ColorRes colorRes: Int, appColorsBeingSet: Boolean) {
        val colorHex = colorResToHexString(colorRes)

        if (appColorsBeingSet) {
            applicationColors.pinPadButtonBackground = colorHex
        } else {
            prefs.write(PAPrefsKeys.PIN_PAD_BUTTON_BACKGROUND_COLOR, colorHex)
        }

        currentColors.pinPadButtonBackground = colorHex
    }

    fun setPinResetInfoImageColor(@ColorRes colorRes: Int, appColorsBeingSet: Boolean) {
        val colorHex = colorResToHexString(colorRes)

        if (appColorsBeingSet) {
            applicationColors.pinResetInfoImage = colorHex
        } else {
            prefs.write(PAPrefsKeys.PIN_RESET_INFO_IMAGE_COLOR, colorHex)
        }

        currentColors.pinResetInfoImage = colorHex
    }

    fun setScreenBackgroundColor(@ColorRes colorRes: Int, appColorsBeingSet: Boolean) {
        val colorHex = colorResToHexString(colorRes)

        if (appColorsBeingSet) {
            applicationColors.screenBackground = colorHex
        } else {
            prefs.write(PAPrefsKeys.SCREEN_BACKGROUND_COLOR, colorHex)
        }

        currentColors.screenBackground = colorHex
    }

    fun setTextColor(@ColorRes colorRes: Int, appColorsBeingSet: Boolean) {
        val colorHex = colorResToHexString(colorRes)

        if (appColorsBeingSet) {
            applicationColors.text = colorHex
        } else {
            prefs.write(PAPrefsKeys.TEXT_COLOR, colorHex)
        }

        currentColors.text = colorHex
    }

    fun applyColors(appColorsBeingSet: Boolean) {
        if (!appColorsBeingSet) {
            prefs.write(PAPrefsKeys.CUSTOM_COLORS_ARE_SET, true)
        }
    }


    ///////////////////////////////////////////////////
    // PinAuthentication build method initialization //
    ///////////////////////////////////////////////////
    private var initOnceCounter = 0

    fun initializeViewColors() {
        if (initOnceCounter < 1) {
            loadColorsFromSharedPreferences()
            initOnceCounter++
        }
    }

    private fun loadColorsFromSharedPreferences() {
        if (prefs.contains(PAPrefsKeys.CUSTOM_COLORS_ARE_SET)) {

            prefs.read(
                PAPrefsKeys.BACKSPACE_BUTTON_IMAGE_COLOR,
                EncryptedStorage.Prefs.INVALID_STRING
            )?.let {
                if (it != EncryptedStorage.Prefs.INVALID_STRING) {
                    currentColors.backspaceButtonImage = it
                }
            }
            prefs.read(
                PAPrefsKeys.CONFIRM_BUTTON_BACKGROUND_COLOR,
                EncryptedStorage.Prefs.INVALID_STRING
            )?.let {
                if (it != EncryptedStorage.Prefs.INVALID_STRING) {
                    currentColors.confirmButtonBackground = it
                }
            }
            prefs.read(
                PAPrefsKeys.CONFIRM_BUTTON_IMAGE_COLOR,
                EncryptedStorage.Prefs.INVALID_STRING
            )?.let {
                if (it != EncryptedStorage.Prefs.INVALID_STRING) {
                    currentColors.confirmButtonImage = it
                }
            }
            prefs.read(
                PAPrefsKeys.PIN_HINT_CONTAINER_COLOR,
                EncryptedStorage.Prefs.INVALID_STRING
            )?.let {
                if (it != EncryptedStorage.Prefs.INVALID_STRING) {
                    currentColors.pinHintContainer = it
                }
            }
            prefs.read(
                PAPrefsKeys.PIN_HINT_IMAGE_COLOR,
                EncryptedStorage.Prefs.INVALID_STRING
            )?.let {
                if (it != EncryptedStorage.Prefs.INVALID_STRING) {
                    currentColors.pinHintImage = it
                }
            }
            prefs.read(
                PAPrefsKeys.PIN_PAD_BUTTON_BACKGROUND_COLOR,
                EncryptedStorage.Prefs.INVALID_STRING
            )?.let {
                if (it != EncryptedStorage.Prefs.INVALID_STRING) {
                    currentColors.pinPadButtonBackground = it
                }
            }
            prefs.read(
                PAPrefsKeys.PIN_RESET_INFO_IMAGE_COLOR,
                EncryptedStorage.Prefs.INVALID_STRING
            )?.let {
                if (it != EncryptedStorage.Prefs.INVALID_STRING) {
                    currentColors.pinResetInfoImage = it
                }
            }
            prefs.read(
                PAPrefsKeys.SCREEN_BACKGROUND_COLOR,
                EncryptedStorage.Prefs.INVALID_STRING
            )?.let {
                if (it != EncryptedStorage.Prefs.INVALID_STRING) {
                    currentColors.screenBackground = it
                }
            }
            prefs.read(
                PAPrefsKeys.TEXT_COLOR,
                EncryptedStorage.Prefs.INVALID_STRING
            )?.let {
                if (it != EncryptedStorage.Prefs.INVALID_STRING) {
                    currentColors.text = it
                }
            }
        }
    }

}