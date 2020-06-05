package io.matthewnelson.pin_authentication.di.application

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import io.matthewnelson.pin_authentication.di.CompanionInjection
import io.matthewnelson.pin_authentication.di.activity.TestActivityComponent
import io.matthewnelson.pin_authentication.di.application.module.ApplicationModule
import io.matthewnelson.pin_authentication.di.application.module.CoroutineDispatchersModule
import io.matthewnelson.pin_authentication.di.application.module.TestPrefsModule

@PAApplicationScope
@Component(
    modules = [
        ApplicationModule::class,
        CoroutineDispatchersModule::class,
        TestPrefsModule::class
    ]
)
internal interface TestApplicationComponent: ApplicationComponent {

    override fun getPAActivityComponentBuilder(): TestActivityComponent.Builder

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun bindApplication(application: Application): Builder

        fun build(): TestApplicationComponent
    }

    override fun inject(companionInjection: CompanionInjection)

}