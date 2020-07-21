/*
* Copyright (C) 2020 Matthew Nelson
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program.  If not, see <https://www.gnu.org/licenses/>.
* */
package io.matthewnelson.pin_authentication.service.components

import android.content.Intent
import io.matthewnelson.pin_authentication.ui.PinAuthenticationActivity
import io.matthewnelson.pin_authentication.util.definitions.AuthenticationStates.AuthenticationState
import io.matthewnelson.pin_authentication.util.definitions.LockApplicationEvents.LockApplicationEvent
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
    private var authenticationState = AuthenticationState.REQUIRED
    private var backgroundLogoutTimer = 0L

    init {
        appLifecycleWatcher.getPALockApplicationEvent().observeForever {
            when (it) {
                LockApplicationEvent.LOCK -> {
                    if (authenticationState == AuthenticationState.NOT_REQUIRED) {
                        if (backgroundLogoutTimer > 0L) {
                            launchAuthInvalidationJobIfInactive()
                        }
                    }
                }
                LockApplicationEvent.UNLOCK -> {
                    cancelAuthInvalidationJobIfNotComplete()
                    hijackApp(PinEntryState.LOGIN)
                }
                else -> {}
            }
        }
    }

    fun setAuthenticationStateNotRequired() {
        authenticationState = AuthenticationState.NOT_REQUIRED
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
                if (authenticationState != AuthenticationState.REQUIRED) {
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
            authenticationState = AuthenticationState.REQUIRED
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