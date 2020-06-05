[pin-authentication](../../../index.md) / [io.matthewnelson.pin_authentication.service](../../index.md) / [PinAuthentication](../index.md) / [Controller](index.md) / [hasPostLoginProcessBeenStarted](./has-post-login-process-been-started.md)

# hasPostLoginProcessBeenStarted

`fun hasPostLoginProcessBeenStarted(): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) [(source)](https://github.com/05nelsonm/pin-authentication/blob/master/pin-authentication/src/main/java/io/matthewnelson/pin_authentication/service/PinAuthentication.kt#L773)

Check if your startup process has previously
been started.

This is needed so that when observing the
returned LiveData from
[Controller.hasInitialAppLoginBeenSatisfied](has-initial-app-login-been-satisfied.md),
so your startup processes only get executed once.

``` kotlin
if (!paController.hasPostLoginProcessBeenStarted()) {
    paController.postLoginProcessStarted()

    // if the on-board process was started, clear the backstack
    if (navController.currentDestination?.id != R.id.navigation_controller) {
        val navOptions = NavOptions.Builder()
            .setPopUpTo(R.id.mobile_navigation, true)
            .build()
        navController.navigate(R.id.navigation_controller, Bundle(), navOptions)
    }

    // When initial login is satisfied make the support action bar
    // and bottom nav bar visible again.
    supportActionBar?.show()
    navView.visibility = View.VISIBLE
}
```

**See Also**

[InitialAppLogin.hasPostLoginProcessBeenStarted](#)

**Return**
Boolean

