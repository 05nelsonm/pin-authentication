package io.matthewnelson.pin_authentication.di.activity

import dagger.BindsInstance
import dagger.Subcomponent
import io.matthewnelson.pin_authentication.di.activity.module.ActivityModule
import io.matthewnelson.pin_authentication.di.activity.module.ActivityViewModelsModule
import io.matthewnelson.pin_authentication.ui.PinAuthenticationActivity

@PAActivityScope
@Subcomponent(
    modules = [
        ActivityModule::class,
        ActivityViewModelsModule::class
    ])
internal interface TestActivityComponent: ActivityComponent {

    @Subcomponent.Builder
    interface Builder: ActivityComponent.Builder {

        @BindsInstance
        override fun bindPinAuthenticationActivity(
            pinAuthenticationActivity: PinAuthenticationActivity
        ): Builder

        override fun build(): ActivityComponent
    }

    override fun inject(pinAuthenticationActivity: PinAuthenticationActivity)
}