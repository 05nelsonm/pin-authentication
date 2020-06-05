[pin-authentication](../../../index.md) / [io.matthewnelson.pin_authentication.service](../../index.md) / [PinAuthentication](../index.md) / [Settings](index.md) / [enableScrambledPin](./enable-scrambled-pin.md)

# enableScrambledPin

`fun enableScrambledPin(enable: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) [(source)](https://github.com/05nelsonm/pin-authentication/blob/master/pin-authentication/src/main/java/io/matthewnelson/pin_authentication/service/PinAuthentication.kt#L942)

ENABLE/DISABLE scrambled pin for [PinAuthenticationActivity](#)

``` kotlin
binding.switchSettingsScrambledPin.setOnCheckedChangeListener { _, isChecked ->
    paSettings.enableScrambledPin(isChecked)
}
```

### Parameters

`enable` - Boolean (`true` = ENABLE, `false` = DISABLE)