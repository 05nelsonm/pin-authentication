[pin-authentication](../../../index.md) / [io.matthewnelson.pin_authentication.service](../../index.md) / [PinAuthentication](../index.md) / [Settings](index.md) / [resetColorsToApplicationDefaults](./reset-colors-to-application-defaults.md)

# resetColorsToApplicationDefaults

`fun resetColorsToApplicationDefaults(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) [(source)](https://github.com/05nelsonm/pin-authentication/blob/master/pin-authentication/src/main/java/io/matthewnelson/pin_authentication/service/PinAuthentication.kt#L822)

Will clear currently applied colors and set them back
to colors defined in the Application onCreate()'s
initialization of
[PinAuthentication.Builder.PABuilder.setCustomColors](../-builder/-p-a-builder/set-custom-colors.md),
if they were specified. Otherwise it will fall back to
[PinAuthentication](../index.md)'s default colors.

``` kotlin
binding.buttonSettingsResetColors.setOnClickListener {
    paSettings.resetColorsToApplicationDefaults()
    App.showToast(getString(R.string.toast_reset_colors))
}
```

**See Also**

[PAViewColors.resetColorsToApplicationDefaults](#)

