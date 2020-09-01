package io.matthewnelson.pin_authentication.service.components

import android.app.Application
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.test.core.app.ApplicationProvider
import io.matthewnelson.encrypted_storage.Prefs
import io.matthewnelson.pin_authentication.R
import io.matthewnelson.pin_authentication.util.PrefsKeys
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

@Config(minSdk = 23, maxSdk = 28)
@RunWith(RobolectricTestRunner::class)
class ViewColorsUnitTest {

    private val app: Application by lazy {
        ApplicationProvider.getApplicationContext() as Application
    }
    private lateinit var prefs: Prefs
    private lateinit var viewColors: ViewColors

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
        prefs = Prefs.createUnencrypted("AMViewColors Test", app)
        viewColors = ViewColors(app, prefs)

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
        assertEquals(backspaceButtonImageHexString, viewColors.getBackspaceButtonImageColor())
        assertEquals(confirmButtonBackgroundHexString, viewColors.getConfirmButtonBackgroundColor())
        assertEquals(confirmButtonImageHexString, viewColors.getConfirmButtonImageColor())
        assertEquals(pinHintContainerHexString, viewColors.getPinHintContainerColor())
        assertEquals(pinHintImageHexString, viewColors.getPinHintImageColor())
        assertEquals(pinPadButtonBackgroundHexString, viewColors.getPinPadButtonBackgroundColor())
        assertEquals(pinResetInfoImageHexString, viewColors.getPinResetInfoImageColor())
        assertEquals(screenBackgroundHexString, viewColors.getScreenBackgroundColor())
        assertEquals(textHexString, viewColors.getTextColor())
    }

    @Test
    fun testSettingColors() {
        // Setting colors for the application
        setColors(R.color.pa_transparent, true)

        // Default color values are overwritten
        assertNotEquals(backspaceButtonImageHexString, viewColors.getBackspaceButtonImageColor())
        assertNotEquals(confirmButtonBackgroundHexString, viewColors.getConfirmButtonBackgroundColor())
        assertNotEquals(confirmButtonImageHexString, viewColors.getConfirmButtonImageColor())
        assertNotEquals(pinHintContainerHexString, viewColors.getPinHintContainerColor())
        assertNotEquals(pinHintImageHexString, viewColors.getPinHintImageColor())
        assertNotEquals(pinPadButtonBackgroundHexString, viewColors.getPinPadButtonBackgroundColor())
        assertNotEquals(pinResetInfoImageHexString, viewColors.getPinResetInfoImageColor())
        assertNotEquals(screenBackgroundHexString, viewColors.getScreenBackgroundColor())
        assertNotEquals(textHexString, viewColors.getTextColor())

        assertEquals(testColor1HexString, viewColors.getBackspaceButtonImageColor())
        assertEquals(testColor1HexString, viewColors.getConfirmButtonBackgroundColor())
        assertEquals(testColor1HexString, viewColors.getConfirmButtonImageColor())
        assertEquals(testColor1HexString, viewColors.getPinHintContainerColor())
        assertEquals(testColor1HexString, viewColors.getPinHintImageColor())
        assertEquals(testColor1HexString, viewColors.getPinPadButtonBackgroundColor())
        assertEquals(testColor1HexString, viewColors.getPinResetInfoImageColor())
        assertEquals(testColor1HexString, viewColors.getScreenBackgroundColor())
        assertEquals(testColor1HexString, viewColors.getTextColor())

        // Nothing was written to Prefs
        assertEquals(false, prefs.contains(PrefsKeys.CUSTOM_COLORS_ARE_SET))
        assertEquals(
            Prefs.INVALID_STRING,
            prefs.read(PrefsKeys.BACKSPACE_BUTTON_IMAGE_COLOR, Prefs.INVALID_STRING)
        )
        assertEquals(
            Prefs.INVALID_STRING,
            prefs.read(PrefsKeys.CONFIRM_BUTTON_BACKGROUND_COLOR, Prefs.INVALID_STRING)
        )
        assertEquals(
            Prefs.INVALID_STRING,
            prefs.read(PrefsKeys.CONFIRM_BUTTON_IMAGE_COLOR, Prefs.INVALID_STRING)
        )
        assertEquals(
            Prefs.INVALID_STRING,
            prefs.read(PrefsKeys.PIN_HINT_CONTAINER_COLOR, Prefs.INVALID_STRING)
        )
        assertEquals(
            Prefs.INVALID_STRING,
            prefs.read(PrefsKeys.PIN_HINT_IMAGE_COLOR, Prefs.INVALID_STRING)
        )
        assertEquals(
            Prefs.INVALID_STRING,
            prefs.read(PrefsKeys.PIN_PAD_BUTTON_BACKGROUND_COLOR, Prefs.INVALID_STRING)
        )
        assertEquals(
            Prefs.INVALID_STRING,
            prefs.read(PrefsKeys.PIN_RESET_INFO_IMAGE_COLOR, Prefs.INVALID_STRING)
        )
        assertEquals(
            Prefs.INVALID_STRING,
            prefs.read(PrefsKeys.SCREEN_BACKGROUND_COLOR, Prefs.INVALID_STRING)
        )
        assertEquals(
            Prefs.INVALID_STRING,
            prefs.read(PrefsKeys.TEXT_COLOR, Prefs.INVALID_STRING)
        )

        // Setting custom colors (after AuthenticationManager initialized Application Colors)
        setColors(R.color.pa_transparent, false)

        assertEquals(testColor2HexString, viewColors.getBackspaceButtonImageColor())
        assertEquals(testColor2HexString, viewColors.getConfirmButtonBackgroundColor())
        assertEquals(testColor2HexString, viewColors.getConfirmButtonImageColor())
        assertEquals(testColor2HexString, viewColors.getPinHintContainerColor())
        assertEquals(testColor2HexString, viewColors.getPinHintImageColor())
        assertEquals(testColor2HexString, viewColors.getPinPadButtonBackgroundColor())
        assertEquals(testColor2HexString, viewColors.getPinResetInfoImageColor())
        assertEquals(testColor2HexString, viewColors.getScreenBackgroundColor())
        assertEquals(testColor2HexString, viewColors.getTextColor())

        // Custom colors should be saved to shared preferences
        assertEquals(true, prefs.contains(PrefsKeys.CUSTOM_COLORS_ARE_SET))
        assertEquals(
            testColor2HexString,
            prefs.read(PrefsKeys.BACKSPACE_BUTTON_IMAGE_COLOR, Prefs.INVALID_STRING)
        )
        assertEquals(
            testColor2HexString,
            prefs.read(PrefsKeys.CONFIRM_BUTTON_BACKGROUND_COLOR, Prefs.INVALID_STRING)
        )
        assertEquals(
            testColor2HexString,
            prefs.read(PrefsKeys.CONFIRM_BUTTON_IMAGE_COLOR, Prefs.INVALID_STRING)
        )
        assertEquals(
            testColor2HexString,
            prefs.read(PrefsKeys.PIN_HINT_CONTAINER_COLOR, Prefs.INVALID_STRING)
        )
        assertEquals(
            testColor2HexString,
            prefs.read(PrefsKeys.PIN_HINT_IMAGE_COLOR, Prefs.INVALID_STRING)
        )
        assertEquals(
            testColor2HexString,
            prefs.read(PrefsKeys.PIN_PAD_BUTTON_BACKGROUND_COLOR, Prefs.INVALID_STRING)
        )
        assertEquals(
            testColor2HexString,
            prefs.read(PrefsKeys.PIN_RESET_INFO_IMAGE_COLOR, Prefs.INVALID_STRING)
        )
        assertEquals(
            testColor2HexString,
            prefs.read(PrefsKeys.SCREEN_BACKGROUND_COLOR, Prefs.INVALID_STRING)
        )
        assertEquals(
            testColor2HexString,
            prefs.read(PrefsKeys.TEXT_COLOR, Prefs.INVALID_STRING)
        )

        // Resetting colors back to application specified colors
        viewColors.resetColorsToApplicationDefaults()

        // Colors correctly reset to those specified for the application
        assertEquals(testColor1HexString, viewColors.getBackspaceButtonImageColor())
        assertEquals(testColor1HexString, viewColors.getConfirmButtonBackgroundColor())
        assertEquals(testColor1HexString, viewColors.getConfirmButtonImageColor())
        assertEquals(testColor1HexString, viewColors.getPinHintContainerColor())
        assertEquals(testColor1HexString, viewColors.getPinHintImageColor())
        assertEquals(testColor1HexString, viewColors.getPinPadButtonBackgroundColor())
        assertEquals(testColor1HexString, viewColors.getPinResetInfoImageColor())
        assertEquals(testColor1HexString, viewColors.getScreenBackgroundColor())
        assertEquals(testColor1HexString, viewColors.getTextColor())

        // Custom Colors were removed from Prefs
        assertEquals(false, prefs.contains(PrefsKeys.CUSTOM_COLORS_ARE_SET))
        assertEquals(
            Prefs.INVALID_STRING,
            prefs.read(PrefsKeys.BACKSPACE_BUTTON_IMAGE_COLOR, Prefs.INVALID_STRING)
        )
        assertEquals(
            Prefs.INVALID_STRING,
            prefs.read(PrefsKeys.CONFIRM_BUTTON_BACKGROUND_COLOR, Prefs.INVALID_STRING)
        )
        assertEquals(
            Prefs.INVALID_STRING,
            prefs.read(PrefsKeys.CONFIRM_BUTTON_IMAGE_COLOR, Prefs.INVALID_STRING)
        )
        assertEquals(
            Prefs.INVALID_STRING,
            prefs.read(PrefsKeys.PIN_HINT_CONTAINER_COLOR, Prefs.INVALID_STRING)
        )
        assertEquals(
            Prefs.INVALID_STRING,
            prefs.read(PrefsKeys.PIN_HINT_IMAGE_COLOR, Prefs.INVALID_STRING)
        )
        assertEquals(
            Prefs.INVALID_STRING,
            prefs.read(PrefsKeys.PIN_PAD_BUTTON_BACKGROUND_COLOR, Prefs.INVALID_STRING)
        )
        assertEquals(
            Prefs.INVALID_STRING,
            prefs.read(PrefsKeys.PIN_RESET_INFO_IMAGE_COLOR, Prefs.INVALID_STRING)
        )
        assertEquals(
            Prefs.INVALID_STRING,
            prefs.read(PrefsKeys.SCREEN_BACKGROUND_COLOR, Prefs.INVALID_STRING)
        )
        assertEquals(
            Prefs.INVALID_STRING,
            prefs.read(PrefsKeys.TEXT_COLOR, Prefs.INVALID_STRING)
        )

    }

    @Test
    fun testInitializeViewColors() {
        loadSharedPrefsWithCustomColors(R.color.pa_transparent)

        // Set application colors
        setColors(R.color.pa_transparent, true)

        assertEquals(testColor2HexString, viewColors.getBackspaceButtonImageColor())
        assertEquals(testColor2HexString, viewColors.getConfirmButtonBackgroundColor())
        assertEquals(testColor2HexString, viewColors.getConfirmButtonImageColor())
        assertEquals(testColor2HexString, viewColors.getPinHintContainerColor())
        assertEquals(testColor2HexString, viewColors.getPinHintImageColor())
        assertEquals(testColor2HexString, viewColors.getPinPadButtonBackgroundColor())
        assertEquals(testColor2HexString, viewColors.getPinResetInfoImageColor())
        assertEquals(testColor2HexString, viewColors.getScreenBackgroundColor())
        assertEquals(testColor2HexString, viewColors.getTextColor())

        // Colors from Prefs should be loaded to current colors
        viewColors.initializeViewColors()

        assertEquals(testColor1HexString, viewColors.getBackspaceButtonImageColor())
        assertEquals(testColor1HexString, viewColors.getConfirmButtonBackgroundColor())
        assertEquals(testColor1HexString, viewColors.getConfirmButtonImageColor())
        assertEquals(testColor1HexString, viewColors.getPinHintContainerColor())
        assertEquals(testColor1HexString, viewColors.getPinHintImageColor())
        assertEquals(testColor1HexString, viewColors.getPinPadButtonBackgroundColor())
        assertEquals(testColor1HexString, viewColors.getPinResetInfoImageColor())
        assertEquals(testColor1HexString, viewColors.getScreenBackgroundColor())
        assertEquals(testColor1HexString, viewColors.getTextColor())

        // Overwrite colors saved to shared prefs
        loadSharedPrefsWithCustomColors(R.color.pa_transparent)

        assertEquals(true, prefs.contains(PrefsKeys.CUSTOM_COLORS_ARE_SET))
        assertEquals(
            testColor2HexString,
            prefs.read(PrefsKeys.BACKSPACE_BUTTON_IMAGE_COLOR, Prefs.INVALID_STRING)
        )
        assertEquals(
            testColor2HexString,
            prefs.read(PrefsKeys.CONFIRM_BUTTON_BACKGROUND_COLOR, Prefs.INVALID_STRING)
        )
        assertEquals(
            testColor2HexString,
            prefs.read(PrefsKeys.CONFIRM_BUTTON_IMAGE_COLOR, Prefs.INVALID_STRING)
        )
        assertEquals(
            testColor2HexString,
            prefs.read(PrefsKeys.PIN_HINT_CONTAINER_COLOR, Prefs.INVALID_STRING)
        )
        assertEquals(
            testColor2HexString,
            prefs.read(PrefsKeys.PIN_HINT_IMAGE_COLOR, Prefs.INVALID_STRING)
        )
        assertEquals(
            testColor2HexString,
            prefs.read(PrefsKeys.PIN_PAD_BUTTON_BACKGROUND_COLOR, Prefs.INVALID_STRING)
        )
        assertEquals(
            testColor2HexString,
            prefs.read(PrefsKeys.PIN_RESET_INFO_IMAGE_COLOR, Prefs.INVALID_STRING)
        )
        assertEquals(
            testColor2HexString,
            prefs.read(PrefsKeys.SCREEN_BACKGROUND_COLOR, Prefs.INVALID_STRING)
        )
        assertEquals(
            testColor2HexString,
            prefs.read(PrefsKeys.TEXT_COLOR, Prefs.INVALID_STRING)
        )

        // Colors from Prefs should NOT be loaded
        viewColors.initializeViewColors()

        assertEquals(testColor1HexString, viewColors.getBackspaceButtonImageColor())
        assertEquals(testColor1HexString, viewColors.getConfirmButtonBackgroundColor())
        assertEquals(testColor1HexString, viewColors.getConfirmButtonImageColor())
        assertEquals(testColor1HexString, viewColors.getPinHintContainerColor())
        assertEquals(testColor1HexString, viewColors.getPinHintImageColor())
        assertEquals(testColor1HexString, viewColors.getPinPadButtonBackgroundColor())
        assertEquals(testColor1HexString, viewColors.getPinResetInfoImageColor())
        assertEquals(testColor1HexString, viewColors.getScreenBackgroundColor())
        assertEquals(testColor1HexString, viewColors.getTextColor())
    }

    private fun colorResToHexString(@ColorRes colorRes: Int): String =
        "#${Integer.toHexString(ContextCompat.getColor(app, colorRes))}"

    private fun loadSharedPrefsWithCustomColors(@ColorRes colorRes: Int) {
        val hexString = colorResToHexString(colorRes)
        prefs.write(PrefsKeys.CUSTOM_COLORS_ARE_SET, true)
        prefs.write(PrefsKeys.BACKSPACE_BUTTON_IMAGE_COLOR, hexString)
        prefs.write(PrefsKeys.CONFIRM_BUTTON_BACKGROUND_COLOR, hexString)
        prefs.write(PrefsKeys.CONFIRM_BUTTON_IMAGE_COLOR, hexString)
        prefs.write(PrefsKeys.PIN_HINT_CONTAINER_COLOR, hexString)
        prefs.write(PrefsKeys.PIN_HINT_IMAGE_COLOR, hexString)
        prefs.write(PrefsKeys.PIN_PAD_BUTTON_BACKGROUND_COLOR, hexString)
        prefs.write(PrefsKeys.PIN_RESET_INFO_IMAGE_COLOR, hexString)
        prefs.write(PrefsKeys.SCREEN_BACKGROUND_COLOR, hexString)
        prefs.write(PrefsKeys.TEXT_COLOR, hexString)
    }

    private fun setColors(@ColorRes colorRes: Int, setApplicationColors: Boolean) {
        viewColors.setBackspaceButtonImageColor(colorRes, setApplicationColors)
        viewColors.setConfirmButtonBackgroundColor(colorRes, setApplicationColors)
        viewColors.setConfirmButtonImageColor(colorRes, setApplicationColors)
        viewColors.setPinHintContainerColor(colorRes, setApplicationColors)
        viewColors.setPinHintImageColor(colorRes, setApplicationColors)
        viewColors.setPinPadButtonBackgroundColor(colorRes, setApplicationColors)
        viewColors.setPinResetInfoImageColor(colorRes, setApplicationColors)
        viewColors.setScreenBackgroundColor(colorRes, setApplicationColors)
        viewColors.setTextColor(colorRes, setApplicationColors)
        viewColors.applyColors(setApplicationColors)

    }

}