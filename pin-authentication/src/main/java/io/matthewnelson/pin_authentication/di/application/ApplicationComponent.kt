package io.matthewnelson.pin_authentication.di.application

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import io.matthewnelson.pin_authentication.di.CompanionInjection
import io.matthewnelson.pin_authentication.di.activity.ActivityComponent
import io.matthewnelson.pin_authentication.di.application.module.ApplicationModule
import io.matthewnelson.pin_authentication.di.application.module.CoroutineDispatchersModule
import io.matthewnelson.pin_authentication.di.application.module.PrefsModule

/**
 * @suppress
 * */
@PAApplicationScope
@Component(
    modules = [
        ApplicationModule::class,
        CoroutineDispatchersModule::class,
        PrefsModule::class
    ])
internal interface ApplicationComponent {

    fun getPAActivityComponentBuilder(): ActivityComponent.Builder

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun bindApplication(application: Application): Builder

        fun build(): ApplicationComponent
    }

    fun inject(companionInjection: CompanionInjection)
}