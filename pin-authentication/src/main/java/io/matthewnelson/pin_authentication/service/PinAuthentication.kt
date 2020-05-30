package io.matthewnelson.pin_authentication.service

import android.app.Activity
import android.app.Application
import androidx.annotation.ColorRes
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import io.matthewnelson.pin_authentication.R.id
import io.matthewnelson.pin_authentication.di.PAInjection
import io.matthewnelson.pin_authentication.di.application.PAApplicationComponent
import io.matthewnelson.pin_authentication.service.components.*
import io.matthewnelson.pin_authentication.ui.PinAuthenticationActivity
import io.matthewnelson.pin_authentication.util.PAPrefsKeys
import io.matthewnelson.pin_authentication.util.BindingAdapters
import io.matthewnelson.pin_authentication.util.definitions.PAPinEntryState
import io.matthewnelson.pin_authentication.util.definitions.PAScreenType
import io.matthewnelson.pin_authentication.di.application.DaggerPAApplicationComponent
import io.matthewnelson.pin_authentication.util.annotations.NotForPublicConsumption

@OptIn(NotForPublicConsumption::class)
sealed class PinAuthentication {

    /**
     * Call from the Application's onCreate() to implement
     *
     * @sample [io.matthewnelson.pin_authentication_demo.App.initializePinAuthentication]
     * */
    class Builder {

        /**
         * Sets the Application which is used throughout [PinAuthentication]. It initializes
         * [PinAuthentication]'s [DaggerPAApplicationComponent] which then is used to inject
         * classes as needed.
         * See [PAInjection]
         * See [Companion.injected]
         *
         * It also sets the [PinAuthenticationActivity]'s window flag to secure for release
         * builds to inhibit screen capture of the user's PIN.
         *
         * See [Builder] for sample code.
         *
         * @param [application] Application
         * @param [buildConfigDebug] Boolean - (send BuildConfig.DEBUG)
         *
         * @return [PABuilder]
         * */
        fun setApplicationAndBuildConfig(
            application: Application,
            buildConfigDebug: Boolean
        ): PABuilder {
            val applicationComponent =
                DaggerPAApplicationComponent
                    .builder()
                    .bindApplication(application)
                    .build()

            val amInjection = PAInjection(applicationComponent)
            return PABuilder(applicationComponent, amInjection, buildConfigDebug)
        }

        /**
         * Meant to only be used after calling [Builder.setApplicationAndBuildConfig], which
         * returns this class.
         *
         * See [Builder] for sample code.
         *
         * @param [applicationComponent] AMApplicationComponent
         * @param [paInjection] AMInjection
         * @param [buildConfigDebug] Boolean
         * */
        class PABuilder(
            private val applicationComponent: PAApplicationComponent,
            private val paInjection: PAInjection,
            private val buildConfigDebug: Boolean
        ) {
            private var appHasOnBoardProcessInitValue = false
            private var backgroundLogoutTimerInitValue = 0L
            private var hapticFeedbackIsEnabledInitValue = false
            private var minPinLengthInitValue = 4
            private var scrambledPinIsEnabledInitValue = false

            private var enableWrongPinLockout = false

            init {
                paApplicationComponent = applicationComponent
                injected = paInjection
            }

            /**
             * By default, application has on-board process is DISABLED.
             *
             * Enabling this will delay everything until on-boarding process has been completed.
             *
             * At end of your on-boarding, hit the [Controller.completeOnBoardProcess] method
             * to kick everything off.
             *
             * See [Builder] for sample code.
             *
             * @return [PABuilder]
             * */
            fun applicationHasOnBoardProcess(): PABuilder {
                appHasOnBoardProcessInitValue = true
                return this
            }

            /**
             * By default, background logout timer is DISABLED.
             *
             * Sets the length of time the application spends in the background before
             * automatically logging out.
             *
             * The _maximum_ time to set this value is 29s.
             *
             * See [Builder] for sample code.
             *
             * @see [PAAppLockObserver.launchAuthInvalidationJobIfInactive]
             *
             * @param [secondsLessThan30] Int - (Set to 0 to DISABLE)
             *
             * @return [PABuilder]
             * */
            fun enableBackgroundLogoutTimer(secondsLessThan30: Int): PABuilder {
                if (secondsLessThan30 < 30) {
                    backgroundLogoutTimerInitValue = secondsLessThan30 * 1000L
                }
                return this
            }

            /**
             * By default, wrong pin lockout is DISABLED.
             *
             * Enabling this will inhibit the user from confirming their pin after the declared
             * value of [maxPinAttempts] has been met.
             *
             * They must wait for the declared value of [lockoutDurationSeconds] until they can
             * try again.
             *
             * See [Builder] for sample code.
             *
             * @see [PAWrongPinLockout]
             *
             * @param [lockoutDurationSeconds] Int
             * @param [maxPinAttempts] Int
             *
             * @return [PABuilder]
             * */
            fun enableWrongPinLockout(lockoutDurationSeconds: Int, maxPinAttempts: Int): PABuilder {
                var lockoutDurationInitValue = 10
                if (lockoutDurationSeconds > 0) {
                    lockoutDurationInitValue = lockoutDurationSeconds
                }
                PAWrongPinLockout.setWrongPinLockoutValues(lockoutDurationInitValue, maxPinAttempts)
                enableWrongPinLockout = true
                return this
            }

            /**
             * By default, haptic feedback is DISABLED.
             *
             * Declaring this in your Builder will enable it. This can be changed later by the
             * user if you include in your settings UI the ability to enable it via accessing
             * [Settings.enableHapticFeedback] method. The user's setting for this will always
             * be loaded after this method.
             *
             * See [Builder] for sample code.
             *
             * @return [PABuilder]
             * */
            fun enableHapticFeedbackByDefault(): PABuilder {
                hapticFeedbackIsEnabledInitValue = true
                return this
            }

            /**
             * By default, pin security is DISABLED.
             *
             * Declaring this in your Builder will enable it. This can be changed later by the
             * user if you include in your settings UI the ability to enable it via accessing
             * [Settings.enablePinSecurity] method. The user's setting for this will always
             * be loaded after this method.
             *
             * See [Builder] for sample code.
             *
             * @return [PABuilder]
             * */
            fun enablePinSecurityByDefault(): PABuilder {
                injected.paPinSecurity.setPinSecurityValue(true)
                return this
            }

            /**
             * By default, scrambled pin is DISABLED.
             *
             * Declaring this in your Builder will enable it. This can be changed later by the
             * user if you include in your settings UI the ability to enable it via accessing
             * [Settings.enableScrambledPin] method. The user's setting for this will always
             * be loaded after this method.
             *
             * See [Builder] for sample code.
             *
             * @return [PABuilder]
             * */
            fun enableScrambledPinByDefault(): PABuilder {
                scrambledPinIsEnabledInitValue = true
                return this
            }

            /**
             * By default, minimum pin length is set to 4.
             *
             * Set a minimum pin length, between 4 to 14.
             *
             * This minimum value must be met when the user is entering their PIN before the
             * confirm button for [PinAuthenticationActivity] becomes visible.
             *
             * See [Builder] for sample code.
             *
             * @see [id.button_pin_authentication_confirm]
             * @see [BindingAdapters.paInvisibleUnless]
             *
             * @param [intFrom4To14] Int
             *
             * @return [PABuilder]
             * */
            fun setMinimumPinLength(intFrom4To14: Int): PABuilder {
                if (intFrom4To14 in 4..14) {
                    minPinLengthInitValue = intFrom4To14
                }
                return this
            }

            /**
             * Calling this method will return another Builder for customizing the colors
             * of [PinAuthenticationActivity].
             *
             * When done customizing the colors, call [PAColorsBuilder.applyColors] and it
             * will return you to the previous [PABuilder] to continue initialization.
             *
             * If this method is being called it will ensure that the Application colors
             * stay set so that, if at a later time colors are changed via
             * [Settings.setCustomColors] method, reversion back to original colors defined
             * during the Application's onCreate() process will be had and not
             * [PinAuthentication]'s default colors.
             *
             * See [Builder] for sample code.
             *
             * @return [PAColorsBuilder]
             * */
            fun setCustomColors(): PAColorsBuilder {
                return PAColorsBuilder(this)
            }

            /**
             * Initializes [PinAuthentication]
             *
             * See [Builder] for code sample.
             * */
            fun build() {
                injected.paPinSecurity.initializePinSecurity()

                injected.paSettings.initializeSettings(
                    buildConfigDebug,
                    appHasOnBoardProcessInitValue,
                    hapticFeedbackIsEnabledInitValue,
                    minPinLengthInitValue,
                    scrambledPinIsEnabledInitValue
                )

                injected.paViewColors.initializeViewColors()

                injected.paAppLockObserver.initializeAppLockObserver(
                    backgroundLogoutTimerInitValue
                )

                PAWrongPinLockout.initializeWrongPinLockout(enableWrongPinLockout)
            }

        }

        /**
         * Customize [PinAuthenticationActivity]'s colors.
         *
         * This Builder is used in 2 ways. Via:
         *
         *   - [PABuilder.setCustomColors] method being called which "unlocks" these options
         *   while initializing [PinAuthentication.Builder].
         *
         *   - [Settings.setCustomColors] method for modifying colors after
         *   [PinAuthentication] has been initialized.
         *
         * Call [applyColors] when done.
         *
         * @param [paBuilder] AMBuilder?
         *
         * @sample [io.matthewnelson.pin_authentication_demo.App.initializePinAuthentication]
         * @sample [io.matthewnelson.pin_authentication_demo.ui.SettingsFragment.initializeSetCustomColorsButton]
         * */
        class PAColorsBuilder(private val paBuilder: PABuilder?) {

            /**
             * Set the color for [id.button_pin_authentication_backspace]'s image.
             *
             * See [PAColorsBuilder] for sample code.
             *
             * @param [colorRes]
             *
             * @return [PAColorsBuilder]
             * */
            fun set1_BackspaceButtonImageColor(@ColorRes colorRes: Int): PAColorsBuilder {
                injected.paViewColors.setBackspaceButtonImageColor(colorRes, paBuilder != null)

                return this
            }

            /**
             * Set the color for [id.button_pin_authentication_confirm]'s background.
             *
             * See [PAColorsBuilder] for sample code.
             *
             * @param [colorRes]
             *
             * @return [PAColorsBuilder]
             * */
            fun set2_ConfirmButtonBackgroundColor(@ColorRes colorRes: Int): PAColorsBuilder {
                injected.paViewColors.setConfirmButtonBackgroundColor(colorRes, paBuilder != null)

                return this
            }

            /**
             * Set the color for [id.button_pin_authentication_confirm]'s image.
             *
             * See [PAColorsBuilder] for sample code.
             *
             * @param [colorRes]
             *
             * @return [PAColorsBuilder]
             * */
            fun set3_ConfirmButtonImageColor(@ColorRes colorRes: Int): PAColorsBuilder {
                injected.paViewColors.setConfirmButtonImageColor(colorRes, paBuilder != null)

                return this
            }

            /**
             * Set the color for [id.layout_linear_pin_authentication_pin_hint]'s background.
             *
             * See [PAColorsBuilder] for sample code.
             *
             * @param [colorRes]
             *
             * @return [PAColorsBuilder]
             * */
            fun set4_PinHintContainerColor(@ColorRes colorRes: Int): PAColorsBuilder {
                injected.paViewColors.setPinHintContainerColor(colorRes, paBuilder != null)

                return this
            }

            /**
             * Set the color for [id.image_view_pin_authentication_dot1] through
             * [id.image_view_pin_authentication_dot14]'s image
             *
             * See [PAColorsBuilder] for sample code.
             *
             * @param [colorRes]
             *
             * @return [PAColorsBuilder]
             * */
            fun set5_PinHintImageColor(@ColorRes colorRes: Int): PAColorsBuilder {
                injected.paViewColors.setPinHintImageColor(colorRes, paBuilder != null)

                return this
            }

            /**
             * Set the background color for all buttons (except [id.button_pin_authentication_confirm])
             *
             * See [PAColorsBuilder] for sample code.
             *
             * @param [colorRes]
             *
             * @return [PAColorsBuilder]
             * */
            fun set6_PinPadButtonBackgroundColor(@ColorRes colorRes: Int): PAColorsBuilder {
                injected.paViewColors.setPinPadButtonBackgroundColor(colorRes, paBuilder != null)

                return this
            }

            /**
             * Set the color for [id.image_view_pin_authentication_reset_help]
             *
             * See [PAColorsBuilder] for sample code.
             *
             * @param [colorRes]
             *
             * @return [PAColorsBuilder]
             * */
            fun set7_PinResetInfoImageColor(@ColorRes colorRes: Int): PAColorsBuilder {
                injected.paViewColors.setPinResetInfoImageColor(colorRes, paBuilder != null)

                return this
            }

            /**
             * Set the color for [id.layout_constraint_pin_authentication_container]
             *
             * See [PAColorsBuilder] for sample code.
             *
             * @param [colorRes]
             *
             * @return [PAColorsBuilder]
             * */
            fun set8_ScreenBackgroundColor(@ColorRes colorRes: Int): PAColorsBuilder {
                injected.paViewColors.setScreenBackgroundColor(colorRes, paBuilder != null)

                return this
            }

            /**
             * Set the color for all text
             *
             * See [PAColorsBuilder] for sample code.
             *
             * @param [colorRes]
             *
             * @return [PAColorsBuilder]
             * */
            fun set9_TextColor(@ColorRes colorRes: Int): PAColorsBuilder {
                injected.paViewColors.setTextColor(colorRes, paBuilder != null)

                return this
            }

            /**
             * - If being used when initializing [PinAuthentication.Builder],
             * will return [PABuilder]
             *
             * - If being used from [PinAuthentication.Settings.setCustomColors],
             * will return **`null`**.
             *
             * See [PAColorsBuilder] for sample code.
             *
             * @return [paBuilder]
             * */
            fun applyColors(): PABuilder? {
                injected.paViewColors.applyColors(paBuilder != null)
                return paBuilder
            }
        }
    }


    /**
     * PRIVATE variables needed by [PinAuthentication]
     * */
    private companion object {
        lateinit var paApplicationComponent: PAApplicationComponent
        lateinit var injected: PAInjection
    }


    /**
     * @suppress
     * PUBLIC methods needed for [PinAuthenticationActivity] to access dependency injection.
     * */
    @NotForPublicConsumption
    class PinAuthenticationActivityInjection(
        private val pinAuthenticationActivity: PinAuthenticationActivity
    ) {

        private val paActivityComponent by lazy {
            paApplicationComponent
                .getPAActivityComponentBuilder()
                .bindPinAuthenticationActivity(pinAuthenticationActivity)
                .build()
        }

        fun inject() {
            paActivityComponent.inject(pinAuthenticationActivity)
        }

    }


    /**
     * PUBLIC methods to be utilized by the Application that's implementing
     * [PinAuthentication] to access core features.
     * */
    class Controller {

        /**
         * Clears data associated with [PinAuthentication] from
         * SharedPreferences, and EncryptedSharedPreferences.
         *
         * Would suggest restarting the application afterwards
         * to avoid unintended operation.
         * */
        fun clearPinAuthenticationData() {
            injected.prefs.clear()
            PAPrefsKeys.getBlacklistedKeys().forEach {
                injected.encryptedPrefs.remove(it)
            }
        }

        //////////////////////////////////
        // Application On-board Process //
        //////////////////////////////////
        /**
         * Declares the on-board process as being complete,
         * and if Pin Security is:
         *
         *   - ENABLED: Immediately prompts the user to set
         *            their PIN before continuing.
         *
         *   **OR**
         *
         *   - DISABLED: Sets [PAInitialAppLogin.initialApplicationLoginSatisfied]
         *             to true.
         *
         * Declaring the on-board process as completed is saved
         * to SharedPreferences such that henceforth,
         * [PinAuthentication] will immediately request a
         * PIN to login at application start (if pin security
         * is enabled).
         *
         * @see [PAAppLockObserver.hijackApp]
         *
         * @sample [io.matthewnelson.pin_authentication_demo.ui.OnBoardFragment.initializeOnBoardFinishButton]
         * */
        fun completeOnBoardProcess() {
            injected.paSettings.setOnBoardProcessIsComplete()
            if (injected.paPinSecurity.isPinSecurityEnabled()) {
                injected.paAppLockObserver.hijackApp(PAPinEntryState.LOGIN)
            } else {
                injected.paInitialAppLogin.initialAppLoginIsSatisfied()
            }
        }

        /**
         * Returns TRUE if:
         *
         *   - You declared that your application has an on-board process via
         *   [PinAuthentication.Builder.PABuilder.applicationHasOnBoardProcess]
         *   and the on-board process has been marked **complete**.
         *
         *   **OR**
         *
         *   - You did **not** declare that your application has an on-board process.
         *
         * Returns FALSE if:
         *
         *   - You declared that your application has an on-board process via
         *   [PinAuthentication.Builder.PABuilder.applicationHasOnBoardProcess],
         *   but the on-board process has **not** been marked complete.
         *
         * @return Boolean
         *
         * @sample [io.matthewnelson.pin_authentication_demo.MainActivity.doOnBoard]
         * */
        fun hasOnBoardProcessBeenSatisfied(): Boolean =
            injected.paSettings.hasAppOnBoardProcessBeenSatisfied()


        ////////////////////////////
        // Confirm Pin To Proceed //
        ////////////////////////////
        /**
         * See [registerPinConfirmationToProceedRequestKey] sample code.
         *
         * @return Boolean
         * */
        fun isPinSecurityEnabled(): Boolean =
            injected.paPinSecurity.isPinSecurityEnabled()

        /**
         * Register request keys.
         * */
        fun registerPinConfirmationToProceedRequestKey(
            @Suppress("UNUSED_PARAMETER") activity: Activity,
            requestKey: String
        ): LiveData<Boolean>? =
            registerRequestKey(requestKey, PAScreenType.ACTIVITY)

        /**
         * Adds a requestKey to [PAConfirmPinToProceed.mapRequestKeys]
         * and returns LiveData associated with it which will change
         * after calling [Controller.requestPinConfirmationToProceed]
         * depending on whether or not the User enters the correct pin.
         *
         * - TRUE -> Pin was confirmed
         * - FALSE -> Pin has not been confirmed
         *
         * If the [requestKey] is already registered, it will **not**
         * overwrite the current value.
         *
         * @param [activity] Activity **OR**
         * @param [fragment] Fragment
         * @param [requestKey] String
         *
         * @return LiveData<Boolean>?
         *
         * @sample [io.matthewnelson.pin_authentication_demo.ui.ControllerFragment.observePinConfirmationRequestKey1]
         * @sample [io.matthewnelson.pin_authentication_demo.ui.ControllerFragment.observePinConfirmationRequestKey2]
         * */
        fun registerPinConfirmationToProceedRequestKey(
            fragment: Fragment,
            requestKey: String
        ): LiveData<Boolean>? =
            registerRequestKey(
                requestKey,
                PAScreenType.FRAGMENT,
                fragment.javaClass.name,
                fragment.id
            )

        private fun registerRequestKey(
            requestKey: String,
            screenType: @PAScreenType.ScreenType Int,
            fragmentClassName: String? = null,
            @IdRes fragmentID: Int? = null
        ): LiveData<Boolean>? {
            if (injected.paConfirmPinToProceed.isRequestKeyBlacklisted(requestKey)) {
                return null
            }

            val pinSecurityState = injected.paPinSecurity.isPinSecurityEnabled()
            return injected.paConfirmPinToProceed.registerRequestKey(
                requestKey,
                !pinSecurityState,
                screenType,
                fragmentClassName,
                fragmentID
            )
        }

        /**
         * To be used after registering the [requestKey] via
         * [Controller.registerPinConfirmationToProceedRequestKey].
         *
         * This will launch [PinAuthenticationActivity] in
         * [PAPinEntryState.CONFIRM_PIN] configuration. If the
         * [requestKey] is not registered, it does nothing.
         *
         * @param [requestKey] String
         *
         * @sample [io.matthewnelson.pin_authentication_demo.ui.ControllerFragment.observePinConfirmationRequestKey1]
         * @sample [io.matthewnelson.pin_authentication_demo.ui.ControllerFragment.observePinConfirmationRequestKey2]
         * */
        fun requestPinConfirmationToProceed(requestKey: String) {
            if (injected.paConfirmPinToProceed.isRequestKeyBlacklisted(requestKey)) {
                return
            }

            if (!injected.paSettings.hasAppOnBoardProcessBeenSatisfied()) {
                injected.paConfirmPinToProceed.setRequestKeyValueTo(requestKey, true)
                return
            }

            if (injected.paConfirmPinToProceed.isRequestKeyRegistered(requestKey)) {
                injected.paConfirmPinToProceed.setCurrentRequestKey(requestKey)
                injected.paAppLockObserver.hijackApp(PAPinEntryState.CONFIRM_PIN)
            }
        }

        /**
         * Reset the boolean value associated with the [requestKey]
         * to false to proc the observer that is setup on the
         * [Controller.registerPinConfirmationToProceedRequestKey]
         * method.
         *
         * If the [requestKey] is not registered or if PinSecurity
         * is DISABLED, it does nothing.
         *
         * @param [requestKey] String
         *
         * @sample [io.matthewnelson.pin_authentication_demo.ui.ControllerFragment.observePinConfirmationRequestKey1]
         * @sample [io.matthewnelson.pin_authentication_demo.ui.ControllerFragment.observePinConfirmationRequestKey2]
         * */
        fun resetPinConfirmationToProceedRequestKey(requestKey: String) {
            if (injected.paConfirmPinToProceed.isRequestKeyBlacklisted(requestKey)) {
                return
            }

            if (injected.paPinSecurity.isPinSecurityEnabled()) {
                injected.paConfirmPinToProceed.setRequestKeyValueTo(requestKey, false)
            }
        }

        /**
         * Remove registered request key.
         * */
        fun unregisterPinConfirmationToProceedRequestKey(requestKey: String) =
            unregisterRequestKey(requestKey)

        /**
         * Removes the requestKey and LiveData values from the Map
         * contained in [PAConfirmPinToProceed.mapRequestKeys].
         *
         * To be implemented in an Activity's onDestroy() or a
         * Fragments onDestroyView(), **after** the super call.
         *
         * @see [PAConfirmPinToProceed.unregisterRequestKey]
         *
         * @param [requestKey] String **OR**
         * @param [requestKeys] Array<String>
         *
         * @sample [io.matthewnelson.pin_authentication_demo.ui.ControllerFragment.onDestroyView]
         * */
        fun unregisterPinConfirmationToProceedRequestKey(requestKeys: Array<String>) =
            requestKeys.forEach { key ->
                unregisterRequestKey(key)
            }

        private fun unregisterRequestKey(requestKey: String) {
            if (injected.paConfirmPinToProceed.isRequestKeyBlacklisted(requestKey)) {
                return
            }

            injected.paConfirmPinToProceed.unregisterRequestKey(requestKey)
        }


        ///////////////////////////////
        // Initial Application Login //
        ///////////////////////////////
        /**
         * Returns a boolean value that will change from
         * false to true, and stay true until the
         * application is terminated.
         *
         * This boolean value gets changed when the user
         * opens the application and:
         *
         *   - If pin security is ENABLED and they
         *   successfully log in.
         *
         *   **OR**
         *
         *   - If pin security was DISABLED and the
         *   application on-board process has been
         *   satisfied when this method is called for the
         *   first time.
         *
         * Primary use case for this is to hold application
         * processes until authentication has been had, for
         * example, on your landing Activity/Fragment.
         *
         * @return LiveData<Boolean>
         *
         * @sample [io.matthewnelson.pin_authentication_demo.MainActivity.observeInitialAppLogin]
         * */
        fun hasInitialAppLoginBeenSatisfied(): LiveData<Boolean> {
            if (!injected.paPinSecurity.isPinSecurityEnabled() &&
                injected.paSettings.hasAppOnBoardProcessBeenSatisfied()
            ) {
                injected.paInitialAppLogin.initialAppLoginIsSatisfied()
            }

            return injected.paInitialAppLogin.hasInitialAppLoginBeenSatisfied()
        }

        /**
         * Check if your startup process has previously
         * been started.
         *
         * This is needed so that when observing the
         * returned LiveData from
         * [Controller.hasInitialAppLoginBeenSatisfied],
         * so your startup processes only get executed once.
         *
         * @see PAInitialAppLogin.hasPostLoginProcessBeenStarted
         *
         * @return Boolean
         *
         * @sample [io.matthewnelson.pin_authentication_demo.MainActivity.executePostLoginProcesses]
         * */
        fun hasPostLoginProcessBeenStarted(): Boolean =
            injected.paInitialAppLogin.hasPostLoginProcessBeenStarted()

        /**
         * After [Controller.hasPostLoginProcessBeenStarted]
         * returns false and your one-time processes start,
         * use this method to set the value to true so that
         * your post login processes won't be executed again
         * if the observer on
         * [Controller.hasInitialAppLoginBeenSatisfied] gets
         * proc'd again.
         *
         * @sample [io.matthewnelson.pin_authentication_demo.MainActivity.executePostLoginProcesses]
         * */
        fun postLoginProcessStarted() =
            injected.paInitialAppLogin.postLoginProcessStarted()

    }


    /**
     * PUBLIC methods to be utilized by the Application that's
     * implementing [PinAuthentication] for allowing Users
     * the ability to modify various settings.
     * */
    class Settings {

        ////////////
        // Colors //
        ////////////
        /**
         * Will clear currently applied colors and set them back
         * to colors defined in the Application onCreate()'s
         * initialization of
         * [PinAuthentication.Builder.PABuilder.setCustomColors],
         * if they were specified. Otherwise it will fall back to
         * [PinAuthentication]'s default colors.
         *
         * @see [PAViewColors.resetColorsToApplicationDefaults]
         *
         * @sample [io.matthewnelson.pin_authentication_demo.ui.SettingsFragment.initializeResetColorsButton]
         * */
        fun resetColorsToApplicationDefaults() =
            injected.paViewColors.resetColorsToApplicationDefaults()

        /**
         * Change the colors of [PinAuthenticationActivity]
         * on the fly. These settings get saved to
         * [PinAuthentication]'s SharedPreferences and loaded
         * at startup **after** custom colors that may have been
         * specified in the Application onCreate()'s
         * [PinAuthentication.Builder.PABuilder.setCustomColors].
         *
         * @return [Builder.PAColorsBuilder]
         *
         * @sample [io.matthewnelson.pin_authentication_demo.ui.SettingsFragment.initializeSetCustomColorsButton]
         * */
        fun setCustomColors(): Builder.PAColorsBuilder =
            Builder.PAColorsBuilder(null)


        /////////////////////
        // Haptic Feedback //
        /////////////////////
        /**
         * ENABLE/DISABLE haptic feedback on interactions with
         * [PinAuthenticationActivity].
         *
         * @param [enable] Boolean (`true` = ENABLE, `false` = DISABLE)
         *
         * @sample [io.matthewnelson.pin_authentication_demo.ui.SettingsFragment.initializeHapticFeedbackSwitch]
         * */
        fun enableHapticFeedback(enable: Boolean) =
            injected.paSettings.setHapticFeedbackIsEnabled(enable)

        /**
         * Checks if haptic feedback is enabled.
         *
         * @return Boolean
         *
         * @sample [io.matthewnelson.pin_authentication_demo.ui.SettingsFragment.setHapticFeedbackSwitchPosition]
         * */
        fun isHapticFeedbackEnabled(): Boolean =
            injected.paSettings.getHapticFeedbackIsEnabled()


        ///////////////
        // PIN Reset //
        ///////////////
        /**
         * Launches [PinAuthenticationActivity] in
         * [PAPinEntryState.RESET_PIN] configuration for
         * the user to reset their PIN.
         *
         * @see [PAAppLockObserver.hijackApp]
         *
         * @sample [io.matthewnelson.pin_authentication_demo.ui.SettingsFragment.initializeResetPinButton]
         * */
        fun resetPin() =
            injected.paAppLockObserver.hijackApp(PAPinEntryState.RESET_PIN)


        //////////////////
        // Pin Security //
        //////////////////
        /**
         * Starts the process for ENABLING/DISABLING pin security.
         *
         * Going from:
         *
         *   - DISABLED -> ENABLED:
         *       - Will prompt the user to set a pin.
         *   - ENABLED -> DISABLED:
         *       - Will prompt the user to confirm their pin.
         *
         * @see [PAPinSecurity]
         *
         * @param [enable] Boolean (`true` = ENABLE, `false` = DISABLE)
         *
         * @sample [io.matthewnelson.pin_authentication_demo.ui.SettingsFragment.initializePinSecuritySwitch]
         * */
        fun enablePinSecurity(enable: Boolean) {
            val pinSecurityState = injected.paPinSecurity.isPinSecurityEnabled()

            // Set the value again to proc the out-of-sync observer
            if (enable == pinSecurityState) {
                injected.paPinSecurity.setPinSecurityValue(pinSecurityState)
                return
            }

            if (!injected.paSettings.hasAppOnBoardProcessBeenSatisfied()) {
                if (enable) {
                    injected.paPinSecurity.enablePinSecuritySuccess()
                } else {
                    injected.paPinSecurity.disablePinSecuritySuccess()
                }
                return
            }

            injected.paPinSecurity.setCurrentRequestKeyToPinSecurity()

            if (enable) {
                injected.paAppLockObserver.hijackApp(PAPinEntryState.ENABLE_PIN_SECURITY)
            } else {
                injected.paAppLockObserver.hijackApp(PAPinEntryState.CONFIRM_PIN)
            }
        }

        /**
         * Checks if PIN security is enabled.
         *
         * @return LiveData<Boolean>?
         *
         * @sample [io.matthewnelson.pin_authentication_demo.ui.SettingsFragment.observePinSecurity]
         * */
        fun isPinSecurityEnabled(): LiveData<Boolean>? =
            injected.paPinSecurity.getPinSecurity()


        ///////////////////
        // Scrambled Pin //
        ///////////////////
        /**
         * ENABLE/DISABLE scrambled pin for [PinAuthenticationActivity]
         *
         * @param [enable] Boolean (`true` = ENABLE, `false` = DISABLE)
         *
         * @sample [io.matthewnelson.pin_authentication_demo.ui.SettingsFragment.initializeScrambledPinSwitch]
         * */
        fun enableScrambledPin(enable: Boolean) =
            injected.paSettings.setScrambledPinIsEnabled(enable)

        /**
         * Checks if scrambled pin is enabled.
         *
         * @return Boolean
         *
         * @sample [io.matthewnelson.pin_authentication_demo.ui.SettingsFragment.setScrambledPinSwitchPosition]
         * */
        fun isScrambledPinEnabled(): Boolean =
            injected.paSettings.getScrambledPinIsEnabled()

    }

}