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
package io.matthewnelson.pin_authentication.di.application.module

import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Named

@Module
internal object CoroutineDispatchersModule {

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