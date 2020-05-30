[pin-authentication](../../../index.md) / [io.matthewnelson.pin_authentication.service](../../index.md) / [PinAuthentication](../index.md) / [Settings](index.md) / [enablePinSecurity](./enable-pin-security.md)

# enablePinSecurity

`fun enablePinSecurity(enable: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) [(source)](https://github.com/05nelsonm/pin-authentication/blob/master/pin-authentication/src/main/java/io/matthewnelson/pin_authentication/service/PinAuthentication.kt#L901)

Starts the process for ENABLING/DISABLING pin security.

Going from:

* DISABLED -&gt; ENABLED:
  * Will prompt the user to set a pin.
* ENABLED -&gt; DISABLED:
  * Will prompt the user to confirm their pin.

``` kotlin
binding.switchSettingsPinSecurity.setOnClickListener {
    paSettings.enablePinSecurity(binding.switchSettingsPinSecurity.isChecked)
}
```

### Parameters

`enable` - Boolean (`true` = ENABLE, `false` = DISABLE)

**See Also**

[PAPinSecurity](#)

