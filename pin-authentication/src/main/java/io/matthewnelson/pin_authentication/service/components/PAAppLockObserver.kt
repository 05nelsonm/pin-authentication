package io.matthewnelson.pin_authentication.service.components

import android.content.Intent
import io.matthewnelson.pin_authentication.ui.PinAuthenticationActivity
import io.matthewnelson.pin_authentication.util.annotations.NotForPublicConsumption
import io.matthewnelson.pin_authentication.util.definitions.PAAuthenticationState
import io.matthewnelson.pin_authentication.util.definitions.PALockApplicationEvent
import io.matthewnelson.pin_authentication.util.definitions.PAPinEntryState
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
 * @see [PAAppLifecycleWatcher]
 *
 * @param [paAppLifecycleWatcher] [PAAppLifecycleWatcher]
 * @param [paCoroutines] [PACoroutines]
 * @param [paPinSecurity] [PAPinSecurity]
 * @param [paSettings] [PASettings]
 * @param [paViewData] [PAViewData]
 * */
@NotForPublicConsumption
class PAAppLockObserver(
    private val paAppLifecycleWatcher: PAAppLifecycleWatcher,
    private val paCoroutines: PACoroutines,
    private val paPinSecurity: PAPinSecurity,
    private val paSettings: PASettings,
    private val paViewData: PAViewData
) {

    //////////////////////////
    // Authentication State //
    //////////////////////////
    private var authenticationState = PAAuthenticationState.REQUIRED
    private var backgroundLogoutTimer = 0L

    init {
        paAppLifecycleWatcher.getPALockApplicationEvent().observeForever {
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
                    hijackApp(PAPinEntryState.LOGIN)
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
    fun hijackApp(pinEntryState: @PAPinEntryState.PinEntryState Int) {

        // Inhibit calls if the app on-board process is not satisfied.
        if (!paSettings.hasAppOnBoardProcessBeenSatisfied()) {
            return
        }

        // If pin security is disabled inhibit all calls to hijackApp, other than a request
        // to enable pin security, from altering paViewData.pinEntryState LiveData.
        if (!paPinSecurity.isPinSecurityEnabled() && !paPinSecurity.isCurrentRequestKeyPinSecurity()) {
            return
        }

        // Inhibit alteration of LiveData multiple times if configuration has already been
        // set to SET_PIN_FIRST_TIME. Will allow for a first time pass because
        // paViewData.pinEntryState resets to IDLE after completion of the Activity.
        if (paViewData.getPinEntryState().value == PAPinEntryState.SET_PIN_FIRST_TIME) {
            return
        }

        // Inhibit alteration of LiveData multiple times if configuration has already been
        // set to ENABLE_PIN_SECURITY. Will allow for a first time pass because
        // paViewData.pinEntryState resets to IDLE after completion of the Activity.
        if (paViewData.getPinEntryState().value == PAPinEntryState.ENABLE_PIN_SECURITY) {
            return
        }

        when (pinEntryState) {
            PAPinEntryState.CONFIRM_PIN -> {
                paViewData.setPinEntryState(pinEntryState)
                startPinAuthenticationActivity()
            }
            PAPinEntryState.ENABLE_PIN_SECURITY -> {
                paViewData.setPinEntryState(pinEntryState)
                startPinAuthenticationActivity()
            }
            PAPinEntryState.RESET_PIN -> {
                paViewData.setPinEntryState(pinEntryState)
                startPinAuthenticationActivity()
            }
            else -> {

                // Inhibit launching into LOGIN or SET_PIN_FIRST_TIME configuration if
                // authentication state is not required.
                if (authenticationState != PAAuthenticationState.REQUIRED) {
                    return
                }

                val setPinEntryState =
                    if (paSettings.getUserPinIsSet()) {
                        PAPinEntryState.LOGIN
                    } else {
                        PAPinEntryState.SET_PIN_FIRST_TIME
                    }
                paViewData.setPinEntryState(setPinEntryState)
                startPinAuthenticationActivity()
            }
        }
    }

    private fun startPinAuthenticationActivity() {
        if (!paAppLifecycleWatcher.isCurrentActivityPinAuthenticationActivity()) {
            paAppLifecycleWatcher.getCurrentActivity()?.apply {
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

        authInvalidationJob = paCoroutines.getScopeUI().launch {
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