[pin-authentication](../../../../index.md) / [io.matthewnelson.pin_authentication.service](../../../index.md) / [PinAuthentication](../../index.md) / [Builder](../index.md) / [PABuilder](index.md) / [enableWrongPinLockout](./enable-wrong-pin-lockout.md)

# enableWrongPinLockout

`fun enableWrongPinLockout(lockoutDurationSeconds: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`, maxPinAttempts: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`): PABuilder` [(source)](https://github.com/05nelsonm/pin-authentication/blob/master/pin-authentication/src/main/java/io/matthewnelson/pin_authentication/service/PinAuthentication.kt#L148)

By default, wrong pin lockout is DISABLED.

Enabling this will inhibit the user from confirming their pin after the declared
value of [maxPinAttempts](enable-wrong-pin-lockout.md#io.matthewnelson.pin_authentication.service.PinAuthentication.Builder.PABuilder$enableWrongPinLockout(kotlin.Int, kotlin.Int)/maxPinAttempts) has been met.

They must wait for the declared value of [lockoutDurationSeconds](enable-wrong-pin-lockout.md#io.matthewnelson.pin_authentication.service.PinAuthentication.Builder.PABuilder$enableWrongPinLockout(kotlin.Int, kotlin.Int)/lockoutDurationSeconds) until they can
try again.

See [Builder](../index.md) for sample code.

### Parameters

`lockoutDurationSeconds` - Int

`maxPinAttempts` - Int

**See Also**

[PAWrongPinLockout](#)

**Return**
[PABuilder](index.md)

