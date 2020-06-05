[pin-authentication](../../../../index.md) / [io.matthewnelson.pin_authentication.service](../../../index.md) / [PinAuthentication](../../index.md) / [Builder](../index.md) / [OptionsBuilder](index.md) / [setMinimumPinLength](./set-minimum-pin-length.md)

# setMinimumPinLength

`fun setMinimumPinLength(intFrom4To14: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`): OptionsBuilder` [(source)](https://github.com/05nelsonm/pin-authentication/blob/master/pin-authentication/src/main/java/io/matthewnelson/pin_authentication/service/PinAuthentication.kt#L222)

By default, minimum pin length is set to 4.

Set a minimum pin length, between 4 to 14.

This minimum value must be met when the user is entering their PIN before the
confirm button for [PinAuthenticationActivity](#) becomes visible.

See [Builder](../index.md) for sample code.

### Parameters

`intFrom4To14` - Int

**See Also**

[id.button_pin_authentication_confirm](#)

[BindingAdapters.paInvisibleUnless](#)

**Return**
[OptionsBuilder](index.md)

