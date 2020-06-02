[pin-authentication](../../../../index.md) / [io.matthewnelson.pin_authentication.service](../../../index.md) / [PinAuthentication](../../index.md) / [Builder](../index.md) / [PAColorsBuilder](./index.md)

# PAColorsBuilder

`class PAColorsBuilder` [(source)](https://github.com/05nelsonm/pin-authentication/blob/master/pin-authentication/src/main/java/io/matthewnelson/pin_authentication/service/PinAuthentication.kt#L299)

Customize [PinAuthenticationActivity](#)'s colors.

This Builder is used in 2 ways. Via:

* [PABuilder.setCustomColors](../-p-a-builder/set-custom-colors.md) method being called which "unlocks" these options
while initializing [PinAuthentication.Builder](../index.md).

* [Settings.setCustomColors](../../-settings/set-custom-colors.md) method for modifying colors after
[PinAuthentication](../../index.md) has been initialized.

Call [applyColors](apply-colors.md) when done.

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

``` kotlin
binding.buttonSettingsSetCustomColors.setOnClickListener {
    paSettings.setCustomColors()
        .set2_ConfirmButtonBackgroundColor(R.color.pa_super_green)
        .set4_PinHintContainerColor(R.color.pa_sea_blue)
        .set6_PinPadButtonBackgroundColor(R.color.pa_sea_blue)
        .set8_ScreenBackgroundColor(R.color.pa_deep_sea_blue)
        .applyColors()
    App.showToast(getString(R.string.toast_set_custom_colors))
}
```

### Parameters

`paBuilder` - [PABuilder](../-p-a-builder/index.md)?

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | Customize [PinAuthenticationActivity](#)'s colors.`PAColorsBuilder(paBuilder: PABuilder?)` |

### Functions

| Name | Summary |
|---|---|
| [applyColors](apply-colors.md) | <ul><li>If being used when initializing [PinAuthentication.Builder](../index.md), will return [PABuilder](../-p-a-builder/index.md)</li> <li>If being used from [PinAuthentication.Settings.setCustomColors](../../-settings/set-custom-colors.md), will return **`null`**.</li></ul>`fun applyColors(): PABuilder?` |
| [set1_BackspaceButtonImageColor](set1_-backspace-button-image-color.md) | Set the color for [id.button_pin_authentication_backspace](#)'s image.`fun set1_BackspaceButtonImageColor(colorRes: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`): PAColorsBuilder` |
| [set2_ConfirmButtonBackgroundColor](set2_-confirm-button-background-color.md) | Set the color for [id.button_pin_authentication_confirm](#)'s background.`fun set2_ConfirmButtonBackgroundColor(colorRes: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`): PAColorsBuilder` |
| [set3_ConfirmButtonImageColor](set3_-confirm-button-image-color.md) | Set the color for [id.button_pin_authentication_confirm](#)'s image.`fun set3_ConfirmButtonImageColor(colorRes: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`): PAColorsBuilder` |
| [set4_PinHintContainerColor](set4_-pin-hint-container-color.md) | Set the color for [id.layout_linear_pin_authentication_pin_hint](#)'s background.`fun set4_PinHintContainerColor(colorRes: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`): PAColorsBuilder` |
| [set5_PinHintImageColor](set5_-pin-hint-image-color.md) | Set the color for [id.image_view_pin_authentication_dot1](#) through [id.image_view_pin_authentication_dot14](#)'s image`fun set5_PinHintImageColor(colorRes: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`): PAColorsBuilder` |
| [set6_PinPadButtonBackgroundColor](set6_-pin-pad-button-background-color.md) | Set the background color for all buttons (except [id.button_pin_authentication_confirm](#))`fun set6_PinPadButtonBackgroundColor(colorRes: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`): PAColorsBuilder` |
| [set7_PinResetInfoImageColor](set7_-pin-reset-info-image-color.md) | Set the color for [id.image_view_pin_authentication_reset_help](#)`fun set7_PinResetInfoImageColor(colorRes: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`): PAColorsBuilder` |
| [set8_ScreenBackgroundColor](set8_-screen-background-color.md) | Set the color for [id.layout_constraint_pin_authentication_container](#)`fun set8_ScreenBackgroundColor(colorRes: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`): PAColorsBuilder` |
| [set9_TextColor](set9_-text-color.md) | Set the color for all text`fun set9_TextColor(colorRes: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`): PAColorsBuilder` |
