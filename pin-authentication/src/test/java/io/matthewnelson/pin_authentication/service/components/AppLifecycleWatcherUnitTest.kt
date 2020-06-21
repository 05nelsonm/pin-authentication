package io.matthewnelson.pin_authentication.service.components

import android.app.Activity
import android.app.Application
import android.content.ComponentCallbacks2
import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.os.PowerManager
import androidx.test.core.app.ApplicationProvider
import io.matthewnelson.pin_authentication.util.definitions.LockApplicationEvents.LockApplicationEvent
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowPowerManager
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@Config(minSdk = 23, maxSdk = 28)
@RunWith(RobolectricTestRunner::class)
class AppLifecycleWatcherUnitTest {

    private val app: Application by lazy {
        ApplicationProvider.getApplicationContext() as Application
    }
    private val roboActivity: Activity by lazy {
        Robolectric.buildActivity(Activity::class.java).create().get()
    }
    private val roboActivity2: Activity by lazy {
        Robolectric.buildActivity(Activity::class.java).create().get()
    }
    private lateinit var appLifecycleWatcher: AppLifecycleWatcher

    @Before
    fun setup() {
        appLifecycleWatcher = AppLifecycleWatcher(app)
    }


    @Test
    fun a_testClassInitializationValues() {
        val paLockApplicationEventValue = appLifecycleWatcher.getPALockApplicationEvent().value
        assertEquals(LockApplicationEvent.LOCK, paLockApplicationEventValue)
        assertNotEquals(LockApplicationEvent.UNLOCK, paLockApplicationEventValue)
        assertEquals(null, appLifecycleWatcher.getCurrentActivity())
    }

    @Test
    fun b_testCurrentActivityNameGetterMethod() {
        simulateOnActivityCreated(roboActivity)
        assertEquals(roboActivity.localClassName, appLifecycleWatcher.getCurrentActivityName())
    }

    @Test
    fun bb_testIsCurrentActivityAuthManagerActivityMethod() {
        simulateOnActivityCreated(roboActivity)
        assertEquals(false, appLifecycleWatcher.isCurrentActivityPinAuthenticationActivity())
    }

    @Test
    fun c_testOnActivityCreated() {
        simulateOnActivityCreated(roboActivity)
        assertEquals(roboActivity, appLifecycleWatcher.getCurrentActivity())

        simulateOnActivityCreated(roboActivity2)
        assertNotEquals(roboActivity, appLifecycleWatcher.getCurrentActivity())
        assertEquals(roboActivity2, appLifecycleWatcher.getCurrentActivity())
    }

    @Test
    fun d_testOnActivityStarted_fromLocked() {
        val preSimulationValue = appLifecycleWatcher.getPALockApplicationEvent().value
        assertEquals(LockApplicationEvent.LOCK, preSimulationValue)
        assertNotEquals(LockApplicationEvent.UNLOCK, preSimulationValue)

        simulateOnActivityStarted(roboActivity)

        var postSimulationValue = appLifecycleWatcher.getPALockApplicationEvent().value
        assertEquals(LockApplicationEvent.UNLOCK, postSimulationValue)
        assertNotEquals(LockApplicationEvent.LOCK, postSimulationValue)

        simulateOnActivityStarted(roboActivity)

        postSimulationValue = appLifecycleWatcher.getPALockApplicationEvent().value
        assertEquals(LockApplicationEvent.UNLOCK, postSimulationValue)
        assertNotEquals(LockApplicationEvent.LOCK, postSimulationValue)
    }

    @Test
    fun e_testOnActivitySaveInstanceState() {
        var preSimulationValue = appLifecycleWatcher.getPALockApplicationEvent().value
        if (preSimulationValue == LockApplicationEvent.LOCK) {
            simulateOnActivityStarted(roboActivity)
            preSimulationValue = appLifecycleWatcher.getPALockApplicationEvent().value
        }
        assertEquals(LockApplicationEvent.UNLOCK, preSimulationValue)
        assertNotEquals(LockApplicationEvent.LOCK, preSimulationValue)

        simulateOnActivitySaveInstanceState(roboActivity, false)

        var postSimulationValue = appLifecycleWatcher.getPALockApplicationEvent().value
        assertEquals(LockApplicationEvent.LOCK, postSimulationValue)
        assertNotEquals(LockApplicationEvent.UNLOCK, postSimulationValue)

        simulateOnActivityStarted(roboActivity)

        postSimulationValue = appLifecycleWatcher.getPALockApplicationEvent().value
        assertEquals(LockApplicationEvent.UNLOCK, postSimulationValue)
        assertNotEquals(LockApplicationEvent.LOCK, postSimulationValue)

        simulateOnActivitySaveInstanceState(roboActivity, true)

        postSimulationValue = appLifecycleWatcher.getPALockApplicationEvent().value
        assertEquals(LockApplicationEvent.UNLOCK, postSimulationValue)
        assertNotEquals(LockApplicationEvent.LOCK, postSimulationValue)
    }

    @Test
    fun f_testOnTrimMemory() {
        var preSimulationValue = appLifecycleWatcher.getPALockApplicationEvent().value
        if (preSimulationValue == LockApplicationEvent.LOCK) {
            simulateOnActivityStarted(roboActivity)
            preSimulationValue = appLifecycleWatcher.getPALockApplicationEvent().value
        }
        assertEquals(LockApplicationEvent.UNLOCK, preSimulationValue)
        assertNotEquals(LockApplicationEvent.LOCK, preSimulationValue)

        simulateOnTrimMemory()
        val postSimulationValue = appLifecycleWatcher.getPALockApplicationEvent().value
        assertEquals(LockApplicationEvent.LOCK, postSimulationValue)
        assertNotEquals(LockApplicationEvent.UNLOCK, postSimulationValue)
    }

    @Test
    fun g_screenRotation() {
        val currentOrientation = roboActivity.resources.configuration.orientation
        simulateOnActivityCreated(roboActivity)
        assertEquals(false, appLifecycleWatcher.isScreenRotationOccurring())

        val newOrientation = simulateScreenRotation(currentOrientation)
        assertEquals(true, appLifecycleWatcher.isScreenRotationOccurring())
        roboActivity.resources.configuration.orientation = newOrientation

        simulateOnActivityStarted(roboActivity)
        assertEquals(false, appLifecycleWatcher.isScreenRotationOccurring())
    }

    private fun simulateOnActivityCreated(activity: Activity) {
        appLifecycleWatcher.onActivityCreated(activity, null)
    }

    private fun simulateOnActivityStarted(activity: Activity) {
        appLifecycleWatcher.onActivityStarted(activity)
    }

    private fun simulateOnActivitySaveInstanceState(activity: Activity, isInteractive: Boolean) {
        val pm = activity.getSystemService(Context.POWER_SERVICE) as PowerManager
        val shadowPowerManager: ShadowPowerManager = shadowOf(pm)
        shadowPowerManager.setIsInteractive(isInteractive)

        appLifecycleWatcher.onActivitySaveInstanceState(activity, Bundle())
    }

    private fun simulateOnTrimMemory() {
        appLifecycleWatcher.onTrimMemory(ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN)
    }

    private fun simulateScreenRotation(currentOrientation: Int): Int {
        val newConfiguration = Configuration()
        if (currentOrientation == 1) {
            newConfiguration.orientation = 2
        } else if (currentOrientation == 2) {
            newConfiguration.orientation = 1
        }
        appLifecycleWatcher.onConfigurationChanged(newConfiguration)
        return newConfiguration.orientation
    }

}