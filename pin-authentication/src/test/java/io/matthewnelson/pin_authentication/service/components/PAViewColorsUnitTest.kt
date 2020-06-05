package io.matthewnelson.pin_authentication.service.components

import android.app.Application
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.test.core.app.ApplicationProvider
import io.matthewnelson.encrypted_storage.EncryptedStorage
import io.matthewnelson.pin_authentication.R
import io.matthewnelson.pin_authentication.util.PAPrefsKeys
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

@Config(minSdk = 23, maxSdk = 28)
@RunWith(RobolectricTestRunner::class)
class PAViewColorsUnitTest {

    private val app: Application by lazy {
        ApplicationProvider.getApplicationContext() as Application
    }
    private lateinit var prefs: EncryptedStorage.Prefs
    private lateinit var paViewColors: PAViewColors

    @ColorRes private val defaultBackspaceButtonImageColorRes: Int = R.color.pa_white
    @ColorRes private val defaultConfirmButtonBackgroundColorRes: Int = R.color.pa_super_green
    @ColorRes private val defaultConfirmButtonImageColorRes: Int = R.color.pa_white
    @ColorRes private val defaultPinHintContainerColorRes: Int = R.color.pa_sea_blue
    @ColorRes private val defaultPinHintImageColorRes: Int = R.color.pa_white
    @ColorRes private val defaultPinPadButtonBackgroundColorRes: Int = R.color.pa_sea_blue
    @ColorRes private val defaultPinResetInfoImageColorRes: Int = R.color.pa_white
    @ColorRes private val defaultScreenBackgroundColorRes: Int = R.color.pa_deep_sea_blue
    @ColorRes private val defaultTextColorRes: Int = R.color.pa_white

    private lateinit var backspaceButtonImageHexString: String
    private lateinit var confirmButtonBackgroundHexString: String
    private lateinit var confirmButtonImageHexString: String
    private lateinit var pinHintContainerHexString: String
    private lateinit var pinHintImageHexString: String
    private lateinit var pinPadButtonBackgroundHexString: String
    private lateinit var pinResetInfoImageHexString: String
    private lateinit var screenBackgroundHexString: String
    private lateinit var textHexString: String

    private val testColor1HexString = colorResToHexString(R.color.pa_transparent)
    private val testColor2HexString = colorResToHexString(R.color.pa_transparent)

    @Before
    fun setup() {
        prefs = EncryptedStorage.Prefs.createUnencrypted("AMViewColors Test", app)
        paViewColors = PAViewColors(app, prefs)

        backspaceButtonImageHexString = colorResToHexString(defaultBackspaceButtonImageColorRes)
        confirmButtonBackgroundHexString = colorResToHexString(defaultConfirmButtonBackgroundColorRes)
        confirmButtonImageHexString = colorResToHexString(defaultConfirmButtonImageColorRes)
        pinHintContainerHexString = colorResToHexString(defaultPinHintContainerColorRes)
        pinHintImageHexString = colorResToHexString(defaultPinHintImageColorRes)
        pinPadButtonBackgroundHexString = colorResToHexString(defaultPinPadButtonBackgroundColorRes)
        pinResetInfoImageHexString = colorResToHexString(defaultPinResetInfoImageColorRes)
        screenBackgroundHexString = colorResToHexString(defaultScreenBackgroundColorRes)
        textHexString = colorResToHexString(defaultTextColorRes)
    }

    @Test
    fun testClassColorInitialization() {
        assertEquals(backspaceButtonImageHexString, paViewColors.getBackspaceButtonImageColor())
        assertEquals(confirmButtonBackgroundHexString, paViewColors.getConfirmButtonBackgroundColor())
        assertEquals(confirmButtonImageHexString, paViewColors.getConfirmButtonImageColor())
        assertEquals(pinHintContainerHexString, paViewColors.getPinHintContainerColor())
        assertEquals(pinHintImageHexString, paViewColors.getPinHintImageColor())
        assertEquals(pinPadButtonBackgroundHexString, paViewColors.getPinPadButtonBackgroundColor())
        assertEquals(pinResetInfoImageHexString, paViewColors.getPinResetInfoImageColor())
        assertEquals(screenBackgroundHexString, paViewColors.getScreenBackgroundColor())
        assertEquals(textHexString, paViewColors.getTextColor())
    }

    @Test
    fun testSettingColors() {
        // Setting colors for the application
        setColors(R.color.pa_transparent, true)

        // Default color values are overwritten
        assertNotEquals(backspaceButtonImageHexString, paViewColors.getBackspaceButtonImageColor())
        assertNotEquals(confirmButtonBackgroundHexString, paViewColors.getConfirmButtonBackgroundColor())
        assertNotEquals(confirmButtonImageHexString, paViewColors.getConfirmButtonImageColor())
        assertNotEquals(pinHintContainerHexString, paViewColors.getPinHintContainerColor())
        assertNotEquals(pinHintImageHexString, paViewColors.getPinHintImageColor())
        assertNotEquals(pinPadButtonBackgroundHexString, paViewColors.getPinPadButtonBackgroundColor())
        assertNotEquals(pinResetInfoImageHexString, paViewColors.getPinResetInfoImageColor())
        assertNotEquals(screenBackgroundHexString, paViewColors.getScreenBackgroundColor())
        assertNotEquals(textHexString, paViewColors.getTextColor())

        assertEquals(testColor1HexString, paViewColors.getBackspaceButtonImageColor())
        assertEquals(testColor1HexString, paViewColors.getConfirmButtonBackgroundColor())
        assertEquals(testColor1HexString, paViewColors.getConfirmButtonImageColor())
        assertEquals(testColor1HexString, paViewColors.getPinHintContainerColor())
        assertEquals(testColor1HexString, paViewColors.getPinHintImageColor())
        assertEquals(testColor1HexString, paViewColors.getPinPadButtonBackgroundColor())
        assertEquals(testColor1HexString, paViewColors.getPinResetInfoImageColor())
        assertEquals(testColor1HexString, paViewColors.getScreenBackgroundColor())
        assertEquals(testColor1HexString, paViewColors.getTextColor())

        // Nothing was written to Prefs
        assertEquals(false, prefs.contains(PAPrefsKeys.CUSTOM_COLORS_ARE_SET))
        assertEquals(
            EncryptedStorage.Prefs.INVALID_STRING,
            prefs.read(PAPrefsKeys.BACKSPACE_BUTTON_IMAGE_COLOR, EncryptedStorage.Prefs.INVALID_STRING)
        )
        assertEquals(
            EncryptedStorage.Prefs.INVALID_STRING,
            prefs.read(PAPrefsKeys.CONFIRM_BUTTON_BACKGROUND_COLOR, EncryptedStorage.Prefs.INVALID_STRING)
        )
        assertEquals(
            EncryptedStorage.Prefs.INVALID_STRING,
            prefs.read(PAPrefsKeys.CONFIRM_BUTTON_IMAGE_COLOR, EncryptedStorage.Prefs.INVALID_STRING)
        )
        assertEquals(
            EncryptedStorage.Prefs.INVALID_STRING,
            prefs.read(PAPrefsKeys.PIN_HINT_CONTAINER_COLOR, EncryptedStorage.Prefs.INVALID_STRING)
        )
        assertEquals(
            EncryptedStorage.Prefs.INVALID_STRING,
            prefs.read(PAPrefsKeys.PIN_HINT_IMAGE_COLOR, EncryptedStorage.Prefs.INVALID_STRING)
        )
        assertEquals(
            EncryptedStorage.Prefs.INVALID_STRING,
            prefs.read(PAPrefsKeys.PIN_PAD_BUTTON_BACKGROUND_COLOR, EncryptedStorage.Prefs.INVALID_STRING)
        )
        assertEquals(
            EncryptedStorage.Prefs.INVALID_STRING,
            prefs.read(PAPrefsKeys.PIN_RESET_INFO_IMAGE_COLOR, EncryptedStorage.Prefs.INVALID_STRING)
        )
        assertEquals(
            EncryptedStorage.Prefs.INVALID_STRING,
            prefs.read(PAPrefsKeys.SCREEN_BACKGROUND_COLOR, EncryptedStorage.Prefs.INVALID_STRING)
        )
        assertEquals(
            EncryptedStorage.Prefs.INVALID_STRING,
            prefs.read(PAPrefsKeys.TEXT_COLOR, EncryptedStorage.Prefs.INVALID_STRING)
        )

        // Setting custom colors (after AuthenticationManager initialized Application Colors)
        setColors(R.color.pa_transparent, false)

        assertEquals(testColor2HexString, paViewColors.getBackspaceButtonImageColor())
        assertEquals(testColor2HexString, paViewColors.getConfirmButtonBackgroundColor())
        assertEquals(testColor2HexString, paViewColors.getConfirmButtonImageColor())
        assertEquals(testColor2HexString, paViewColors.getPinHintContainerColor())
        assertEquals(testColor2HexString, paViewColors.getPinHintImageColor())
        assertEquals(testColor2HexString, paViewColors.getPinPadButtonBackgroundColor())
        assertEquals(testColor2HexString, paViewColors.getPinResetInfoImageColor())
        assertEquals(testColor2HexString, paViewColors.getScreenBackgroundColor())
        assertEquals(testColor2HexString, paViewColors.getTextColor())

        // Custom colors should be saved to shared preferences
        assertEquals(true, prefs.contains(PAPrefsKeys.CUSTOM_COLORS_ARE_SET))
        assertEquals(
            testColor2HexString,
            prefs.read(PAPrefsKeys.BACKSPACE_BUTTON_IMAGE_COLOR, EncryptedStorage.Prefs.INVALID_STRING)
        )
        assertEquals(
            testColor2HexString,
            prefs.read(PAPrefsKeys.CONFIRM_BUTTON_BACKGROUND_COLOR, EncryptedStorage.Prefs.INVALID_STRING)
        )
        assertEquals(
            testColor2HexString,
            prefs.read(PAPrefsKeys.CONFIRM_BUTTON_IMAGE_COLOR, EncryptedStorage.Prefs.INVALID_STRING)
        )
        assertEquals(
            testColor2HexString,
            prefs.read(PAPrefsKeys.PIN_HINT_CONTAINER_COLOR, EncryptedStorage.Prefs.INVALID_STRING)
        )
        assertEquals(
            testColor2HexString,
            prefs.read(PAPrefsKeys.PIN_HINT_IMAGE_COLOR, EncryptedStorage.Prefs.INVALID_STRING)
        )
        assertEquals(
            testColor2HexString,
            prefs.read(PAPrefsKeys.PIN_PAD_BUTTON_BACKGROUND_COLOR, EncryptedStorage.Prefs.INVALID_STRING)
        )
        assertEquals(
            testColor2HexString,
            prefs.read(PAPrefsKeys.PIN_RESET_INFO_IMAGE_COLOR, EncryptedStorage.Prefs.INVALID_STRING)
        )
        assertEquals(
            testColor2HexString,
            prefs.read(PAPrefsKeys.SCREEN_BACKGROUND_COLOR, EncryptedStorage.Prefs.INVALID_STRING)
        )
        assertEquals(
            testColor2HexString,
            prefs.read(PAPrefsKeys.TEXT_COLOR, EncryptedStorage.Prefs.INVALID_STRING)
        )

        // Resetting colors back to application specified colors
        paViewColors.resetColorsToApplicationDefaults()

        // Colors correctly reset to those specified for the application
        assertEquals(testColor1HexString, paViewColors.getBackspaceButtonImageColor())
        assertEquals(testColor1HexString, paViewColors.getConfirmButtonBackgroundColor())
        assertEquals(testColor1HexString, paViewColors.getConfirmButtonImageColor())
        assertEquals(testColor1HexString, paViewColors.getPinHintContainerColor())
        assertEquals(testColor1HexString, paViewColors.getPinHintImageColor())
        assertEquals(testColor1HexString, paViewColors.getPinPadButtonBackgroundColor())
        assertEquals(testColor1HexString, paViewColors.getPinResetInfoImageColor())
        assertEquals(testColor1HexString, paViewColors.getScreenBackgroundColor())
        assertEquals(testColor1HexString, paViewColors.getTextColor())

        // Custom Colors were removed from Prefs
        assertEquals(false, prefs.contains(PAPrefsKeys.CUSTOM_COLORS_ARE_SET))
        assertEquals(
            EncryptedStorage.Prefs.INVALID_STRING,
            prefs.read(PAPrefsKeys.BACKSPACE_BUTTON_IMAGE_COLOR, EncryptedStorage.Prefs.INVALID_STRING)
        )
        assertEquals(
            EncryptedStorage.Prefs.INVALID_STRING,
            prefs.read(PAPrefsKeys.CONFIRM_BUTTON_BACKGROUND_COLOR, EncryptedStorage.Prefs.INVALID_STRING)
        )
        assertEquals(
            EncryptedStorage.Prefs.INVALID_STRING,
            prefs.read(PAPrefsKeys.CONFIRM_BUTTON_IMAGE_COLOR, EncryptedStorage.Prefs.INVALID_STRING)
        )
        assertEquals(
            EncryptedStorage.Prefs.INVALID_STRING,
            prefs.read(PAPrefsKeys.PIN_HINT_CONTAINER_COLOR, EncryptedStorage.Prefs.INVALID_STRING)
        )
        assertEquals(
            EncryptedStorage.Prefs.INVALID_STRING,
            prefs.read(PAPrefsKeys.PIN_HINT_IMAGE_COLOR, EncryptedStorage.Prefs.INVALID_STRING)
        )
        assertEquals(
            EncryptedStorage.Prefs.INVALID_STRING,
            prefs.read(PAPrefsKeys.PIN_PAD_BUTTON_BACKGROUND_COLOR, EncryptedStorage.Prefs.INVALID_STRING)
        )
        assertEquals(
            EncryptedStorage.Prefs.INVALID_STRING,
            prefs.read(PAPrefsKeys.PIN_RESET_INFO_IMAGE_COLOR, EncryptedStorage.Prefs.INVALID_STRING)
        )
        assertEquals(
            EncryptedStorage.Prefs.INVALID_STRING,
            prefs.read(PAPrefsKeys.SCREEN_BACKGROUND_COLOR, EncryptedStorage.Prefs.INVALID_STRING)
        )
        assertEquals(
            EncryptedStorage.Prefs.INVALID_STRING,
            prefs.read(PAPrefsKeys.TEXT_COLOR, EncryptedStorage.Prefs.INVALID_STRING)
        )

    }

    @Test
    fun testInitializeViewColors() {
        loadSharedPrefsWithCustomColors(R.color.pa_transparent)

        // Set application colors
        setColors(R.color.pa_transparent, true)

        assertEquals(testColor2HexString, paViewColors.getBackspaceButtonImageColor())
        assertEquals(testColor2HexString, paViewColors.getConfirmButtonBackgroundColor())
        assertEquals(testColor2HexString, paViewColors.getConfirmButtonImageColor())
        assertEquals(testColor2HexString, paViewColors.getPinHintContainerColor())
        assertEquals(testColor2HexString, paViewColors.getPinHintImageColor())
        assertEquals(testColor2HexString, paViewColors.getPinPadButtonBackgroundColor())
        assertEquals(testColor2HexString, paViewColors.getPinResetInfoImageColor())
        assertEquals(testColor2HexString, paViewColors.getScreenBackgroundColor())
        assertEquals(testColor2HexString, paViewColors.getTextColor())

        // Colors from Prefs should be loaded to current colors
        paViewColors.initializeViewColors()

        assertEquals(testColor1HexString, paViewColors.getBackspaceButtonImageColor())
        assertEquals(testColor1HexString, paViewColors.getConfirmButtonBackgroundColor())
        assertEquals(testColor1HexString, paViewColors.getConfirmButtonImageColor())
        assertEquals(testColor1HexString, paViewColors.getPinHintContainerColor())
        assertEquals(testColor1HexString, paViewColors.getPinHintImageColor())
        assertEquals(testColor1HexString, paViewColors.getPinPadButtonBackgroundColor())
        assertEquals(testColor1HexString, paViewColors.getPinResetInfoImageColor())
        assertEquals(testColor1HexString, paViewColors.getScreenBackgroundColor())
        assertEquals(testColor1HexString, paViewColors.getTextColor())

        // Overwrite colors saved to shared prefs
        loadSharedPrefsWithCustomColors(R.color.pa_transparent)

        assertEquals(true, prefs.contains(PAPrefsKeys.CUSTOM_COLORS_ARE_SET))
        assertEquals(
            testColor2HexString,
            prefs.read(PAPrefsKeys.BACKSPACE_BUTTON_IMAGE_COLOR, EncryptedStorage.Prefs.INVALID_STRING)
        )
        assertEquals(
            testColor2HexString,
            prefs.read(PAPrefsKeys.CONFIRM_BUTTON_BACKGROUND_COLOR, EncryptedStorage.Prefs.INVALID_STRING)
        )
        assertEquals(
            testColor2HexString,
            prefs.read(PAPrefsKeys.CONFIRM_BUTTON_IMAGE_COLOR, EncryptedStorage.Prefs.INVALID_STRING)
        )
        assertEquals(
            testColor2HexString,
            prefs.read(PAPrefsKeys.PIN_HINT_CONTAINER_COLOR, EncryptedStorage.Prefs.INVALID_STRING)
        )
        assertEquals(
            testColor2HexString,
            prefs.read(PAPrefsKeys.PIN_HINT_IMAGE_COLOR, EncryptedStorage.Prefs.INVALID_STRING)
        )
        assertEquals(
            testColor2HexString,
            prefs.read(PAPrefsKeys.PIN_PAD_BUTTON_BACKGROUND_COLOR, EncryptedStorage.Prefs.INVALID_STRING)
        )
        assertEquals(
            testColor2HexString,
            prefs.read(PAPrefsKeys.PIN_RESET_INFO_IMAGE_COLOR, EncryptedStorage.Prefs.INVALID_STRING)
        )
        assertEquals(
            testColor2HexString,
            prefs.read(PAPrefsKeys.SCREEN_BACKGROUND_COLOR, EncryptedStorage.Prefs.INVALID_STRING)
        )
        assertEquals(
            testColor2HexString,
            prefs.read(PAPrefsKeys.TEXT_COLOR, EncryptedStorage.Prefs.INVALID_STRING)
        )

        // Colors from Prefs should NOT be loaded
        paViewColors.initializeViewColors()

        assertEquals(testColor1HexString, paViewColors.getBackspaceButtonImageColor())
        assertEquals(testColor1HexString, paViewColors.getConfirmButtonBackgroundColor())
        assertEquals(testColor1HexString, paViewColors.getConfirmButtonImageColor())
        assertEquals(testColor1HexString, paViewColors.getPinHintContainerColor())
        assertEquals(testColor1HexString, paViewColors.getPinHintImageColor())
        assertEquals(testColor1HexString, paViewColors.getPinPadButtonBackgroundColor())
        assertEquals(testColor1HexString, paViewColors.getPinResetInfoImageColor())
        assertEquals(testColor1HexString, paViewColors.getScreenBackgroundColor())
        assertEquals(testColor1HexString, paViewColors.getTextColor())
    }

    private fun colorResToHexString(@ColorRes colorRes: Int): String =
        "#${Integer.toHexString(ContextCompat.getColor(app, colorRes))}"

    private fun loadSharedPrefsWithCustomColors(@ColorRes colorRes: Int) {
        val hexString = colorResToHexString(colorRes)
        prefs.write(PAPrefsKeys.CUSTOM_COLORS_ARE_SET, true)
        prefs.write(PAPrefsKeys.BACKSPACE_BUTTON_IMAGE_COLOR, hexString)
        prefs.write(PAPrefsKeys.CONFIRM_BUTTON_BACKGROUND_COLOR, hexString)
        prefs.write(PAPrefsKeys.CONFIRM_BUTTON_IMAGE_COLOR, hexString)
        prefs.write(PAPrefsKeys.PIN_HINT_CONTAINER_COLOR, hexString)
        prefs.write(PAPrefsKeys.PIN_HINT_IMAGE_COLOR, hexString)
        prefs.write(PAPrefsKeys.PIN_PAD_BUTTON_BACKGROUND_COLOR, hexString)
        prefs.write(PAPrefsKeys.PIN_RESET_INFO_IMAGE_COLOR, hexString)
        prefs.write(PAPrefsKeys.SCREEN_BACKGROUND_COLOR, hexString)
        prefs.write(PAPrefsKeys.TEXT_COLOR, hexString)
    }

    private fun setColors(@ColorRes colorRes: Int, setApplicationColors: Boolean) {
        paViewColors.setBackspaceButtonImageColor(colorRes, setApplicationColors)
        paViewColors.setConfirmButtonBackgroundColor(colorRes, setApplicationColors)
        paViewColors.setConfirmButtonImageColor(colorRes, setApplicationColors)
        paViewColors.setPinHintContainerColor(colorRes, setApplicationColors)
        paViewColors.setPinHintImageColor(colorRes, setApplicationColors)
        paViewColors.setPinPadButtonBackgroundColor(colorRes, setApplicationColors)
        paViewColors.setPinResetInfoImageColor(colorRes, setApplicationColors)
        paViewColors.setScreenBackgroundColor(colorRes, setApplicationColors)
        paViewColors.setTextColor(colorRes, setApplicationColors)
        paViewColors.applyColors(setApplicationColors)

    }

}