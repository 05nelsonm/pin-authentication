[pin-authentication](../../../index.md) / [io.matthewnelson.pin_authentication.service](../../index.md) / [PinAuthentication](../index.md) / [Controller](index.md) / [postLoginProcessStarted](./post-login-process-started.md)

# postLoginProcessStarted

`fun postLoginProcessStarted(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) [(source)](https://github.com/05nelsonm/pin-authentication/blob/master/pin-authentication/src/main/java/io/matthewnelson/pin_authentication/service/PinAuthentication.kt#L794)

After [Controller.hasPostLoginProcessBeenStarted](has-post-login-process-been-started.md)
returns false and your one-time processes start,
use this method to set the value to true so that
your post login processes won't be executed again
if the observer on
[Controller.hasInitialAppLoginBeenSatisfied](has-initial-app-login-been-satisfied.md) gets
proc'd again.

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

