[pin-authentication](../../../../index.md) / [io.matthewnelson.pin_authentication.service](../../../index.md) / [PinAuthentication](../../index.md) / [Builder](../index.md) / [PABuilder](./index.md)

# PABuilder

`class PABuilder` [(source)](https://github.com/05nelsonm/pin-authentication/blob/master/pin-authentication/src/main/java/io/matthewnelson/pin_authentication/service/PinAuthentication.kt#L72)

Meant to only be used after calling [Builder.setApplicationAndBuildConfig](../set-application-and-build-config.md), which
returns this class.

See [Builder](../index.md) for sample code.

### Parameters

`paApplicationComponent` - [PAApplicationComponent](#)

`paInjection` - [PAInjection](#)

`buildConfigDebug` - Boolean

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | Meant to only be used after calling [Builder.setApplicationAndBuildConfig](../set-application-and-build-config.md), which returns this class.`PABuilder(paApplicationComponent: PAApplicationComponent, paInjection: PAInjection, buildConfigDebug: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`)` |

### Functions

| Name | Summary |
|---|---|
| [applicationHasOnBoardProcess](application-has-on-board-process.md) | By default, application has on-board process is DISABLED.`fun applicationHasOnBoardProcess(): PABuilder` |
| [build](build.md) | Initializes [PinAuthentication](../../index.md)`fun build(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [enableBackgroundLogoutTimer](enable-background-logout-timer.md) | By default, background logout timer is DISABLED.`fun enableBackgroundLogoutTimer(secondsLessThan30: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`): PABuilder` |
| [enableHapticFeedbackByDefault](enable-haptic-feedback-by-default.md) | By default, haptic feedback is DISABLED.`fun enableHapticFeedbackByDefault(): PABuilder` |
| [enablePinSecurityByDefault](enable-pin-security-by-default.md) | By default, pin security is DISABLED.`fun enablePinSecurityByDefault(): PABuilder` |
| [enableScrambledPinByDefault](enable-scrambled-pin-by-default.md) | By default, scrambled pin is DISABLED.`fun enableScrambledPinByDefault(): PABuilder` |
| [enableWrongPinLockout](enable-wrong-pin-lockout.md) | By default, wrong pin lockout is DISABLED.`fun enableWrongPinLockout(lockoutDurationSeconds: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`, maxPinAttempts: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`): PABuilder` |
| [setCustomColors](set-custom-colors.md) | Calling this method will return another Builder for customizing the colors of [PinAuthenticationActivity](#).`fun setCustomColors(): PAColorsBuilder` |
| [setMinimumPinLength](set-minimum-pin-length.md) | By default, minimum pin length is set to 4.`fun setMinimumPinLength(intFrom4To14: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`): PABuilder` |
