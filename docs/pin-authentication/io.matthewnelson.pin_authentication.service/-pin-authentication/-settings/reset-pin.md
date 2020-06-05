[pin-authentication](../../../index.md) / [io.matthewnelson.pin_authentication.service](../../index.md) / [PinAuthentication](../index.md) / [Settings](index.md) / [resetPin](./reset-pin.md)

# resetPin

`fun resetPin(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) [(source)](https://github.com/05nelsonm/pin-authentication/blob/master/pin-authentication/src/main/java/io/matthewnelson/pin_authentication/service/PinAuthentication.kt#L871)

Launches [PinAuthenticationActivity](#) in
[PAPinEntryState.RESET_PIN](#) configuration for
the user to reset their PIN.

``` kotlin
binding.buttonSettingsPinReset.setOnClickListener {
    paSettings.resetPin()
}
```

**See Also**

[AppLockObserver.hijackApp](#)

