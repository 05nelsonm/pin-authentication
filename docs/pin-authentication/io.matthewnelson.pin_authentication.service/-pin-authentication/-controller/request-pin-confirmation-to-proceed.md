[pin-authentication](../../../index.md) / [io.matthewnelson.pin_authentication.service](../../index.md) / [PinAuthentication](../index.md) / [Controller](index.md) / [requestPinConfirmationToProceed](./request-pin-confirmation-to-proceed.md)

# requestPinConfirmationToProceed

`fun requestPinConfirmationToProceed(requestKey: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) [(source)](https://github.com/05nelsonm/pin-authentication/blob/master/pin-authentication/src/main/java/io/matthewnelson/pin_authentication/service/PinAuthentication.kt#L645)

To be used after registering the [requestKey](request-pin-confirmation-to-proceed.md#io.matthewnelson.pin_authentication.service.PinAuthentication.Controller$requestPinConfirmationToProceed(kotlin.String)/requestKey) via
[Controller.registerPinConfirmationToProceedRequestKey](register-pin-confirmation-to-proceed-request-key.md).

This will launch [PinAuthenticationActivity](#) in
[PAPinEntryState.CONFIRM_PIN](#) configuration. If the
[requestKey](request-pin-confirmation-to-proceed.md#io.matthewnelson.pin_authentication.service.PinAuthentication.Controller$requestPinConfirmationToProceed(kotlin.String)/requestKey) is not registered, it does nothing.

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

`requestKey` - String