package io.matthewnelson.pin_authentication.util.definitions

import androidx.annotation.IntDef

/**
 * @suppress
 * */
internal abstract class LockApplicationEvents {
    @Target(AnnotationTarget.TYPE, AnnotationTarget.PROPERTY_GETTER)
    @IntDef(
        LockApplicationEvent.LOCK,
        LockApplicationEvent.UNLOCK
    )
    @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
    annotation class LockApplicationEvent {
        companion object {
            const val LOCK = 0
            const val UNLOCK = 1
        }
    }
}