package io.matthewnelson.pin_authentication.service

import android.app.Activity
import android.app.Application
import androidx.annotation.ColorRes
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import io.matthewnelson.pin_authentication.R.id
import io.matthewnelson.pin_authentication.di.CompanionInjection
import io.matthewnelson.pin_authentication.di.application.ApplicationComponent
import io.matthewnelson.pin_authentication.service.components.*
import io.matthewnelson.pin_authentication.ui.PinAuthenticationActivity
import io.matthewnelson.pin_authentication.util.BindingAdapters
import io.matthewnelson.pin_authentication.util.definitions.PAPinEntryState
import io.matthewnelson.pin_authentication.util.definitions.ScreenTypes.ScreenType
import io.matthewnelson.pin_authentication.di.application.DaggerApplicationComponent
import io.matthewnelson.pin_authentication.util.definitions.ScreenTypes

sealed class PinAuthentication {

    /**
     * Call from the Application's onCreate() to implement
     *
     * @sample [io.matthewnelson.pin_authentication_demo.App.initializePinAuthentication]
     * */
    class Builder {

        /**
         * Sets the Application which is used throughout [PinAuthentication]. It initializes
         * [PinAuthentication]'s [DaggerApplicationComponent] which then is used to inject
         * classes as needed.
         * See [CompanionInjection]
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
         * @return [OptionsBuilder]
         * */
        fun setApplicationAndBuildConfig(
            application: Application,
            buildConfigDebug: Boolean
        ): OptionsBuilder {
            applicationComponent =
                DaggerApplicationComponent
                    .builder()
                    .bindApplication(application)
                    .build()

            injected = CompanionInjection(applicationComponent)
            return OptionsBuilder(buildConfigDebug)
        }

        internal fun testing(
            appComponent: ApplicationComponent,
            companionInjection: CompanionInjection,
            buildConfigDebug: Boolean
        ): OptionsBuilder {
            applicationComponent = appComponent
            injected = companionInjection
            return OptionsBuilder(buildConfigDebug)
        }

        /**
         * Meant to only be used after calling [Builder.setApplicationAndBuildConfig], which
         * returns this class.
         *
         * See [Builder] for sample code.
         *
         * @param [buildConfigDebug] Boolean
         * */
        class OptionsBuilder(private val buildConfigDebug: Boolean) {
            private var appHasOnBoardProcessInitValue = false
            private var backgroundLogoutTimerInitValue = 0L
            private var hapticFeedbackIsEnabledInitValue = false
            private var minPinLengthInitValue = 4
            private var scrambledPinIsEnabledInitValue = false

            private var enableWrongPinLockout = false

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
             * @return [OptionsBuilder]
             * */
            fun applicationHasOnBoardProcess(): OptionsBuilder {
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
             * @see [AppLockObserver.launchAuthInvalidationJobIfInactive]
             *
             * @param [secondsLessThan30] Int - (Set to 0 to DISABLE)
             *
             * @return [OptionsBuilder]
             * */
            fun enableBackgroundLogoutTimer(secondsLessThan30: Int): OptionsBuilder {
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
             * @see [WrongPinLockout]
             *
             * @param [lockoutDurationSeconds] Int
             * @param [maxPinAttempts] Int
             *
             * @return [OptionsBuilder]
             * */
            fun enableWrongPinLockout(lockoutDurationSeconds: Int, maxPinAttempts: Int): OptionsBuilder {
                var lockoutDurationInitValue = 10
                if (lockoutDurationSeconds > 0) {
                    lockoutDurationInitValue = lockoutDurationSeconds
                }
                WrongPinLockout.setWrongPinLockoutValues(lockoutDurationInitValue, maxPinAttempts)
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
             * @return [OptionsBuilder]
             * */
            fun enableHapticFeedbackByDefault(): OptionsBuilder {
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
             * @return [OptionsBuilder]
             * */
            fun enablePinSecurityByDefault(): OptionsBuilder {
                injected.pinSecurity.setPinSecurityValue(true)
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
             * @return [OptionsBuilder]
             * */
            fun enableScrambledPinByDefault(): OptionsBuilder {
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
             * @return [OptionsBuilder]
             * */
            fun setMinimumPinLength(intFrom4To14: Int): OptionsBuilder {
                if (intFrom4To14 in 4..14) {
                    minPinLengthInitValue = intFrom4To14
                }
                return this
            }

            /**
             * Calling this method will return another Builder for customizing the colors
             * of [PinAuthenticationActivity].
             *
             * When done customizing the colors, call [ColorsBuilder.applyColors] and it
             * will return you to the previous [OptionsBuilder] to continue initialization.
             *
             * If this method is being called it will ensure that the Application colors
             * stay set so that, if at a later time colors are changed via
             * [Settings.setCustomColors] method, reversion back to original colors defined
             * during the Application's onCreate() process will be had and not
             * [PinAuthentication]'s default colors.
             *
             * See [Builder] for sample code.
             *
             * @return [ColorsBuilder]
             * */
            fun setCustomColors(): ColorsBuilder {
                return ColorsBuilder(this)
            }

            /**
             * Initializes [PinAuthentication]
             *
             * See [Builder] for code sample.
             * */
            fun build() {
                injected.pinSecurity.initializePinSecurity()

                injected.settings.initializeSettings(
                    buildConfigDebug,
                    appHasOnBoardProcessInitValue,
                    hapticFeedbackIsEnabledInitValue,
                    minPinLengthInitValue,
                    scrambledPinIsEnabledInitValue
                )

                injected.viewColors.initializeViewColors()

                injected.appLockObserver.initializeAppLockObserver(
                    backgroundLogoutTimerInitValue
                )

                WrongPinLockout.initializeWrongPinLockout(enableWrongPinLockout)
            }

        }

        /**
         * Customize [PinAuthenticationActivity]'s colors.
         *
         * This Builder is used in 2 ways. Via:
         *
         *   - [OptionsBuilder.setCustomColors] method being called which "unlocks" these options
         *   while initializing [PinAuthentication.Builder].
         *
         *   - [Settings.setCustomColors] method for modifying colors after
         *   [PinAuthentication] has been initialized.
         *
         * Call [applyColors] when done.
         *
         * @param [optionsBuilder] [OptionsBuilder]?
         *
         * @sample [io.matthewnelson.pin_authentication_demo.App.initializePinAuthentication]
         * @sample [io.matthewnelson.pin_authentication_demo.ui.SettingsFragment.initializeSetCustomColorsButton]
         * */
        class ColorsBuilder(private val optionsBuilder: OptionsBuilder?) {

            /**
             * Set the color for [id.button_pin_authentication_backspace]'s image.
             *
             * See [ColorsBuilder] for sample code.
             *
             * @param [colorRes]
             *
             * @return [ColorsBuilder]
             * */
            fun set1_BackspaceButtonImageColor(@ColorRes colorRes: Int): ColorsBuilder {
                injected.viewColors.setBackspaceButtonImageColor(colorRes, optionsBuilder != null)

                return this
            }

            /**
             * Set the color for [id.button_pin_authentication_confirm]'s background.
             *
             * See [ColorsBuilder] for sample code.
             *
             * @param [colorRes]
             *
             * @return [ColorsBuilder]
             * */
            fun set2_ConfirmButtonBackgroundColor(@ColorRes colorRes: Int): ColorsBuilder {
                injected.viewColors.setConfirmButtonBackgroundColor(colorRes, optionsBuilder != null)

                return this
            }

            /**
             * Set the color for [id.button_pin_authentication_confirm]'s image.
             *
             * See [ColorsBuilder] for sample code.
             *
             * @param [colorRes]
             *
             * @return [ColorsBuilder]
             * */
            fun set3_ConfirmButtonImageColor(@ColorRes colorRes: Int): ColorsBuilder {
                injected.viewColors.setConfirmButtonImageColor(colorRes, optionsBuilder != null)

                return this
            }

            /**
             * Set the color for [id.layout_linear_pin_authentication_pin_hint]'s background.
             *
             * See [ColorsBuilder] for sample code.
             *
             * @param [colorRes]
             *
             * @return [ColorsBuilder]
             * */
            fun set4_PinHintContainerColor(@ColorRes colorRes: Int): ColorsBuilder {
                injected.viewColors.setPinHintContainerColor(colorRes, optionsBuilder != null)

                return this
            }

            /**
             * Set the color for [id.image_view_pin_authentication_dot1] through
             * [id.image_view_pin_authentication_dot14]'s image
             *
             * See [ColorsBuilder] for sample code.
             *
             * @param [colorRes]
             *
             * @return [ColorsBuilder]
             * */
            fun set5_PinHintImageColor(@ColorRes colorRes: Int): ColorsBuilder {
                injected.viewColors.setPinHintImageColor(colorRes, optionsBuilder != null)

                return this
            }

            /**
             * Set the background color for all buttons (except [id.button_pin_authentication_confirm])
             *
             * See [ColorsBuilder] for sample code.
             *
             * @param [colorRes]
             *
             * @return [ColorsBuilder]
             * */
            fun set6_PinPadButtonBackgroundColor(@ColorRes colorRes: Int): ColorsBuilder {
                injected.viewColors.setPinPadButtonBackgroundColor(colorRes, optionsBuilder != null)

                return this
            }

            /**
             * Set the color for [id.image_view_pin_authentication_reset_help]
             *
             * See [ColorsBuilder] for sample code.
             *
             * @param [colorRes]
             *
             * @return [ColorsBuilder]
             * */
            fun set7_PinResetInfoImageColor(@ColorRes colorRes: Int): ColorsBuilder {
                injected.viewColors.setPinResetInfoImageColor(colorRes, optionsBuilder != null)

                return this
            }

            /**
             * Set the color for [id.layout_constraint_pin_authentication_container]
             *
             * See [ColorsBuilder] for sample code.
             *
             * @param [colorRes]
             *
             * @return [ColorsBuilder]
             * */
            fun set8_ScreenBackgroundColor(@ColorRes colorRes: Int): ColorsBuilder {
                injected.viewColors.setScreenBackgroundColor(colorRes, optionsBuilder != null)

                return this
            }

            /**
             * Set the color for all text
             *
             * See [ColorsBuilder] for sample code.
             *
             * @param [colorRes]
             *
             * @return [ColorsBuilder]
             * */
            fun set9_TextColor(@ColorRes colorRes: Int): ColorsBuilder {
                injected.viewColors.setTextColor(colorRes, optionsBuilder != null)

                return this
            }

            /**
             * - If being used when initializing [PinAuthentication.Builder],
             * will return [OptionsBuilder]
             *
             * - If being used from [PinAuthentication.Settings.setCustomColors],
             * will return **`null`**.
             *
             * See [ColorsBuilder] for sample code.
             *
             * @return [optionsBuilder]
             * */
            fun applyColors(): OptionsBuilder? {
                injected.viewColors.applyColors(optionsBuilder != null)
                return optionsBuilder
            }
        }
    }


    /**
     * PRIVATE variables needed by [PinAuthentication]
     * */
    private companion object {
        lateinit var applicationComponent: ApplicationComponent
        lateinit var injected: CompanionInjection
    }


    /**
     * @suppress
     * Methods needed for [PinAuthenticationActivity] to access dependency injection.
     * */
    internal class PinAuthenticationActivityInjection(
        private val pinAuthenticationActivity: PinAuthenticationActivity
    ) {

        private val activityComponent by lazy {
            applicationComponent
                .getPAActivityComponentBuilder()
                .bindPinAuthenticationActivity(pinAuthenticationActivity)
                .build()
        }

        fun inject() {
            activityComponent.inject(pinAuthenticationActivity)
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
            injected.encryptedPrefs.clear()
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
         *   - DISABLED: Sets [InitialAppLogin.initialApplicationLoginSatisfied]
         *             to true.
         *
         * Declaring the on-board process as completed is saved
         * to SharedPreferences such that henceforth,
         * [PinAuthentication] will immediately request a
         * PIN to login at application start (if pin security
         * is enabled).
         *
         * @see [AppLockObserver.hijackApp]
         *
         * @sample [io.matthewnelson.pin_authentication_demo.ui.OnBoardFragment.initializeOnBoardFinishButton]
         * */
        fun completeOnBoardProcess() {
            injected.settings.setOnBoardProcessIsComplete()
            if (injected.pinSecurity.isPinSecurityEnabled()) {
                injected.appLockObserver.hijackApp(PAPinEntryState.LOGIN)
            } else {
                injected.initialAppLogin.initialAppLoginIsSatisfied()
            }
        }

        /**
         * Returns TRUE if:
         *
         *   - You declared that your application has an on-board process via
         *   [PinAuthentication.Builder.OptionsBuilder.applicationHasOnBoardProcess]
         *   and the on-board process has been marked **complete**.
         *
         *   **OR**
         *
         *   - You did **not** declare that your application has an on-board process.
         *
         * Returns FALSE if:
         *
         *   - You declared that your application has an on-board process via
         *   [PinAuthentication.Builder.OptionsBuilder.applicationHasOnBoardProcess],
         *   but the on-board process has **not** been marked complete.
         *
         * @return Boolean
         *
         * @sample [io.matthewnelson.pin_authentication_demo.MainActivity.doOnBoard]
         * */
        fun hasOnBoardProcessBeenSatisfied(): Boolean =
            injected.settings.hasAppOnBoardProcessBeenSatisfied()


        ////////////////////////////
        // Confirm Pin To Proceed //
        ////////////////////////////
        /**
         * See [registerPinConfirmationToProceedRequestKey] sample code.
         *
         * @return Boolean
         * */
        fun isPinSecurityEnabled(): Boolean =
            injected.pinSecurity.isPinSecurityEnabled()

        /**
         * Register request keys.
         * */
        fun registerPinConfirmationToProceedRequestKey(
            @Suppress("UNUSED_PARAMETER") activity: Activity,
            requestKey: String
        ): LiveData<Boolean>? =
            registerRequestKey(requestKey, ScreenType.ACTIVITY)

        /**
         * Adds a requestKey to [ConfirmPinToProceed.mapRequestKeys]
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
                ScreenType.FRAGMENT,
                fragment.javaClass.name,
                fragment.id
            )

        private fun registerRequestKey(
            requestKey: String,
            screenType: @ScreenTypes.ScreenType Int,
            fragmentClassName: String? = null,
            @IdRes fragmentID: Int? = null
        ): LiveData<Boolean>? {
            if (injected.confirmPinToProceed.isRequestKeyBlacklisted(requestKey)) {
                return null
            }

            val pinSecurityState = injected.pinSecurity.isPinSecurityEnabled()
            return injected.confirmPinToProceed.registerRequestKey(
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
            if (injected.confirmPinToProceed.isRequestKeyBlacklisted(requestKey)) {
                return
            }

            if (!injected.settings.hasAppOnBoardProcessBeenSatisfied()) {
                injected.confirmPinToProceed.setRequestKeyValueTo(requestKey, true)
                return
            }

            if (injected.confirmPinToProceed.isRequestKeyRegistered(requestKey)) {
                injected.confirmPinToProceed.setCurrentRequestKey(requestKey)
                injected.appLockObserver.hijackApp(PAPinEntryState.CONFIRM_PIN)
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
            if (injected.confirmPinToProceed.isRequestKeyBlacklisted(requestKey)) {
                return
            }

            if (injected.pinSecurity.isPinSecurityEnabled()) {
                injected.confirmPinToProceed.setRequestKeyValueTo(requestKey, false)
            }
        }

        /**
         * Remove registered request key.
         * */
        fun unregisterPinConfirmationToProceedRequestKey(requestKey: String) =
            unregisterRequestKey(requestKey)

        /**
         * Removes the requestKey and LiveData values from the Map
         * contained in [ConfirmPinToProceed.mapRequestKeys].
         *
         * To be implemented in an Activity's onDestroy() or a
         * Fragments onDestroyView(), **after** the super call.
         *
         * @see [ConfirmPinToProceed.unregisterRequestKey]
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
            if (injected.confirmPinToProceed.isRequestKeyBlacklisted(requestKey)) {
                return
            }

            injected.confirmPinToProceed.unregisterRequestKey(requestKey)
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
            if (!injected.pinSecurity.isPinSecurityEnabled() &&
                injected.settings.hasAppOnBoardProcessBeenSatisfied()
            ) {
                injected.initialAppLogin.initialAppLoginIsSatisfied()
            }

            return injected.initialAppLogin.hasInitialAppLoginBeenSatisfied()
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
         * @see InitialAppLogin.hasPostLoginProcessBeenStarted
         *
         * @return Boolean
         *
         * @sample [io.matthewnelson.pin_authentication_demo.MainActivity.executePostLoginProcesses]
         * */
        fun hasPostLoginProcessBeenStarted(): Boolean =
            injected.initialAppLogin.hasPostLoginProcessBeenStarted()

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
            injected.initialAppLogin.postLoginProcessStarted()

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
         * [PinAuthentication.Builder.OptionsBuilder.setCustomColors],
         * if they were specified. Otherwise it will fall back to
         * [PinAuthentication]'s default colors.
         *
         * @see [ViewColors.resetColorsToApplicationDefaults]
         *
         * @sample [io.matthewnelson.pin_authentication_demo.ui.SettingsFragment.initializeResetColorsButton]
         * */
        fun resetColorsToApplicationDefaults() =
            injected.viewColors.resetColorsToApplicationDefaults()

        /**
         * Change the colors of [PinAuthenticationActivity]
         * on the fly. These settings get saved to
         * [PinAuthentication]'s SharedPreferences and loaded
         * at startup **after** custom colors that may have been
         * specified in the Application onCreate()'s
         * [PinAuthentication.Builder.OptionsBuilder.setCustomColors].
         *
         * @return [Builder.ColorsBuilder]
         *
         * @sample [io.matthewnelson.pin_authentication_demo.ui.SettingsFragment.initializeSetCustomColorsButton]
         * */
        fun setCustomColors(): Builder.ColorsBuilder =
            Builder.ColorsBuilder(null)


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
            injected.settings.setHapticFeedbackIsEnabled(enable)

        /**
         * Checks if haptic feedback is enabled.
         *
         * @return Boolean
         *
         * @sample [io.matthewnelson.pin_authentication_demo.ui.SettingsFragment.setHapticFeedbackSwitchPosition]
         * */
        fun isHapticFeedbackEnabled(): Boolean =
            injected.settings.getHapticFeedbackIsEnabled()


        ///////////////
        // PIN Reset //
        ///////////////
        /**
         * Launches [PinAuthenticationActivity] in
         * [PAPinEntryState.RESET_PIN] configuration for
         * the user to reset their PIN.
         *
         * @see [AppLockObserver.hijackApp]
         *
         * @sample [io.matthewnelson.pin_authentication_demo.ui.SettingsFragment.initializeResetPinButton]
         * */
        fun resetPin() =
            injected.appLockObserver.hijackApp(PAPinEntryState.RESET_PIN)


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
         * @see [PinSecurity]
         *
         * @param [enable] Boolean (`true` = ENABLE, `false` = DISABLE)
         *
         * @sample [io.matthewnelson.pin_authentication_demo.ui.SettingsFragment.initializePinSecuritySwitch]
         * */
        fun enablePinSecurity(enable: Boolean) {
            val pinSecurityState = injected.pinSecurity.isPinSecurityEnabled()

            // Set the value again to proc the out-of-sync observer
            if (enable == pinSecurityState) {
                injected.pinSecurity.setPinSecurityValue(pinSecurityState)
                return
            }

            if (!injected.settings.hasAppOnBoardProcessBeenSatisfied()) {
                if (enable) {
                    injected.pinSecurity.enablePinSecuritySuccess()
                } else {
                    injected.pinSecurity.disablePinSecuritySuccess()
                }
                return
            }

            injected.pinSecurity.setCurrentRequestKeyToPinSecurity()

            if (enable) {
                injected.appLockObserver.hijackApp(PAPinEntryState.ENABLE_PIN_SECURITY)
            } else {
                injected.appLockObserver.hijackApp(PAPinEntryState.CONFIRM_PIN)
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
            injected.pinSecurity.getPinSecurity()


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
            injected.settings.setScrambledPinIsEnabled(enable)

        /**
         * Checks if scrambled pin is enabled.
         *
         * @return Boolean
         *
         * @sample [io.matthewnelson.pin_authentication_demo.ui.SettingsFragment.setScrambledPinSwitchPosition]
         * */
        fun isScrambledPinEnabled(): Boolean =
            injected.settings.getScrambledPinIsEnabled()

    }

}