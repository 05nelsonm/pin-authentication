package io.matthewnelson.pin_authentication.util.definitions

import androidx.annotation.IntDef

/**
 * @suppress
 * */
internal abstract class PAConfirmPinStatus {
    @Target(AnnotationTarget.TYPE, AnnotationTarget.PROPERTY_GETTER)
    @IntDef(
        CORRECT_PIN,
        LOCKED_OUT,
        ONE_MORE_ATTEMPT,
        WRONG_PIN
    )
    @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
    annotation class ConfirmPinStatus

    @get:ConfirmPinStatus
    abstract var paConfirmPinStatus: Int

    companion object {
        const val CORRECT_PIN = 0
        const val LOCKED_OUT = 1
        const val ONE_MORE_ATTEMPT = 2
        const val WRONG_PIN = 3
    }
}