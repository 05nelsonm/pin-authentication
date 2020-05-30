package io.matthewnelson.pin_authentication.util.definitions

import androidx.annotation.IntDef
import io.matthewnelson.pin_authentication.util.annotations.NotForPublicConsumption

/**
 * @suppress
 * */
@NotForPublicConsumption
abstract class PAAuthenticationState {
    @Target(AnnotationTarget.TYPE, AnnotationTarget.PROPERTY_GETTER)
    @IntDef(
        REQUIRED,
        NOT_REQUIRED
    )
    @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
    annotation class AuthenticationState

    @get:AuthenticationState
    abstract var paAuthenticationState: Int

    companion object {
        const val REQUIRED = 0
        const val NOT_REQUIRED = 1
    }
}