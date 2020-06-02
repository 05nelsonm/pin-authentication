[pin-authentication](../../../../index.md) / [io.matthewnelson.pin_authentication.service](../../../index.md) / [PinAuthentication](../../index.md) / [Builder](../index.md) / [PAColorsBuilder](index.md) / [&lt;init&gt;](./-init-.md)

# &lt;init&gt;

`PAColorsBuilder(paBuilder: PABuilder?)`

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