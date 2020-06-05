package io.matthewnelson.pin_authentication.di.activity

import dagger.BindsInstance
import dagger.Subcomponent
import io.matthewnelson.pin_authentication.di.activity.module.PAActivityModule
import io.matthewnelson.pin_authentication.di.activity.module.PAActivityViewModelsModule
import io.matthewnelson.pin_authentication.ui.PinAuthenticationActivity

/**
 * @suppress
 * */
@PAActivityScope
@Subcomponent(
    modules = [
        PAActivityModule::class,
        PAActivityViewModelsModule::class
    ])
internal interface PAActivityComponent {

    @Subcomponent.Builder
    interface Builder {

        @BindsInstance
        fun bindPinAuthenticationActivity(
            pinAuthenticationActivity: PinAuthenticationActivity
        ): Builder

        fun build(): PAActivityComponent
    }

    fun inject(pinAuthenticationActivity: PinAuthenticationActivity)
}