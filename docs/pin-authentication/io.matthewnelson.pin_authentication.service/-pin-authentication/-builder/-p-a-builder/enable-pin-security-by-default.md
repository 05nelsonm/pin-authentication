[pin-authentication](../../../../index.md) / [io.matthewnelson.pin_authentication.service](../../../index.md) / [PinAuthentication](../../index.md) / [Builder](../index.md) / [PABuilder](index.md) / [enablePinSecurityByDefault](./enable-pin-security-by-default.md)

# enablePinSecurityByDefault

`fun enablePinSecurityByDefault(): PABuilder` [(source)](https://github.com/05nelsonm/pin-authentication/blob/master/pin-authentication/src/main/java/io/matthewnelson/pin_authentication/service/PinAuthentication.kt#L187)

By default, pin security is DISABLED.

Declaring this in your Builder will enable it. This can be changed later by the
user if you include in your settings UI the ability to enable it via accessing
[Settings.enablePinSecurity](../../-settings/enable-pin-security.md) method. The user's setting for this will always
be loaded after this method.

See [Builder](../index.md) for sample code.

**Return**
[PABuilder](index.md)

