[pin-authentication](../../../index.md) / [io.matthewnelson.pin_authentication.service](../../index.md) / [PinAuthentication](../index.md) / [Controller](index.md) / [registerPinConfirmationToProceedRequestKey](./register-pin-confirmation-to-proceed-request-key.md)

# registerPinConfirmationToProceedRequestKey

`fun registerPinConfirmationToProceedRequestKey(activity: `[`Activity`](https://developer.android.com/reference/android/app/Activity.html)`, requestKey: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): LiveData<`[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`>?` [(source)](https://github.com/05nelsonm/pin-authentication/blob/master/pin-authentication/src/main/java/io/matthewnelson/pin_authentication/service/PinAuthentication.kt#L581)

Register request keys.

`fun registerPinConfirmationToProceedRequestKey(fragment: Fragment, requestKey: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): LiveData<`[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`>?` [(source)](https://github.com/05nelsonm/pin-authentication/blob/master/pin-authentication/src/main/java/io/matthewnelson/pin_authentication/service/PinAuthentication.kt#L608)

Adds a requestKey to [PAConfirmPinToProceed.mapRequestKeys](#)
and returns LiveData associated with it which will change
after calling [Controller.requestPinConfirmationToProceed](request-pin-confirmation-to-proceed.md)
depending on whether or not the User enters the correct pin.

* TRUE -&gt; Pin was confirmed
* FALSE -&gt; Pin has not been confirmed

If the [requestKey](register-pin-confirmation-to-proceed-request-key.md#io.matthewnelson.pin_authentication.service.PinAuthentication.Controller$registerPinConfirmationToProceedRequestKey(androidx.fragment.app.Fragment, kotlin.String)/requestKey) is already registered, it will **not**
overwrite the current value.

``` kotlin
// This button modifies itself based on the status of PIN confirmation for the
// particular request.

// If the user changes their PIN Security setting, all values associated with registered
// RequestKeys will be switched to `false` (at the time PIN Security is enabled), or
// `true` (at the time PIN Security is disabled).

// At time of registering new RequestKeys, their values will be set to opposite of
// PIN Security's state.

// STEP 1: Register the RequestKey & observe the returned LiveData
paController.registerPinConfirmationToProceedRequestKey(this, PIN_CONFIRM_REQUEST_KEY_1)
    ?.observe(viewLifecycleOwner, Observer {
        if (it) { // TRUE: User has confirmed their PIN

            // Set button text & tint
            binding.buttonControllerConfirmPinEx1.text = getString(R.string.button_controller_send_something)
            binding.buttonControllerConfirmPinEx1.background
                .setTint(binding.root.context.getColor(R.color.secondaryLightColor))

            binding.buttonControllerConfirmPinEx1.setOnClickListener {
                doSomething(getString(R.string.toast_append_ex1))

                // STEP 3: Reset the value associated with the RequestKey
                paController.resetPinConfirmationToProceedRequestKey(PIN_CONFIRM_REQUEST_KEY_1)
            }
        } else { // FALSE: User has yet to confirm their PIN

            // Set button text & tint
            binding.buttonControllerConfirmPinEx1.text = getString(R.string.button_controller_confirm_pin_example_1)
            binding.buttonControllerConfirmPinEx1.background
                .setTint(binding.root.context.getColor(R.color.button_gray))

            // STEP 2: Request Pin Confirmation To Proceed
            binding.buttonControllerConfirmPinEx1.setOnClickListener {
                paController.requestPinConfirmationToProceed(PIN_CONFIRM_REQUEST_KEY_1)
            }
        }
    })

// STEP 4: Unregister the RequestKey in the Activity's onDestroy(), or a
//         Fragment's onDestroyView() _after_ the super call.
```

``` kotlin
// Alternative implementation of Pin Confirmation To Proceed which will go right
// into doing something upon confirmation, or if PIN Security is DISABLED, will
// simply require a click to proceed w/o confirming a PIN.

paController.registerPinConfirmationToProceedRequestKey(this, PIN_CONFIRM_REQUEST_KEY_2)
    ?.observe(viewLifecycleOwner, Observer {
        if (it) { // TRUE: User has confirmed their PIN

            // Set button text & tint
            binding.buttonControllerConfirmPinEx2.text =
                getString(R.string.button_controller_send_something)
            binding.buttonControllerConfirmPinEx2.background
                .setTint(binding.root.context.getColor(R.color.secondaryLightColor))

            // If Pin Security is ENABLED, we want it to go right into doing something after
            // the user confirms their PIN.
            if (paController.isPinSecurityEnabled()) {
                doSomething(getString(R.string.toast_append_ex2))

                // Reset the value associated with the RequestKey to switch the button
                // back to `unconfirmed` status.
                paController.resetPinConfirmationToProceedRequestKey(
                    PIN_CONFIRM_REQUEST_KEY_2
                )
            } else {

                // If PIN Security is DISABLED, we want to require a click so it doesn't
                // just execute something automatically when the screen is created.
                binding.buttonControllerConfirmPinEx2.setOnClickListener {
                    doSomething(getString(R.string.toast_append_ex2))

                    // Reset the value associated with the RequestKey to switch the button
                    // back to `unconfirmed` status.
                    paController.resetPinConfirmationToProceedRequestKey(
                        PIN_CONFIRM_REQUEST_KEY_2
                    )
                }

            }

        } else { // FALSE: User has yet to confirm their PIN

            // Set button text & tint
            binding.buttonControllerConfirmPinEx2.text =
                getString(R.string.button_controller_confirm_pin_example_2)
            binding.buttonControllerConfirmPinEx2.background
                .setTint(binding.root.context.getColor(R.color.button_gray))

            binding.buttonControllerConfirmPinEx2.setOnClickListener {

                // Make request to confirm PIN for this given request key.
                paController.requestPinConfirmationToProceed(PIN_CONFIRM_REQUEST_KEY_2)
            }
        }
    })
```

### Parameters

`activity` - Activity **OR**

`fragment` - Fragment

`requestKey` - String

**Return**
LiveData?

