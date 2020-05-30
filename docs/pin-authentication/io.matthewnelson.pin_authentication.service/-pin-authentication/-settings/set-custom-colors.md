[pin-authentication](../../../index.md) / [io.matthewnelson.pin_authentication.service](../../index.md) / [PinAuthentication](../index.md) / [Settings](index.md) / [setCustomColors](./set-custom-colors.md)

# setCustomColors

`fun setCustomColors(): PAColorsBuilder` [(source)](https://github.com/05nelsonm/pin-authentication/blob/master/pin-authentication/src/main/java/io/matthewnelson/pin_authentication/service/PinAuthentication.kt#L837)

Change the colors of [PinAuthenticationActivity](#)
on the fly. These settings get saved to
[PinAuthentication](../index.md)'s SharedPreferences and loaded
at startup **after** custom colors that may have been
specified in the Application onCreate()'s
[PinAuthentication.Builder.PABuilder.setCustomColors](../-builder/-p-a-builder/set-custom-colors.md).

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

**Return**
[Builder.PAColorsBuilder](../-builder/-p-a-colors-builder/index.md)

