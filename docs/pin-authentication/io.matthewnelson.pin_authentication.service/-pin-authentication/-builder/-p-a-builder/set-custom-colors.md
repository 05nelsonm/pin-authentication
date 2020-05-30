[pin-authentication](../../../../index.md) / [io.matthewnelson.pin_authentication.service](../../../index.md) / [PinAuthentication](../../index.md) / [Builder](../index.md) / [PABuilder](index.md) / [setCustomColors](./set-custom-colors.md)

# setCustomColors

`fun setCustomColors(): PAColorsBuilder` [(source)](https://github.com/05nelsonm/pin-authentication/blob/master/pin-authentication/src/main/java/io/matthewnelson/pin_authentication/service/PinAuthentication.kt#L250)

Calling this method will return another Builder for customizing the colors
of [PinAuthenticationActivity](#).

When done customizing the colors, call [PAColorsBuilder.applyColors](../-p-a-colors-builder/apply-colors.md) and it
will return you to the previous [PABuilder](index.md) to continue initialization.

If this method is being called it will ensure that the Application colors
stay set so that, if at a later time colors are changed via
[Settings.setCustomColors](../../-settings/set-custom-colors.md) method, reversion back to original colors defined
during the Application's onCreate() process will be had and not
[PinAuthentication](../../index.md)'s default colors.

See [Builder](../index.md) for sample code.

**Return**
[PAColorsBuilder](../-p-a-colors-builder/index.md)

