package io.matthewnelson.pin_authentication.service.components

import android.app.Activity
import android.app.Application
import android.content.ComponentCallbacks2
import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.os.PowerManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.matthewnelson.pin_authentication.util.definitions.LockApplicationEvents.LockApplicationEvent

/**
 * @suppress
 * Monitors the Application's Activity and Component life-cycles such that when the user
 * sends the application to the background, or locks their device while the app is open,
 * it will update LiveData which is being observed by [AppLockObserver]
 *
 * @param [app] Application
 * */
internal class AppLifecycleWatcher(
    private val app: Application
) : Application.ActivityLifecycleCallbacks, ComponentCallbacks2 {

    //////////////////////
    // Lock Application //
    //////////////////////
    private val paLockApplicationEvent =
        MutableLiveData<@LockApplicationEvent Int>(LockApplicationEvent.LOCK)

    fun getPALockApplicationEvent(): LiveData<@LockApplicationEvent Int> =
        paLockApplicationEvent


    //////////////////////
    // Current Activity //
    //////////////////////
    private companion object {
        const val PIN_AUTHENTICATION_ACTIVITY_NAME = "io.matthewnelson.pin_authentication" +
                ".ui.PinAuthenticationActivity"
    }

    private var currentActivity: Activity? = null

    fun getCurrentActivity(): Activity? =
        currentActivity

    fun getCurrentActivityName(): String =
        currentActivity?.localClassName ?: ""

    fun isCurrentActivityPinAuthenticationActivity(): Boolean =
        getCurrentActivityName() == PIN_AUTHENTICATION_ACTIVITY_NAME

    private fun updateCurrentActivity(activity: Activity) {
        if (currentActivity != activity) {
            currentActivity = activity
        }
    }


    /////////////////////
    // Screen Rotation //
    /////////////////////
    private var screenRotationOccurring = false

    fun isScreenRotationOccurring(): Boolean =
        screenRotationOccurring


    ///////////////
    // Callbacks //
    ///////////////
    init {
        app.registerActivityLifecycleCallbacks(this)
        app.registerComponentCallbacks(this)
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        updateCurrentActivity(activity)
    }

    override fun onActivityStarted(activity: Activity) {
        updateCurrentActivity(activity)
        if (screenRotationOccurring) {
            screenRotationOccurring = false
        }
        if (paLockApplicationEvent.value == LockApplicationEvent.LOCK) {
            paLockApplicationEvent.value = LockApplicationEvent.UNLOCK
        }
    }

    override fun onActivityResumed(activity: Activity) {
        updateCurrentActivity(activity)
    }

    override fun onActivityPaused(activity: Activity) {}

    override fun onActivityStopped(activity: Activity) {}

    override fun onActivityDestroyed(activity: Activity) {}

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
        val pm = activity.getSystemService(Context.POWER_SERVICE) as PowerManager?
        if (pm?.isInteractive == false
            && paLockApplicationEvent.value == LockApplicationEvent.UNLOCK) {
            paLockApplicationEvent.value = LockApplicationEvent.LOCK
        }
    }

    override fun onLowMemory() {}

    override fun onConfigurationChanged(newConfig: Configuration) {
        val currentOrientation = currentActivity?.resources?.configuration?.orientation
        if (currentOrientation != null && newConfig.orientation != currentOrientation) {
            screenRotationOccurring = true
        }
    }

    override fun onTrimMemory(level: Int) {
        if (level == ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN) {
            paLockApplicationEvent.value = LockApplicationEvent.LOCK
        }
    }
}