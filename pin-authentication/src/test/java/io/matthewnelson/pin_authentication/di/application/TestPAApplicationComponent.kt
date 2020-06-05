package io.matthewnelson.pin_authentication.di.application

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import io.matthewnelson.pin_authentication.di.PAInjection
import io.matthewnelson.pin_authentication.di.activity.TestPAActivityComponent
import io.matthewnelson.pin_authentication.di.application.module.PAApplicationModule
import io.matthewnelson.pin_authentication.di.application.module.PACoroutineDispatchersModule
import io.matthewnelson.pin_authentication.di.application.module.TestPAPrefsModule

@PAApplicationScope
@Component(
    modules = [
        PAApplicationModule::class,
        PACoroutineDispatchersModule::class,
        TestPAPrefsModule::class
    ]
)
internal interface TestPAApplicationComponent: PAApplicationComponent {

    override fun getPAActivityComponentBuilder(): TestPAActivityComponent.Builder

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun bindApplication(application: Application): Builder

        fun build(): TestPAApplicationComponent
    }

    override fun inject(paInjection: PAInjection)

}