[pin-authentication](../../../index.md) / [io.matthewnelson.pin_authentication.service](../../index.md) / [PinAuthentication](../index.md) / [Settings](index.md) / [isPinSecurityEnabled](./is-pin-security-enabled.md)

# isPinSecurityEnabled

`fun isPinSecurityEnabled(): LiveData<`[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`>?` [(source)](https://github.com/05nelsonm/pin-authentication/blob/master/pin-authentication/src/main/java/io/matthewnelson/pin_authentication/service/PinAuthentication.kt#L928)

Checks if PIN security is enabled.

``` kotlin
paSettings.isPinSecurityEnabled()?.observe(viewLifecycleOwner, Observer {
    if (it != null) {
        binding.switchSettingsPinSecurity.isChecked = it
        binding.buttonSettingsPinReset.isEnabled = it
    }
})
```

**Return**
LiveData?

