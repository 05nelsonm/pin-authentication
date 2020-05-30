[pin-authentication](../../../index.md) / [io.matthewnelson.pin_authentication.service](../../index.md) / [PinAuthentication](../index.md) / [Controller](index.md) / [hasInitialAppLoginBeenSatisfied](./has-initial-app-login-been-satisfied.md)

# hasInitialAppLoginBeenSatisfied

`fun hasInitialAppLoginBeenSatisfied(): LiveData<`[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`>` [(source)](https://github.com/05nelsonm/pin-authentication/blob/master/pin-authentication/src/main/java/io/matthewnelson/pin_authentication/service/PinAuthentication.kt#L755)

Returns a boolean value that will change from
false to true, and stay true until the
application is terminated.

This boolean value gets changed when the user
opens the application and:

* If pin security is ENABLED and they
successfully log in.

**OR**

* If pin security was DISABLED and the
application on-board process has been
satisfied when this method is called for the
first time.

Primary use case for this is to hold application
processes until authentication has been had, for
example, on your landing Activity/Fragment.

``` kotlin
paController.hasInitialAppLoginBeenSatisfied().observe(this,  Observer<Boolean> {
    if (it) {

        // Update our currentTheme and recreate
        if (currentTheme == R.style.OnApplicationStartTheme) {

            // Could also load user selected themes from SharedPrefs here

            currentTheme = R.style.PostInitialLoginTheme
            recreate()
        } else {

            executePostLoginProcesses()

        }
    } else { // User has yet to either authenticate, or complete the on board process

        doOnBoard()

        // Hide the support action bar and bottom nav bar so we get a clean looking
        // transition on app startup.
        supportActionBar?.hide()
        navView.visibility = View.GONE
    }
})
```

**Return**
LiveData

