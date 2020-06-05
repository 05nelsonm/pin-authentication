[pin-authentication](../../../index.md) / [io.matthewnelson.pin_authentication.service](../../index.md) / [PinAuthentication](../index.md) / [Controller](index.md) / [completeOnBoardProcess](./complete-on-board-process.md)

# completeOnBoardProcess

`fun completeOnBoardProcess(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) [(source)](https://github.com/05nelsonm/pin-authentication/blob/master/pin-authentication/src/main/java/io/matthewnelson/pin_authentication/service/PinAuthentication.kt#L526)

Declares the on-board process as being complete,
and if Pin Security is:

* ENABLED: Immediately prompts the user to set
    their PIN before continuing.

**OR**

* DISABLED: Sets [InitialAppLogin.initialApplicationLoginSatisfied](#)
    to true.

Declaring the on-board process as completed is saved
to SharedPreferences such that henceforth,
[PinAuthentication](../index.md) will immediately request a
PIN to login at application start (if pin security
is enabled).

``` kotlin
binding.buttonOnBoardNext.setOnClickListener {
    PinAuthentication.Controller().completeOnBoardProcess()
    resetMotionLayout()
}
```

**See Also**

[AppLockObserver.hijackApp](#)

