/*
* Copyright (C) 2020 Matthew Nelson
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program.  If not, see <https://www.gnu.org/licenses/>.
* */
package io.matthewnelson.pin_authentication.service.components

import io.matthewnelson.pin_authentication.util.PrefsKeys
import io.matthewnelson.pin_authentication.util.definitions.ConfirmPinStatuss.ConfirmPinStatus
import io.matthewnelson.pin_authentication.service.PinAuthentication
import io.matthewnelson.encrypted_storage.EncryptedStorage

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
                ConfirmPinStatus.WRONG_PIN
            }
            wrongPinAttempts == maxPinAttempts -> {
                ConfirmPinStatus.ONE_MORE_ATTEMPT
            }
            wrongPinAttempts > maxPinAttempts -> {
                ConfirmPinStatus.LOCKED_OUT
            }
            else -> {
                ConfirmPinStatus.WRONG_PIN
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