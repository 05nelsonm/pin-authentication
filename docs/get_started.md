Get Started
===

## Requirements
 - Minimum API 23

## Implementation
 - In your App module's `build.gradle` file, add the following to the `android` block:
    ```groovy
    buildFeatures.dataBinding = true
    ```

 - In your App module's `build.gradle` file, add the following to the `dependencies` block:
    ```groovy
    implementation 'com.github.05nelsonm:pin-authentication:{{ pin_authentication.release }}'
    ```

 - In your Application class' `onCreate`, implement the following Builder and customize it as desired:
    ```kotlin
    PinAuthentication.Builder()
        .setApplicationAndBuildConfig(this, BuildConfig.DEBUG)
        .applicationHasOnBoardProcess()
        .enableBackgroundLogoutTimer(4)
        .enableHapticFeedbackByDefault()
        .enablePinSecurityByDefault()
        .enableScrambledPinByDefault()
        .enableWrongPinLockout(10, 3)
        .setMinimumPinLength(4)

        .setCustomColors()
        .set1_BackspaceButtonImageColor(R.color.pa_white)
        .set2_ConfirmButtonBackgroundColor(R.color.pa_super_green)
        .set3_ConfirmButtonImageColor(R.color.pa_white)
        .set4_PinHintContainerColor(R.color.pa_sea_blue)
        .set5_PinHintImageColor(R.color.pa_white)
        .set6_PinPadButtonBackgroundColor(R.color.pa_sea_blue)
        .set7_PinResetInfoImageColor(R.color.pa_white)
        .set8_ScreenBackgroundColor(R.color.pa_deep_sea_blue)
        .set9_TextColor(R.color.pa_white)
        .applyColors()!!

        .build()
    ```

    !!! Info
        The above Builder contains **all** available options. More info can be found
        [here](pin-authentication/io.matthewnelson.pin_authentication.service/-pin-authentication/-builder/index.md)
        about each individual option.

## Using the SNAPSHOT version

 - In your Project's `build.gradle` file, add the following to the `repositories` block:
     ```groovy
     mavenCentral()
     maven {
         url 'https://oss.sonatype.org/content/repositories/snapshots/'
     }
     ```
 - In your App module's `build.gradle` file, add (or modify) the following in the `dependencies` block:
     ```groovy
     implementation 'com.github.05nelsonm:pin-authentication:{{ pin_authentication.next_release }}-SNAPSHOT'
     ```

    !!! Warning
        SNAPSHOT versions are ever changing and may contain not yet fully fleshed out features. Do **not** ship a release.
