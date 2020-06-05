package io.matthewnelson.pin_authentication.di.activity

import dagger.BindsInstance
import dagger.Subcomponent
import io.matthewnelson.pin_authentication.di.activity.module.PAActivityModule
import io.matthewnelson.pin_authentication.di.activity.module.PAActivityViewModelsModule
import io.matthewnelson.pin_authentication.ui.PinAuthenticationActivity

@PAActivityScope
@Subcomponent(
    modules = [
        PAActivityModule::class,
        PAActivityViewModelsModule::class
    ])
internal interface TestPAActivityComponent: PAActivityComponent {

    @Subcomponent.Builder
    interface Builder: PAActivityComponent.Builder {

        @BindsInstance
        override fun bindPinAuthenticationActivity(
            pinAuthenticationActivity: PinAuthenticationActivity
        ): Builder

        override fun build(): PAActivityComponent
    }

    override fun inject(pinAuthenticationActivity: PinAuthenticationActivity)
}