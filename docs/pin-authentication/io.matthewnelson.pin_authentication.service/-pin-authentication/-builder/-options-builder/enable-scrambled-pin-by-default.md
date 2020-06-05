[pin-authentication](../../../../index.md) / [io.matthewnelson.pin_authentication.service](../../../index.md) / [PinAuthentication](../../index.md) / [Builder](../index.md) / [OptionsBuilder](index.md) / [enableScrambledPinByDefault](./enable-scrambled-pin-by-default.md)

# enableScrambledPinByDefault

`fun enableScrambledPinByDefault(): OptionsBuilder` [(source)](https://github.com/05nelsonm/pin-authentication/blob/master/pin-authentication/src/main/java/io/matthewnelson/pin_authentication/service/PinAuthentication.kt#L200)

By default, scrambled pin is DISABLED.

Declaring this in your Builder will enable it. This can be changed later by the
user if you include in your settings UI the ability to enable it via accessing
[Settings.enableScrambledPin](../../-settings/enable-scrambled-pin.md) method. The user's setting for this will always
be loaded after this method.

See [Builder](../index.md) for sample code.

**Return**
[OptionsBuilder](index.md)

