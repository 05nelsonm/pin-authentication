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
package io.matthewnelson.pin_authentication.util.definitions

import androidx.annotation.IntDef

/**
 * @suppress
 * */
internal abstract class PinEntryStates {
    @Target(AnnotationTarget.TYPE, AnnotationTarget.PROPERTY_GETTER)
    @IntDef(
        PinEntryState.CONFIRM_PIN,
        PinEntryState.ENABLE_PIN_SECURITY,
        PinEntryState.IDLE,
        PinEntryState.LOGIN,
        PinEntryState.RESET_PIN,
        PinEntryState.SET_PIN,
        PinEntryState.SET_PIN_FIRST_TIME
    )
    @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
    annotation class PinEntryState {
        companion object {
            const val CONFIRM_PIN = 0
            const val ENABLE_PIN_SECURITY = 1
            const val IDLE = 2
            const val LOGIN = 3
            const val RESET_PIN = 4
            const val SET_PIN = 5
            const val SET_PIN_FIRST_TIME = 6
        }
    }
}