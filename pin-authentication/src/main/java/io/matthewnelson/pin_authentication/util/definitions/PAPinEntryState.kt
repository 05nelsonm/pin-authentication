package io.matthewnelson.pin_authentication.util.definitions

import androidx.annotation.IntDef

/**
 * @suppress
 * */
internal abstract class PAPinEntryState {
    @Target(AnnotationTarget.TYPE, AnnotationTarget.PROPERTY_GETTER)
    @IntDef(
        CONFIRM_PIN,
        ENABLE_PIN_SECURITY,
        IDLE,
        LOGIN,
        RESET_PIN,
        SET_PIN,
        SET_PIN_FIRST_TIME
    )
    @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
    annotation class PinEntryState

    @get:PinEntryState
    abstract var paPinEntryState: Int

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