[pin-authentication](../../../../index.md) / [io.matthewnelson.pin_authentication.service](../../../index.md) / [PinAuthentication](../../index.md) / [Builder](../index.md) / [OptionsBuilder](index.md) / [applicationHasOnBoardProcess](./application-has-on-board-process.md)

# applicationHasOnBoardProcess

`fun applicationHasOnBoardProcess(): OptionsBuilder` [(source)](https://github.com/05nelsonm/pin-authentication/blob/master/pin-authentication/src/main/java/io/matthewnelson/pin_authentication/service/PinAuthentication.kt#L98)

By default, application has on-board process is DISABLED.

Enabling this will delay everything until on-boarding process has been completed.

At end of your on-boarding, hit the [Controller.completeOnBoardProcess](../../-controller/complete-on-board-process.md) method
to kick everything off.

See [Builder](../index.md) for sample code.

**Return**
[OptionsBuilder](index.md)

