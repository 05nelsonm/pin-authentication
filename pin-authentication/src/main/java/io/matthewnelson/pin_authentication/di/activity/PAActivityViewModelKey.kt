package io.matthewnelson.pin_authentication.di.activity

import androidx.lifecycle.ViewModel
import dagger.MapKey
import kotlin.reflect.KClass

/**
 * @suppress
 * */
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
@MapKey
@Retention(AnnotationRetention.RUNTIME)
internal annotation class PAActivityViewModelKey(val value: KClass<out ViewModel>)