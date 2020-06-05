package io.matthewnelson.pin_authentication.service.components

import android.app.Application
import androidx.test.core.app.ApplicationProvider
import io.matthewnelson.encrypted_storage.EncryptedStorage
import io.matthewnelson.pin_authentication.util.PrefsKeys
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import kotlin.test.assertEquals

@Config(minSdk = 23, maxSdk = 28)
@RunWith(RobolectricTestRunner::class)
class SettingsUnitTest {

    private val app: Application by lazy {
        ApplicationProvider.getApplicationContext() as Application
    }
    private lateinit var prefs: EncryptedStorage.Prefs
    private lateinit var settings: Settings

    private val buildConfigDebugDefault = false
    private val appHasOnBoardDefault = false
    private val hapticFeedbackDefault = false
    private val minPinDefault = 4
    private val onBoardCompleteDefault = false
    private val scrambledPinDefault = false
    private val userPinIsSetDefault = false

    @Before
    fun setup() {
        prefs = EncryptedStorage.Prefs.createUnencrypted("TestPrefs", app)
        settings = Settings(prefs)
    }

    @Test
    fun testInitialValues() {
        assertEquals(buildConfigDebugDefault, settings.getBuildConfigDebug())
        assertEquals(appHasOnBoardDefault, settings.getAppHasOnBoardProcess())
        assertEquals(hapticFeedbackDefault, settings.getHapticFeedbackIsEnabled())
        assertEquals(minPinDefault, settings.getMinPinLength())
        assertEquals(onBoardCompleteDefault, settings.getOnBoardProcessIsComplete())
        assertEquals(scrambledPinDefault, settings.getScrambledPinIsEnabled())
        assertEquals(userPinIsSetDefault, settings.getUserPinIsSet())
    }

    @Test
    fun testInitializeSettings() {
        val buildConfigDebugInit = true
        val hasOnBoardInit = true
        val hapticFeedbackInit = true
        val minPinInit = 8
        val scrambledPinInit = true

        settings.initializeSettings(
            buildConfigDebugInit,
            hasOnBoardInit,
            hapticFeedbackInit,
            minPinInit,
            scrambledPinInit
        )

        assertEquals(buildConfigDebugInit, settings.getBuildConfigDebug())
        assertEquals(hasOnBoardInit, settings.getAppHasOnBoardProcess())
        assertEquals(hapticFeedbackInit, settings.getHapticFeedbackIsEnabled())
        assertEquals(minPinInit, settings.getMinPinLength())
        assertEquals(false, settings.getOnBoardProcessIsComplete())
        assertEquals(scrambledPinInit, settings.getScrambledPinIsEnabled())
        assertEquals(false, settings.getUserPinIsSet())

        val buildConfigDebug2 = false
        val hasOnBoardInit2 = false
        val hapticFeedbackInit2 = false
        val minPinInit2 = 5
        val scrambledPinInit2 = false

        settings.initializeSettings(
            buildConfigDebug2,
            hasOnBoardInit2,
            hapticFeedbackInit2,
            minPinInit2,
            scrambledPinInit2
        )

        assertEquals(buildConfigDebugInit, settings.getBuildConfigDebug())
        assertEquals(hasOnBoardInit, settings.getAppHasOnBoardProcess())
        assertEquals(hapticFeedbackInit, settings.getHapticFeedbackIsEnabled())
        assertEquals(minPinInit, settings.getMinPinLength())
        assertEquals(false, settings.getOnBoardProcessIsComplete())
        assertEquals(scrambledPinInit, settings.getScrambledPinIsEnabled())
        assertEquals(false, settings.getUserPinIsSet())
    }

    @Test
    fun testLoadSettingsFromSharedPreferences() {
        val onBoardProcessSetTrue = true
        val hapticPrefs = true
        val onBoardPrefs = true
        val scrambledPrefs = true
        val userPinPrefs = true
        prefs.write(PrefsKeys.HAPTIC_FEEDBACK_IS_ENABLED, hapticPrefs)
        prefs.write(PrefsKeys.ONBOARD_PROCESS_COMPLETE, onBoardPrefs)
        prefs.write(PrefsKeys.SCRAMBLED_PIN_IS_ENABLED, scrambledPrefs)
        prefs.write(PrefsKeys.USER_PIN_IS_SET, userPinPrefs)

        settings.initializeSettings(
            buildConfigDebugDefault,
            onBoardProcessSetTrue, // Must turn to true so on-board complete gets loaded
            hapticFeedbackDefault,
            minPinDefault,
            scrambledPinDefault
        )

        assertEquals(onBoardProcessSetTrue, settings.getAppHasOnBoardProcess())
        assertEquals(hapticPrefs, settings.getHapticFeedbackIsEnabled())
        assertEquals(minPinDefault, settings.getMinPinLength())
        assertEquals(onBoardPrefs, settings.getOnBoardProcessIsComplete())
        assertEquals(scrambledPrefs, settings.getScrambledPinIsEnabled())
        assertEquals(userPinPrefs, settings.getUserPinIsSet())
    }

    @Test
    fun testSetHapticFeedBack() {
        val resultBefore = prefs.read(PrefsKeys.HAPTIC_FEEDBACK_IS_ENABLED)
        assertEquals(null, resultBefore)
        assertEquals(hapticFeedbackDefault, settings.getHapticFeedbackIsEnabled())

        settings.setHapticFeedbackIsEnabled(!hapticFeedbackDefault)

        val resultAfter = prefs.read(PrefsKeys.HAPTIC_FEEDBACK_IS_ENABLED)
        assertEquals(!hapticFeedbackDefault, resultAfter)
        assertEquals(!hapticFeedbackDefault, settings.getHapticFeedbackIsEnabled())
    }

    @Test
    fun testSetOnBoardProcess() {
        val resultBefore = prefs.read(PrefsKeys.ONBOARD_PROCESS_COMPLETE)
        assertEquals(null, resultBefore)
        assertEquals(onBoardCompleteDefault, settings.getOnBoardProcessIsComplete())

        settings.setOnBoardProcessIsComplete()

        val resultAfter = prefs.read(PrefsKeys.ONBOARD_PROCESS_COMPLETE)
        assertEquals(!onBoardCompleteDefault, resultAfter)
        assertEquals(!onBoardCompleteDefault, settings.getOnBoardProcessIsComplete())
    }

    @Test
    fun testOnBoardHasBeenSatisfied() {
        val resultBefore = prefs.read(PrefsKeys.ONBOARD_PROCESS_COMPLETE)
        assertEquals(null, resultBefore)
        assertEquals(onBoardCompleteDefault, settings.getOnBoardProcessIsComplete())

        assertEquals(true, settings.hasAppOnBoardProcessBeenSatisfied())

        settings.initializeSettings(
            buildConfigDebugDefault,
            !appHasOnBoardDefault,
            hapticFeedbackDefault,
            minPinDefault,
            scrambledPinDefault
        )

        assertEquals(false, settings.hasAppOnBoardProcessBeenSatisfied())
        settings.setOnBoardProcessIsComplete()

        val resultAfter = prefs.read(PrefsKeys.ONBOARD_PROCESS_COMPLETE)
        assertEquals(!onBoardCompleteDefault, resultAfter)
        assertEquals(!onBoardCompleteDefault, settings.getOnBoardProcessIsComplete())
        assertEquals(true, settings.hasAppOnBoardProcessBeenSatisfied())
    }

    @Test
    fun testSetScrambledPin() {
        val resultBefore = prefs.read(PrefsKeys.SCRAMBLED_PIN_IS_ENABLED)
        assertEquals(null, resultBefore)
        assertEquals(scrambledPinDefault, settings.getScrambledPinIsEnabled())

        settings.setScrambledPinIsEnabled(!scrambledPinDefault)

        val resultAfter = prefs.read(PrefsKeys.SCRAMBLED_PIN_IS_ENABLED)
        assertEquals(!scrambledPinDefault, resultAfter)
        assertEquals(!scrambledPinDefault, settings.getScrambledPinIsEnabled())
    }

    @Test
    fun testSetUserPinIsSet() {
        val resultBefore = prefs.read(PrefsKeys.USER_PIN_IS_SET)
        assertEquals(null, resultBefore)
        assertEquals(userPinIsSetDefault, settings.getUserPinIsSet())

        settings.setUserPinIsSet(!userPinIsSetDefault)

        val resultAfter = prefs.read(PrefsKeys.USER_PIN_IS_SET)
        assertEquals(!userPinIsSetDefault, resultAfter)
        assertEquals(!userPinIsSetDefault, settings.getUserPinIsSet())

        settings.setUserPinIsSet(userPinIsSetDefault)

        val resultAfter2 = prefs.read(PrefsKeys.USER_PIN_IS_SET)
        assertEquals(null, resultAfter2)
        assertEquals(userPinIsSetDefault, settings.getUserPinIsSet())
    }

}