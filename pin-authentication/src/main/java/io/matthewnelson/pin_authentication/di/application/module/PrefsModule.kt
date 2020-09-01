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

import android.app.Application
import dagger.Module
import dagger.Provides
import io.matthewnelson.pin_authentication.di.application.PAApplicationScope
import io.matthewnelson.encrypted_storage.Prefs
import javax.inject.Named

@Module
internal object PrefsModule {

    private const val repoName = "io.matthewnelson.pin-authentication"
    const val ENCRYPTED_PREFS = "$repoName.ENCRYPTED_PREFS"
    const val PREFS = "$repoName.PREFS"

    @PAApplicationScope
    @Provides
    @Named(ENCRYPTED_PREFS)
    @JvmStatic
    fun providePrefsEncrypted(application: Application): Prefs =
        Prefs.createEncrypted(ENCRYPTED_PREFS, application.applicationContext)

    @PAApplicationScope
    @Provides
    @Named(PREFS)
    @JvmStatic
    fun providePrefsUnencrypted(application: Application): Prefs =
        Prefs.createUnencrypted(PREFS, application.applicationContext)
}