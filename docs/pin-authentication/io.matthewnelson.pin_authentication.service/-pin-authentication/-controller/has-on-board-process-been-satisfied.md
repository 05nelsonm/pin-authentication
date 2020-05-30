[pin-authentication](../../../index.md) / [io.matthewnelson.pin_authentication.service](../../index.md) / [PinAuthentication](../index.md) / [Controller](index.md) / [hasOnBoardProcessBeenSatisfied](./has-on-board-process-been-satisfied.md)

# hasOnBoardProcessBeenSatisfied

`fun hasOnBoardProcessBeenSatisfied(): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) [(source)](https://github.com/05nelsonm/pin-authentication/blob/master/pin-authentication/src/main/java/io/matthewnelson/pin_authentication/service/PinAuthentication.kt#L563)

Returns TRUE if:

* You declared that your application has an on-board process via
[PinAuthentication.Builder.PABuilder.applicationHasOnBoardProcess](../-builder/-p-a-builder/application-has-on-board-process.md)
and the on-board process has been marked **complete**.

**OR**

* You did **not** declare that your application has an on-board process.

Returns FALSE if:

* You declared that your application has an on-board process via
[PinAuthentication.Builder.PABuilder.applicationHasOnBoardProcess](../-builder/-p-a-builder/application-has-on-board-process.md),
but the on-board process has **not** been marked complete.

``` kotlin
if (!paController.hasOnBoardProcessBeenSatisfied()) {

    if (navController.currentDestination?.id != R.id.navigation_on_board) {
        navController.navigate(R.id.navigation_on_board)
    }

}
```

**Return**
Boolean

