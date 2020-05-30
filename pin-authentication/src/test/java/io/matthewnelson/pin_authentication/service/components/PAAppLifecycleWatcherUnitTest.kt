package io.matthewnelson.pin_authentication.service.components

import android.app.Activity
import android.app.Application
import android.content.ComponentCallbacks2
import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.os.PowerManager
import androidx.test.core.app.ApplicationProvider
import io.matthewnelson.pin_authentication.util.annotations.NotForPublicConsumption
import io.matthewnelson.pin_authentication.util.definitions.PALockApplicationEvent
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

@OptIn(NotForPublicConsumption::class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@Config(minSdk = 23, maxSdk = 28)
@RunWith(RobolectricTestRunner::class)
class PAAppLifecycleWatcherUnitTest {

    private val app: Application by lazy {
        ApplicationProvider.getApplicationContext() as Application
    }
    private val roboActivity: Activity by lazy {
        Robolectric.buildActivity(Activity::class.java).create().get()
    }
    private val roboActivity2: Activity by lazy {
        Robolectric.buildActivity(Activity::class.java).create().get()
    }
    private lateinit var paAppLifecycleWatcher: PAAppLifecycleWatcher

    @Before
    fun setup() {
        paAppLifecycleWatcher = PAAppLifecycleWatcher(app)
    }


    @Test
    fun a_testClassInitializationValues() {
        val paLockApplicationEventValue = paAppLifecycleWatcher.getPALockApplicationEvent().value
        assertEquals(PALockApplicationEvent.LOCK, paLockApplicationEventValue)
        assertNotEquals(PALockApplicationEvent.UNLOCK, paLockApplicationEventValue)
        assertEquals(null, paAppLifecycleWatcher.getCurrentActivity())
    }

    @Test
    fun b_testCurrentActivityNameGetterMethod() {
        simulateOnActivityCreated(roboActivity)
        assertEquals(roboActivity.localClassName, paAppLifecycleWatcher.getCurrentActivityName())
    }

    @Test
    fun bb_testIsCurrentActivityAuthManagerActivityMethod() {
        simulateOnActivityCreated(roboActivity)
        assertEquals(false, paAppLifecycleWatcher.isCurrentActivityPinAuthenticationActivity())
    }

    @Test
    fun c_testOnActivityCreated() {
        simulateOnActivityCreated(roboActivity)
        assertEquals(roboActivity, paAppLifecycleWatcher.getCurrentActivity())

        simulateOnActivityCreated(roboActivity2)
        assertNotEquals(roboActivity, paAppLifecycleWatcher.getCurrentActivity())
        assertEquals(roboActivity2, paAppLifecycleWatcher.getCurrentActivity())
    }

    @Test
    fun d_testOnActivityStarted_fromLocked() {
        val preSimulationValue = paAppLifecycleWatcher.getPALockApplicationEvent().value
        assertEquals(PALockApplicationEvent.LOCK, preSimulationValue)
        assertNotEquals(PALockApplicationEvent.UNLOCK, preSimulationValue)

        simulateOnActivityStarted(roboActivity)

        var postSimulationValue = paAppLifecycleWatcher.getPALockApplicationEvent().value
        assertEquals(PALockApplicationEvent.UNLOCK, postSimulationValue)
        assertNotEquals(PALockApplicationEvent.LOCK, postSimulationValue)

        simulateOnActivityStarted(roboActivity)

        postSimulationValue = paAppLifecycleWatcher.getPALockApplicationEvent().value
        assertEquals(PALockApplicationEvent.UNLOCK, postSimulationValue)
        assertNotEquals(PALockApplicationEvent.LOCK, postSimulationValue)
    }

    @Test
    fun e_testOnActivitySaveInstanceState() {
        var preSimulationValue = paAppLifecycleWatcher.getPALockApplicationEvent().value
        if (preSimulationValue == PALockApplicationEvent.LOCK) {
            simulateOnActivityStarted(roboActivity)
            preSimulationValue = paAppLifecycleWatcher.getPALockApplicationEvent().value
        }
        assertEquals(PALockApplicationEvent.UNLOCK, preSimulationValue)
        assertNotEquals(PALockApplicationEvent.LOCK, preSimulationValue)

        simulateOnActivitySaveInstanceState(roboActivity, false)

        var postSimulationValue = paAppLifecycleWatcher.getPALockApplicationEvent().value
        assertEquals(PALockApplicationEvent.LOCK, postSimulationValue)
        assertNotEquals(PALockApplicationEvent.UNLOCK, postSimulationValue)

        simulateOnActivityStarted(roboActivity)

        postSimulationValue = paAppLifecycleWatcher.getPALockApplicationEvent().value
        assertEquals(PALockApplicationEvent.UNLOCK, postSimulationValue)
        assertNotEquals(PALockApplicationEvent.LOCK, postSimulationValue)

        simulateOnActivitySaveInstanceState(roboActivity, true)

        postSimulationValue = paAppLifecycleWatcher.getPALockApplicationEvent().value
        assertEquals(PALockApplicationEvent.UNLOCK, postSimulationValue)
        assertNotEquals(PALockApplicationEvent.LOCK, postSimulationValue)
    }

    @Test
    fun f_testOnTrimMemory() {
        var preSimulationValue = paAppLifecycleWatcher.getPALockApplicationEvent().value
        if (preSimulationValue == PALockApplicationEvent.LOCK) {
            simulateOnActivityStarted(roboActivity)
            preSimulationValue = paAppLifecycleWatcher.getPALockApplicationEvent().value
        }
        assertEquals(PALockApplicationEvent.UNLOCK, preSimulationValue)
        assertNotEquals(PALockApplicationEvent.LOCK, preSimulationValue)

        simulateOnTrimMemory()
        val postSimulationValue = paAppLifecycleWatcher.getPALockApplicationEvent().value
        assertEquals(PALockApplicationEvent.LOCK, postSimulationValue)
        assertNotEquals(PALockApplicationEvent.UNLOCK, postSimulationValue)
    }

    @Test
    fun g_screenRotation() {
        val currentOrientation = roboActivity.resources.configuration.orientation
        simulateOnActivityCreated(roboActivity)
        assertEquals(false, paAppLifecycleWatcher.isScreenRotationOccurring())

        val newOrientation = simulateScreenRotation(currentOrientation)
        assertEquals(true, paAppLifecycleWatcher.isScreenRotationOccurring())
        roboActivity.resources.configuration.orientation = newOrientation

        simulateOnActivityStarted(roboActivity)
        assertEquals(false, paAppLifecycleWatcher.isScreenRotationOccurring())
    }

    private fun simulateOnActivityCreated(activity: Activity) {
        paAppLifecycleWatcher.onActivityCreated(activity, null)
    }

    private fun simulateOnActivityStarted(activity: Activity) {
        paAppLifecycleWatcher.onActivityStarted(activity)
    }

    private fun simulateOnActivitySaveInstanceState(activity: Activity, isInteractive: Boolean) {
        val pm = activity.getSystemService(Context.POWER_SERVICE) as PowerManager
        val shadowPowerManager: ShadowPowerManager = shadowOf(pm)
        shadowPowerManager.setIsInteractive(isInteractive)

        paAppLifecycleWatcher.onActivitySaveInstanceState(activity, Bundle())
    }

    private fun simulateOnTrimMemory() {
        paAppLifecycleWatcher.onTrimMemory(ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN)
    }

    private fun simulateScreenRotation(currentOrientation: Int): Int {
        val newConfiguration = Configuration()
        if (currentOrientation == 1) {
            newConfiguration.orientation = 2
        } else if (currentOrientation == 2) {
            newConfiguration.orientation = 1
        }
        paAppLifecycleWatcher.onConfigurationChanged(newConfiguration)
        return newConfiguration.orientation
    }

}