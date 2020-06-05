package io.matthewnelson.pin_authentication.service.components

import io.matthewnelson.pin_authentication.util.PrefsKeys
import io.matthewnelson.pin_authentication.util.definitions.PAConfirmPinStatus
import io.matthewnelson.pin_authentication.service.PinAuthentication
import io.matthewnelson.encrypted_storage.EncryptedStorage

/**
 * @suppress
 * */
internal class WrongPinLockout(private val prefs: EncryptedStorage.Prefs) {

    ///////////////////////
    // Wrong Pin Lockout //
    ///////////////////////
    private var lockoutDurationSeconds = lockoutDurationInitValue
    private var maxPinAttempts = maxPinAttemptsInitValue

    fun getLockoutDurationSeconds(): Int =
        lockoutDurationSeconds

    /**
     * Checks for if the user is still locked out due to wrong pin attempts that
     * exceed the maximum pin attempts defined in the
     * [PinAuthentication.Builder.OptionsBuilder.enableWrongPinLockout] method.
     *
     * @return Int
     * */
    fun getSecondsRemaining(): Int {

        var lockoutTimeRemaining = 0
        val timeOfLastWrongPinAttempt = readTimeOfLastAttempt()
        if (timeOfLastWrongPinAttempt > 0) {
            val timeDiffMillis = System.currentTimeMillis() - timeOfLastWrongPinAttempt
            val setDurationMillis = lockoutDurationSeconds * 1000

            if (timeDiffMillis <= setDurationMillis &&
                readNumberOfAttempts() > maxPinAttempts
            ) {
                lockoutTimeRemaining =
                    ((setDurationMillis - timeDiffMillis).toFloat() / 1000).toInt()
            }
        }
        return lockoutTimeRemaining
    }

    fun removeLockoutData() {
        prefs.remove(PrefsKeys.WRONG_PIN_ATTEMPT_COUNTER)
        prefs.remove(PrefsKeys.WRONG_PIN_OCCURRENCE)
    }

    fun updateDataAndGetReturnString(): Int {
        var wrongPinAttempts = readNumberOfAttempts()
        wrongPinAttempts++
        writeAttemptsToPrefs(wrongPinAttempts)
        writeTimeOfAttemptToPrefs()

        return when {
            wrongPinAttempts < maxPinAttempts -> {
                PAConfirmPinStatus.WRONG_PIN
            }
            wrongPinAttempts == maxPinAttempts -> {
                PAConfirmPinStatus.ONE_MORE_ATTEMPT
            }
            wrongPinAttempts > maxPinAttempts -> {
                PAConfirmPinStatus.LOCKED_OUT
            }
            else -> {
                PAConfirmPinStatus.WRONG_PIN
            }
        }
    }

    fun variablyDecrementAttemptsCounter() {
        val previousAttempt = readTimeOfLastAttempt()
        if (previousAttempt > 0) {
            var wrongPinCounter = readNumberOfAttempts()

            val timeDiffMillis = System.currentTimeMillis() - previousAttempt
            val lockoutDurationMillis = lockoutDurationSeconds * 1000

            when {
                timeDiffMillis > lockoutDurationMillis * maxPinAttempts -> {
                    removeLockoutData()
                }
                timeDiffMillis > lockoutDurationMillis * 2 -> {
                    if (wrongPinCounter - 2 > 1) {
                        wrongPinCounter -= 2
                        writeAttemptsToPrefs(wrongPinCounter)
                    } else {
                        removeLockoutData()
                    }
                }
                timeDiffMillis > lockoutDurationMillis -> {
                    if (wrongPinCounter - 1 > 1) {
                        wrongPinCounter--
                        writeAttemptsToPrefs(wrongPinCounter)
                    } else {
                        removeLockoutData()
                    }
                }
            }
        }
    }

    private fun readNumberOfAttempts(): Int {
        var attempts = 1
        prefs.read(PrefsKeys.WRONG_PIN_ATTEMPT_COUNTER, EncryptedStorage.Prefs.INVALID_INT)
            .let {
                if (it != EncryptedStorage.Prefs.INVALID_INT) {
                    attempts = it
                }
            }
        return attempts
    }

    private fun readTimeOfLastAttempt(): Long {
        prefs.read(PrefsKeys.WRONG_PIN_OCCURRENCE, EncryptedStorage.Prefs.INVALID_LONG)
            .let { previousAttempt ->
                return if (previousAttempt != EncryptedStorage.Prefs.INVALID_LONG) {
                    previousAttempt
                } else {
                    0
                }
            }
    }

    private fun writeAttemptsToPrefs(wrongPinAttempts: Int) =
        prefs.write(PrefsKeys.WRONG_PIN_ATTEMPT_COUNTER, wrongPinAttempts)

    private fun writeTimeOfAttemptToPrefs() =
        prefs.write(PrefsKeys.WRONG_PIN_OCCURRENCE, System.currentTimeMillis())


    ///////////////////////////////////////////////////
    // PinAuthentication build method initialization //
    ///////////////////////////////////////////////////
    companion object {
        private var lockoutDurationInitValue = 10
        private var maxPinAttemptsInitValue = 3
        private var initOnceCounter = 0
        private var isEnabled = false

        fun setWrongPinLockoutValues(lockoutDuration: Int, pinAttempts: Int) {
            if (initOnceCounter < 1) {
                lockoutDurationInitValue = lockoutDuration
                maxPinAttemptsInitValue = pinAttempts
            }
        }

        fun initializeWrongPinLockout(enable: Boolean) {
            if (initOnceCounter < 1) {
                isEnabled = enable
                initOnceCounter++
            }
        }
    }

    fun isWrongPinLockoutEnabled(): Boolean =
        isEnabled

}