[pin-authentication](../../../../index.md) / [io.matthewnelson.pin_authentication.service](../../../index.md) / [PinAuthentication](../../index.md) / [Builder](../index.md) / [PABuilder](index.md) / [enableHapticFeedbackByDefault](./enable-haptic-feedback-by-default.md)

# enableHapticFeedbackByDefault

`fun enableHapticFeedbackByDefault(): PABuilder` [(source)](https://github.com/05nelsonm/pin-authentication/blob/master/pin-authentication/src/main/java/io/matthewnelson/pin_authentication/service/PinAuthentication.kt#L170)

By default, haptic feedback is DISABLED.

Declaring this in your Builder will enable it. This can be changed later by the
user if you include in your settings UI the ability to enable it via accessing
[Settings.enableHapticFeedback](../../-settings/enable-haptic-feedback.md) method. The user's setting for this will always
be loaded after this method.

See [Builder](../index.md) for sample code.

**Return**
[PABuilder](index.md)

