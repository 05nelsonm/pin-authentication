package io.matthewnelson.pin_authentication.util.definitions

import androidx.annotation.IntDef

/**
 * @suppress
 * */
internal abstract class PAScreenType {
    @Target(AnnotationTarget.TYPE, AnnotationTarget.PROPERTY_GETTER)
    @IntDef(
        ACTIVITY,
        FRAGMENT,
        NONE
    )
    @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
    annotation class ScreenType

    @get:ScreenType
    abstract var paScreenType: Int

    companion object {
        const val ACTIVITY = 0
        const val FRAGMENT = 1
        const val NONE = 2
    }
}