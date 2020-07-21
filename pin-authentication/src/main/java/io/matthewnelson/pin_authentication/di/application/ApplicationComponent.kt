/*
* Copyright (C) 2020 Matthew Nelson
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program.  If not, see <https://www.gnu.org/licenses/>.
* */
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