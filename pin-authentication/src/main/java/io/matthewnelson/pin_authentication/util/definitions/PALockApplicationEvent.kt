package io.matthewnelson.pin_authentication.util.definitions

import androidx.annotation.IntDef
import io.matthewnelson.pin_authentication.util.annotations.NotForPublicConsumption

/**
 * @suppress
 * */
@NotForPublicConsumption
abstract class PALockApplicationEvent {
    @Target(AnnotationTarget.TYPE, AnnotationTarget.PROPERTY_GETTER)
    @IntDef(
        LOCK,
        UNLOCK
    )
    @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
    annotation class LockApplicationEvent

    @get:LockApplicationEvent
    abstract var paLockApplicationEvent: Int

    companion object {
        const val LOCK = 0
        const val UNLOCK = 1
    }
}