package io.matthewnelson.pin_authentication.di.application.module

import dagger.Module
import dagger.Provides
import io.matthewnelson.pin_authentication.util.annotations.NotForPublicConsumption
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Named

/**
 * @suppress
 * */
@NotForPublicConsumption
@Module
object PACoroutineDispatchersModule {

    const val MAIN = "DISPATCHERS_MAIN"
    const val DEFAULT = "DISPATCHERS_DEFAULT"

    @Provides
    @Named(MAIN)
    fun provideDispatcherMain(): CoroutineDispatcher =
        Dispatchers.Main

    @Provides
    @Named(DEFAULT)
    fun provideDispatcherDefault(): CoroutineDispatcher =
        Dispatchers.Default

}