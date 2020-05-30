[pin-authentication](../../../../index.md) / [io.matthewnelson.pin_authentication.service](../../../index.md) / [PinAuthentication](../../index.md) / [Builder](../index.md) / [PABuilder](index.md) / [enableBackgroundLogoutTimer](./enable-background-logout-timer.md)

# enableBackgroundLogoutTimer

`fun enableBackgroundLogoutTimer(secondsLessThan30: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`): PABuilder` [(source)](https://github.com/05nelsonm/pin-authentication/blob/master/pin-authentication/src/main/java/io/matthewnelson/pin_authentication/service/PinAuthentication.kt#L123)

By default, background logout timer is DISABLED.

Sets the length of time the application spends in the background before
automatically logging out.

The *maximum* time to set this value is 29s.

See [Builder](../index.md) for sample code.

### Parameters

`secondsLessThan30` - Int - (Set to 0 to DISABLE)

**See Also**

[PAAppLockObserver.launchAuthInvalidationJobIfInactive](#)

**Return**
[PABuilder](index.md)

