package io.matthewnelson.pin_authentication.service.components

import android.content.Intent
import io.matthewnelson.pin_authentication.ui.PinAuthenticationActivity
import io.matthewnelson.pin_authentication.util.definitions.PAAuthenticationState
import io.matthewnelson.pin_authentication.util.definitions.PALockApplicationEvent
import io.matthewnelson.pin_authentication.util.definitions.PinEntryStates.PinEntryState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


/**
 * @suppress
 * This class observes when to lock the application due to it being sent to the background, or
 * if the user locks their device while the application is open.
 *
 * This class triggers events based on the LiveData being observed
 *
 * @see [AppLifecycleWatcher]
 *
 * @param [appLifecycleWatcher] [AppLifecycleWatcher]
 * @param [coroutines] [Coroutines]
 * @param [pinSecurity] [PinSecurity]
 * @param [settings] [Settings]
 * @param [viewData] [ViewData]
 * */
internal class AppLockObserver(
    private val appLifecycleWatcher: AppLifecycleWatcher,
    private val coroutines: Coroutines,
    private val pinSecurity: PinSecurity,
    private val settings: Settings,
    private val viewData: ViewData
) {

    //////////////////////////
    // Authentication State //
    //////////////////////////
    private var authenticationState = PAAuthenticationState.REQUIRED
    private var backgroundLogoutTimer = 0L

    init {
        appLifecycleWatcher.getPALockApplicationEvent().observeForever {
            when (it) {
                PALockApplicationEvent.LOCK -> {
                    if (authenticationState == PAAuthenticationState.NOT_REQUIRED) {
                        if (backgroundLogoutTimer > 0L) {
                            launchAuthInvalidationJobIfInactive()
                        }
                    }
                }
                PALockApplicationEvent.UNLOCK -> {
                    cancelAuthInvalidationJobIfNotComplete()
                    hijackApp(PinEntryState.LOGIN)
                }
                else -> {}
            }
        }
    }

    fun setAuthenticationStateNotRequired() {
        authenticationState = PAAuthenticationState.NOT_REQUIRED
    }

    /**
     * Sets the [pinEntryState] LiveData prior to launching [PinAuthenticationActivity] so that
     * the ViewModel can present the needed information for the user to consume.
     *
     * @param [pinEntryState] @PAPinEntryState.PinEntryState Int
     * */
    fun hijackApp(pinEntryState: @PinEntryState Int) {

        // Inhibit calls if the app on-board process is not satisfied.
        if (!settings.hasAppOnBoardProcessBeenSatisfied()) {
            return
        }

        // If pin security is disabled inhibit all calls to hijackApp, other than a request
        // to enable pin security, from altering paViewData.pinEntryState LiveData.
        if (!pinSecurity.isPinSecurityEnabled() && !pinSecurity.isCurrentRequestKeyPinSecurity()) {
            return
        }

        // Inhibit alteration of LiveData multiple times if configuration has already been
        // set to SET_PIN_FIRST_TIME. Will allow for a first time pass because
        // paViewData.pinEntryState resets to IDLE after completion of the Activity.
        if (viewData.getPinEntryState().value == PinEntryState.SET_PIN_FIRST_TIME) {
            return
        }

        // Inhibit alteration of LiveData multiple times if configuration has already been
        // set to ENABLE_PIN_SECURITY. Will allow for a first time pass because
        // paViewData.pinEntryState resets to IDLE after completion of the Activity.
        if (viewData.getPinEntryState().value == PinEntryState.ENABLE_PIN_SECURITY) {
            return
        }

        when (pinEntryState) {
            PinEntryState.CONFIRM_PIN -> {
                viewData.setPinEntryState(pinEntryState)
                startPinAuthenticationActivity()
            }
            PinEntryState.ENABLE_PIN_SECURITY -> {
                viewData.setPinEntryState(pinEntryState)
                startPinAuthenticationActivity()
            }
            PinEntryState.RESET_PIN -> {
                viewData.setPinEntryState(pinEntryState)
                startPinAuthenticationActivity()
            }
            else -> {

                // Inhibit launching into LOGIN or SET_PIN_FIRST_TIME configuration if
                // authentication state is not required.
                if (authenticationState != PAAuthenticationState.REQUIRED) {
                    return
                }

                val setPinEntryState =
                    if (settings.getUserPinIsSet()) {
                        PinEntryState.LOGIN
                    } else {
                        PinEntryState.SET_PIN_FIRST_TIME
                    }
                viewData.setPinEntryState(setPinEntryState)
                startPinAuthenticationActivity()
            }
        }
    }

    private fun startPinAuthenticationActivity() {
        if (!appLifecycleWatcher.isCurrentActivityPinAuthenticationActivity()) {
            appLifecycleWatcher.getCurrentActivity()?.apply {
                val intent = Intent(this, PinAuthenticationActivity::class.java)
                startActivity(intent)
            }
        }
    }


    ///////////////////////////////////////////
    // Authentication Invalidation Coroutine //
    ///////////////////////////////////////////
    private lateinit var authInvalidationJob: Job

    private fun cancelAuthInvalidationJobIfNotComplete() {
        if (::authInvalidationJob.isInitialized && authInvalidationJob.isActive) {
            authInvalidationJob.cancel()
        }
    }

    private fun launchAuthInvalidationJobIfInactive() {
        if (::authInvalidationJob.isInitialized && authInvalidationJob.isActive) {
            return
        }

        authInvalidationJob = coroutines.getScopeUI().launch {
            delay(backgroundLogoutTimer)
            authenticationState = PAAuthenticationState.REQUIRED
        }
    }


    //////////////////////////////////////////////////
    // PinAuthenticaion build method initialization //
    //////////////////////////////////////////////////
    private var initOnceCounter = 0

    fun initializeAppLockObserver(backgroundLogoutTimerInitValue: Long) {
        if (initOnceCounter < 1) {
            backgroundLogoutTimer = backgroundLogoutTimerInitValue
            initOnceCounter++
        }
    }

}