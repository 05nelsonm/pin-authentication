[pin-authentication](../../../../index.md) / [io.matthewnelson.pin_authentication.service](../../../index.md) / [PinAuthentication](../../index.md) / [Builder](../index.md) / [ColorsBuilder](./index.md)

# ColorsBuilder

`class ColorsBuilder` [(source)](https://github.com/05nelsonm/pin-authentication/blob/master/pin-authentication/src/main/java/io/matthewnelson/pin_authentication/service/PinAuthentication.kt#L295)

Customize [PinAuthenticationActivity](#)'s colors.

This Builder is used in 2 ways. Via:

* [OptionsBuilder.setCustomColors](../-options-builder/set-custom-colors.md) method being called which "unlocks" these options
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
     * @see [PinAuthentication.Builder.OptionsBuilder.setCustomColors]
     * @see [PinAuthentication.Builder.ColorsBuilder]
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
     * @see [PinAuthentication.Builder.ColorsBuilder.applyColors]
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

`optionsBuilder` - [OptionsBuilder](../-options-builder/index.md)?

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | Customize [PinAuthenticationActivity](#)'s colors.`ColorsBuilder(optionsBuilder: OptionsBuilder?)` |

### Functions

| Name | Summary |
|---|---|
| [applyColors](apply-colors.md) | <ul><li>If being used when initializing [PinAuthentication.Builder](../index.md), will return [OptionsBuilder](../-options-builder/index.md)</li> <li>If being used from [PinAuthentication.Settings.setCustomColors](../../-settings/set-custom-colors.md), will return **`null`**.</li></ul>`fun applyColors(): OptionsBuilder?` |
| [set1_BackspaceButtonImageColor](set1_-backspace-button-image-color.md) | Set the color for [id.button_pin_authentication_backspace](#)'s image.`fun set1_BackspaceButtonImageColor(colorRes: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`): ColorsBuilder` |
| [set2_ConfirmButtonBackgroundColor](set2_-confirm-button-background-color.md) | Set the color for [id.button_pin_authentication_confirm](#)'s background.`fun set2_ConfirmButtonBackgroundColor(colorRes: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`): ColorsBuilder` |
| [set3_ConfirmButtonImageColor](set3_-confirm-button-image-color.md) | Set the color for [id.button_pin_authentication_confirm](#)'s image.`fun set3_ConfirmButtonImageColor(colorRes: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`): ColorsBuilder` |
| [set4_PinHintContainerColor](set4_-pin-hint-container-color.md) | Set the color for [id.layout_linear_pin_authentication_pin_hint](#)'s background.`fun set4_PinHintContainerColor(colorRes: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`): ColorsBuilder` |
| [set5_PinHintImageColor](set5_-pin-hint-image-color.md) | Set the color for [id.image_view_pin_authentication_dot1](#) through [id.image_view_pin_authentication_dot14](#)'s image`fun set5_PinHintImageColor(colorRes: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`): ColorsBuilder` |
| [set6_PinPadButtonBackgroundColor](set6_-pin-pad-button-background-color.md) | Set the background color for all buttons (except [id.button_pin_authentication_confirm](#))`fun set6_PinPadButtonBackgroundColor(colorRes: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`): ColorsBuilder` |
| [set7_PinResetInfoImageColor](set7_-pin-reset-info-image-color.md) | Set the color for [id.image_view_pin_authentication_reset_help](#)`fun set7_PinResetInfoImageColor(colorRes: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`): ColorsBuilder` |
| [set8_ScreenBackgroundColor](set8_-screen-background-color.md) | Set the color for [id.layout_constraint_pin_authentication_container](#)`fun set8_ScreenBackgroundColor(colorRes: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`): ColorsBuilder` |
| [set9_TextColor](set9_-text-color.md) | Set the color for all text`fun set9_TextColor(colorRes: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`): ColorsBuilder` |
