package io.matthewnelson.pin_authentication.util.definitions

import androidx.annotation.IntDef

/**
 * @suppress
 * */
internal abstract class ConfirmPinStatuss {
    @Target(AnnotationTarget.TYPE, AnnotationTarget.PROPERTY_GETTER)
    @IntDef(
        ConfirmPinStatus.CORRECT_PIN,
        ConfirmPinStatus.LOCKED_OUT,
        ConfirmPinStatus.ONE_MORE_ATTEMPT,
        ConfirmPinStatus.WRONG_PIN
    )
    @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
    annotation class ConfirmPinStatus {
        companion object {
            const val CORRECT_PIN = 0
            const val LOCKED_OUT = 1
            const val ONE_MORE_ATTEMPT = 2
            const val WRONG_PIN = 3
        }
    }
}