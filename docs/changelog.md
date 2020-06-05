# Change Log

## Version 1.0.0-beta01 (2020-06-05)
 - BugFix: Fix protectUserData method to only clear pinEntry.pin [[2a60e95]](https://github.com/05nelsonm/pin-authentication/commit/2a60e95fa231624d49804e4d57304826ec8fb6a3)
 - Add `internal` modifier to all classes not needing to be public [[cbf9682]](https://github.com/05nelsonm/pin-authentication/commit/cbf968286a4ebe9a92910ade4133c20194e00cc1)
 - Remove `PA` prefix from all non-annotation type classes [[69e060c]](https://github.com/05nelsonm/pin-authentication/commit/69e060cdba66cc118db73e4c4e8446b43a66cddd)

## Version 1.0.0-alpha02 (2020-06-02)
 - BugFix: Reset PIN feature was clearing the pinEntryCompare variable if user was logged out [[10801d3]](https://github.com/05nelsonm/pin-authentication/commit/10801d3af6ce19a11b1099068fbeebfee223ae69)
 - BugFix: Reset PIN feature was checking if variable was initialized instead of if it was empty [[e54e137]](https://github.com/05nelsonm/pin-authentication/commit/e54e1370cade9c73572e8f7e5a629eb6fb6e65fb)
 - Updated Sonatype repo GroupID to resolve SNAPSHOT conflicts and match package name

## Version 1.0.0-alpha01 (2020-05-30)

 - Initial Release