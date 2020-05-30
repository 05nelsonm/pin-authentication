package io.matthewnelson.pin_authentication.service.components

import android.app.Application
import androidx.test.core.app.ApplicationProvider
import io.matthewnelson.encrypted_storage.EncryptedStorage
import io.matthewnelson.pin_authentication.util.PAPrefsKeys
import io.matthewnelson.pin_authentication.util.annotations.NotForPublicConsumption
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import kotlin.test.assertEquals

@OptIn(NotForPublicConsumption::class)
@Config(minSdk = 23, maxSdk = 28)
@RunWith(RobolectricTestRunner::class)
class PASettingsUnitTest {

    private val app: Application by lazy {
        ApplicationProvider.getApplicationContext() as Application
    }
    private lateinit var prefs: EncryptedStorage.Prefs
    private lateinit var paSettings: PASettings

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
        paSettings = PASettings(prefs)
    }

    @Test
    fun testInitialValues() {
        assertEquals(buildConfigDebugDefault, paSettings.getBuildConfigDebug())
        assertEquals(appHasOnBoardDefault, paSettings.getAppHasOnBoardProcess())
        assertEquals(hapticFeedbackDefault, paSettings.getHapticFeedbackIsEnabled())
        assertEquals(minPinDefault, paSettings.getMinPinLength())
        assertEquals(onBoardCompleteDefault, paSettings.getOnBoardProcessIsComplete())
        assertEquals(scrambledPinDefault, paSettings.getScrambledPinIsEnabled())
        assertEquals(userPinIsSetDefault, paSettings.getUserPinIsSet())
    }

    @Test
    fun testInitializeSettings() {
        val buildConfigDebugInit = true
        val hasOnBoardInit = true
        val hapticFeedbackInit = true
        val minPinInit = 8
        val scrambledPinInit = true

        paSettings.initializeSettings(
            buildConfigDebugInit,
            hasOnBoardInit,
            hapticFeedbackInit,
            minPinInit,
            scrambledPinInit
        )

        assertEquals(buildConfigDebugInit, paSettings.getBuildConfigDebug())
        assertEquals(hasOnBoardInit, paSettings.getAppHasOnBoardProcess())
        assertEquals(hapticFeedbackInit, paSettings.getHapticFeedbackIsEnabled())
        assertEquals(minPinInit, paSettings.getMinPinLength())
        assertEquals(false, paSettings.getOnBoardProcessIsComplete())
        assertEquals(scrambledPinInit, paSettings.getScrambledPinIsEnabled())
        assertEquals(false, paSettings.getUserPinIsSet())

        val buildConfigDebug2 = false
        val hasOnBoardInit2 = false
        val hapticFeedbackInit2 = false
        val minPinInit2 = 5
        val scrambledPinInit2 = false

        paSettings.initializeSettings(
            buildConfigDebug2,
            hasOnBoardInit2,
            hapticFeedbackInit2,
            minPinInit2,
            scrambledPinInit2
        )

        assertEquals(buildConfigDebugInit, paSettings.getBuildConfigDebug())
        assertEquals(hasOnBoardInit, paSettings.getAppHasOnBoardProcess())
        assertEquals(hapticFeedbackInit, paSettings.getHapticFeedbackIsEnabled())
        assertEquals(minPinInit, paSettings.getMinPinLength())
        assertEquals(false, paSettings.getOnBoardProcessIsComplete())
        assertEquals(scrambledPinInit, paSettings.getScrambledPinIsEnabled())
        assertEquals(false, paSettings.getUserPinIsSet())
    }

    @Test
    fun testLoadSettingsFromSharedPreferences() {
        val onBoardProcessSetTrue = true
        val hapticPrefs = true
        val onBoardPrefs = true
        val scrambledPrefs = true
        val userPinPrefs = true
        prefs.write(PAPrefsKeys.HAPTIC_FEEDBACK_IS_ENABLED, hapticPrefs)
        prefs.write(PAPrefsKeys.ONBOARD_PROCESS_COMPLETE, onBoardPrefs)
        prefs.write(PAPrefsKeys.SCRAMBLED_PIN_IS_ENABLED, scrambledPrefs)
        prefs.write(PAPrefsKeys.USER_PIN_IS_SET, userPinPrefs)

        paSettings.initializeSettings(
            buildConfigDebugDefault,
            onBoardProcessSetTrue, // Must turn to true so on-board complete gets loaded
            hapticFeedbackDefault,
            minPinDefault,
            scrambledPinDefault
        )

        assertEquals(onBoardProcessSetTrue, paSettings.getAppHasOnBoardProcess())
        assertEquals(hapticPrefs, paSettings.getHapticFeedbackIsEnabled())
        assertEquals(minPinDefault, paSettings.getMinPinLength())
        assertEquals(onBoardPrefs, paSettings.getOnBoardProcessIsComplete())
        assertEquals(scrambledPrefs, paSettings.getScrambledPinIsEnabled())
        assertEquals(userPinPrefs, paSettings.getUserPinIsSet())
    }

    @Test
    fun testSetHapticFeedBack() {
        val resultBefore = prefs.read(PAPrefsKeys.HAPTIC_FEEDBACK_IS_ENABLED)
        assertEquals(null, resultBefore)
        assertEquals(hapticFeedbackDefault, paSettings.getHapticFeedbackIsEnabled())

        paSettings.setHapticFeedbackIsEnabled(!hapticFeedbackDefault)

        val resultAfter = prefs.read(PAPrefsKeys.HAPTIC_FEEDBACK_IS_ENABLED)
        assertEquals(!hapticFeedbackDefault, resultAfter)
        assertEquals(!hapticFeedbackDefault, paSettings.getHapticFeedbackIsEnabled())
    }

    @Test
    fun testSetOnBoardProcess() {
        val resultBefore = prefs.read(PAPrefsKeys.ONBOARD_PROCESS_COMPLETE)
        assertEquals(null, resultBefore)
        assertEquals(onBoardCompleteDefault, paSettings.getOnBoardProcessIsComplete())

        paSettings.setOnBoardProcessIsComplete()

        val resultAfter = prefs.read(PAPrefsKeys.ONBOARD_PROCESS_COMPLETE)
        assertEquals(!onBoardCompleteDefault, resultAfter)
        assertEquals(!onBoardCompleteDefault, paSettings.getOnBoardProcessIsComplete())
    }

    @Test
    fun testOnBoardHasBeenSatisfied() {
        val resultBefore = prefs.read(PAPrefsKeys.ONBOARD_PROCESS_COMPLETE)
        assertEquals(null, resultBefore)
        assertEquals(onBoardCompleteDefault, paSettings.getOnBoardProcessIsComplete())

        assertEquals(true, paSettings.hasAppOnBoardProcessBeenSatisfied())

        paSettings.initializeSettings(
            buildConfigDebugDefault,
            !appHasOnBoardDefault,
            hapticFeedbackDefault,
            minPinDefault,
            scrambledPinDefault
        )

        assertEquals(false, paSettings.hasAppOnBoardProcessBeenSatisfied())
        paSettings.setOnBoardProcessIsComplete()

        val resultAfter = prefs.read(PAPrefsKeys.ONBOARD_PROCESS_COMPLETE)
        assertEquals(!onBoardCompleteDefault, resultAfter)
        assertEquals(!onBoardCompleteDefault, paSettings.getOnBoardProcessIsComplete())
        assertEquals(true, paSettings.hasAppOnBoardProcessBeenSatisfied())
    }

    @Test
    fun testSetScrambledPin() {
        val resultBefore = prefs.read(PAPrefsKeys.SCRAMBLED_PIN_IS_ENABLED)
        assertEquals(null, resultBefore)
        assertEquals(scrambledPinDefault, paSettings.getScrambledPinIsEnabled())

        paSettings.setScrambledPinIsEnabled(!scrambledPinDefault)

        val resultAfter = prefs.read(PAPrefsKeys.SCRAMBLED_PIN_IS_ENABLED)
        assertEquals(!scrambledPinDefault, resultAfter)
        assertEquals(!scrambledPinDefault, paSettings.getScrambledPinIsEnabled())
    }

    @Test
    fun testSetUserPinIsSet() {
        val resultBefore = prefs.read(PAPrefsKeys.USER_PIN_IS_SET)
        assertEquals(null, resultBefore)
        assertEquals(userPinIsSetDefault, paSettings.getUserPinIsSet())

        paSettings.setUserPinIsSet(!userPinIsSetDefault)

        val resultAfter = prefs.read(PAPrefsKeys.USER_PIN_IS_SET)
        assertEquals(!userPinIsSetDefault, resultAfter)
        assertEquals(!userPinIsSetDefault, paSettings.getUserPinIsSet())

        paSettings.setUserPinIsSet(userPinIsSetDefault)

        val resultAfter2 = prefs.read(PAPrefsKeys.USER_PIN_IS_SET)
        assertEquals(null, resultAfter2)
        assertEquals(userPinIsSetDefault, paSettings.getUserPinIsSet())
    }

}