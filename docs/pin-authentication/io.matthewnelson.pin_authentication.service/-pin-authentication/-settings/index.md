[pin-authentication](../../../index.md) / [io.matthewnelson.pin_authentication.service](../../index.md) / [PinAuthentication](../index.md) / [Settings](./index.md)

# Settings

`class Settings` [(source)](https://github.com/05nelsonm/pin-authentication/blob/master/pin-authentication/src/main/java/io/matthewnelson/pin_authentication/service/PinAuthentication.kt#L805)

PUBLIC methods to be utilized by the Application that's
implementing [PinAuthentication](../index.md) for allowing Users
the ability to modify various settings.

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | PUBLIC methods to be utilized by the Application that's implementing [PinAuthentication](../index.md) for allowing Users the ability to modify various settings.`Settings()` |

### Functions

| Name | Summary |
|---|---|
| [enableHapticFeedback](enable-haptic-feedback.md) | ENABLE/DISABLE haptic feedback on interactions with [PinAuthenticationActivity](#).`fun enableHapticFeedback(enable: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [enablePinSecurity](enable-pin-security.md) | Starts the process for ENABLING/DISABLING pin security.`fun enablePinSecurity(enable: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [enableScrambledPin](enable-scrambled-pin.md) | ENABLE/DISABLE scrambled pin for [PinAuthenticationActivity](#)`fun enableScrambledPin(enable: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [isHapticFeedbackEnabled](is-haptic-feedback-enabled.md) | Checks if haptic feedback is enabled.`fun isHapticFeedbackEnabled(): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [isPinSecurityEnabled](is-pin-security-enabled.md) | Checks if PIN security is enabled.`fun isPinSecurityEnabled(): LiveData<`[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`>?` |
| [isScrambledPinEnabled](is-scrambled-pin-enabled.md) | Checks if scrambled pin is enabled.`fun isScrambledPinEnabled(): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [resetColorsToApplicationDefaults](reset-colors-to-application-defaults.md) | Will clear currently applied colors and set them back to colors defined in the Application onCreate()'s initialization of [PinAuthentication.Builder.PABuilder.setCustomColors](../-builder/-p-a-builder/set-custom-colors.md), if they were specified. Otherwise it will fall back to [PinAuthentication](../index.md)'s default colors.`fun resetColorsToApplicationDefaults(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [resetPin](reset-pin.md) | Launches [PinAuthenticationActivity](#) in [PAPinEntryState.RESET_PIN](#) configuration for the user to reset their PIN.`fun resetPin(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [setCustomColors](set-custom-colors.md) | Change the colors of [PinAuthenticationActivity](#) on the fly. These settings get saved to [PinAuthentication](../index.md)'s SharedPreferences and loaded at startup **after** custom colors that may have been specified in the Application onCreate()'s [PinAuthentication.Builder.PABuilder.setCustomColors](../-builder/-p-a-builder/set-custom-colors.md).`fun setCustomColors(): PAColorsBuilder` |
