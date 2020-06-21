package io.matthewnelson.pin_authentication.util.definitions

import androidx.annotation.IntDef

/**
 * @suppress
 * */
internal abstract class ScreenTypes {
    @Target(AnnotationTarget.TYPE, AnnotationTarget.PROPERTY_GETTER)
    @IntDef(
        ScreenType.ACTIVITY,
        ScreenType.FRAGMENT,
        ScreenType.NONE
    )
    @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
    annotation class ScreenType {
        companion object {
            const val ACTIVITY = 0
            const val FRAGMENT = 1
            const val NONE = 2
        }
    }
}