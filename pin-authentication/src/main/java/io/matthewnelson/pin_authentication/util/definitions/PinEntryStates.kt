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