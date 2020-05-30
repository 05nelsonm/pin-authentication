[pin-authentication](../../../index.md) / [io.matthewnelson.pin_authentication.service](../../index.md) / [PinAuthentication](../index.md) / [Builder](./index.md)

# Builder

`class Builder` [(source)](https://github.com/05nelsonm/pin-authentication/blob/master/pin-authentication/src/main/java/io/matthewnelson/pin_authentication/service/PinAuthentication.kt#L29)

Call from the Application's onCreate() to implement

``` kotlin
PinAuthentication.Builder()
    .setApplicationAndBuildConfig(this, BuildConfig.DEBUG)
    .applicationHasOnBoardProcess()
    .enableBackgroundLogoutTimer(4)
    .enableHapticFeedbackByDefault()
    .enablePinSecurityByDefault()
    .enableScrambledPinByDefault()
    .enableWrongPinLockout(10, 3)
    .setMinimumPinLength(4)


    /**
     * Set custom colors for [PinAuthentication]'s Activity which will overwrite
     * the default color value for that view.
     *
     * Colors can also be changed after initialization of
     * [PinAuthentication] by utilizing [PinAuthentication.Settings.setCustomColors]
     * method, and also reset back to the colors chosen here by utilizing the
     * [PinAuthentication.Settings.resetColorsToApplicationDefaults] method.
     *
     * @see [PinAuthentication.Builder.PABuilder.setCustomColors]
     * @see [PinAuthentication.Builder.PAColorsBuilder]
     * */
    .setCustomColors()
    .set2_ConfirmButtonBackgroundColor(R.color.secondaryLightColor)
    .set4_PinHintContainerColor(R.color.primaryDarkColor)
    .set6_PinPadButtonBackgroundColor(R.color.primaryDarkColor)
    .set8_ScreenBackgroundColor(R.color.primaryColor)

    /**
     * Calling applyColors from within the Builder will **NOT** return null. null is only
     * returned if calling [PinAuthentication.Settings.setCustomColors].
     *
     * @see [PinAuthentication.Builder.PAColorsBuilder.applyColors]
     * */
    .applyColors()!!

    .build()
```

### Types

| Name | Summary |
|---|---|
| [PABuilder](-p-a-builder/index.md) | Meant to only be used after calling [Builder.setApplicationAndBuildConfig](set-application-and-build-config.md), which returns this class.`class PABuilder` |
| [PAColorsBuilder](-p-a-colors-builder/index.md) | Customize [PinAuthenticationActivity](#)'s colors.`class PAColorsBuilder` |

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | Call from the Application's onCreate() to implement`Builder()` |

### Functions

| Name | Summary |
|---|---|
| [setApplicationAndBuildConfig](set-application-and-build-config.md) | Sets the Application which is used throughout [PinAuthentication](../index.md). It initializes [PinAuthentication](../index.md)'s [DaggerPAApplicationComponent](#) which then is used to inject classes as needed. See [PAInjection](#) See [Companion.injected](#)`fun setApplicationAndBuildConfig(application: `[`Application`](https://developer.android.com/reference/android/app/Application.html)`, buildConfigDebug: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`): PABuilder` |
