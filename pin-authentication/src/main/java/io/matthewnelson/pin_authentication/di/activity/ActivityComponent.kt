package io.matthewnelson.pin_authentication.di.activity

import dagger.BindsInstance
import dagger.Subcomponent
import io.matthewnelson.pin_authentication.di.activity.module.ActivityModule
import io.matthewnelson.pin_authentication.di.activity.module.ActivityViewModelsModule
import io.matthewnelson.pin_authentication.ui.PinAuthenticationActivity

/**
 * @suppress
 * */
@PAActivityScope
@Subcomponent(
    modules = [
        ActivityModule::class,
        ActivityViewModelsModule::class
    ])
internal interface ActivityComponent {

    @Subcomponent.Builder
    interface Builder {

        @BindsInstance
        fun bindPinAuthenticationActivity(
            pinAuthenticationActivity: PinAuthenticationActivity
        ): Builder

        fun build(): ActivityComponent
    }

    fun inject(pinAuthenticationActivity: PinAuthenticationActivity)
}