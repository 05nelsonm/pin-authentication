package io.matthewnelson.pin_authentication.util.definitions

import androidx.annotation.IntDef

/**
 * @suppress
 * */
internal abstract class AuthenticationStates {
    @Target(AnnotationTarget.TYPE, AnnotationTarget.PROPERTY_GETTER)
    @IntDef(
        AuthenticationState.REQUIRED,
        AuthenticationState.NOT_REQUIRED
    )
    @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
    annotation class AuthenticationState {
        companion object {
            const val REQUIRED = 0
            const val NOT_REQUIRED = 1
        }
    }
}