[pin-authentication](../../../index.md) / [io.matthewnelson.pin_authentication.service](../../index.md) / [PinAuthentication](../index.md) / [Controller](index.md) / [unregisterPinConfirmationToProceedRequestKey](./unregister-pin-confirmation-to-proceed-request-key.md)

# unregisterPinConfirmationToProceedRequestKey

`fun unregisterPinConfirmationToProceedRequestKey(requestKey: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) [(source)](https://github.com/05nelsonm/pin-authentication/blob/master/pin-authentication/src/main/java/io/matthewnelson/pin_authentication/service/PinAuthentication.kt#L695)

Remove registered request key.

`fun unregisterPinConfirmationToProceedRequestKey(requestKeys: `[`Array`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) [(source)](https://github.com/05nelsonm/pin-authentication/blob/master/pin-authentication/src/main/java/io/matthewnelson/pin_authentication/service/PinAuthentication.kt#L712)

Removes the requestKey and LiveData values from the Map
contained in [PAConfirmPinToProceed.mapRequestKeys](#).

To be implemented in an Activity's onDestroy() or a
Fragments onDestroyView(), **after** the super call.

``` kotlin
super.onDestroyView()

// Method accepts a single string, or an array of strings.
paController.unregisterPinConfirmationToProceedRequestKey(
    arrayOf(PIN_CONFIRM_REQUEST_KEY_1, PIN_CONFIRM_REQUEST_KEY_2)
)
```

### Parameters

`requestKey` - String **OR**

`requestKeys` - Array

**See Also**

[PAConfirmPinToProceed.unregisterRequestKey](#)

