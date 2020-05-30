[pin-authentication](../../../index.md) / [io.matthewnelson.pin_authentication.service](../../index.md) / [PinAuthentication](../index.md) / [Builder](index.md) / [setApplicationAndBuildConfig](./set-application-and-build-config.md)

# setApplicationAndBuildConfig

`fun setApplicationAndBuildConfig(application: `[`Application`](https://developer.android.com/reference/android/app/Application.html)`, buildConfigDebug: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`): PABuilder` [(source)](https://github.com/05nelsonm/pin-authentication/blob/master/pin-authentication/src/main/java/io/matthewnelson/pin_authentication/service/PinAuthentication.kt#L48)

Sets the Application which is used throughout [PinAuthentication](../index.md). It initializes
[PinAuthentication](../index.md)'s [DaggerPAApplicationComponent](#) which then is used to inject
classes as needed.
See [PAInjection](#)
See [Companion.injected](#)

It also sets the [PinAuthenticationActivity](#)'s window flag to secure for release
builds to inhibit screen capture of the user's PIN.

See [Builder](index.md) for sample code.

### Parameters

`application` - Application

`buildConfigDebug` - Boolean - (send BuildConfig.DEBUG)

**Return**
[PABuilder](-p-a-builder/index.md)

