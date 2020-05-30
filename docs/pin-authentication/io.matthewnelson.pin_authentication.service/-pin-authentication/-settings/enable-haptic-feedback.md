[pin-authentication](../../../index.md) / [io.matthewnelson.pin_authentication.service](../../index.md) / [PinAuthentication](../index.md) / [Settings](index.md) / [enableHapticFeedback](./enable-haptic-feedback.md)

# enableHapticFeedback

`fun enableHapticFeedback(enable: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) [(source)](https://github.com/05nelsonm/pin-authentication/blob/master/pin-authentication/src/main/java/io/matthewnelson/pin_authentication/service/PinAuthentication.kt#L852)

ENABLE/DISABLE haptic feedback on interactions with
[PinAuthenticationActivity](#).

``` kotlin
binding.switchSettingsHapticFeedback.setOnCheckedChangeListener { _, isChecked ->
    paSettings.enableHapticFeedback(isChecked)
}
```

### Parameters

`enable` - Boolean (`true` = ENABLE, `false` = DISABLE)