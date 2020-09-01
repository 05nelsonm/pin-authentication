package io.matthewnelson.pin_authentication.di.application.module

import android.app.Application
import dagger.Module
import dagger.Provides
import io.matthewnelson.encrypted_storage.Prefs
import io.matthewnelson.pin_authentication.di.application.PAApplicationScope
import javax.inject.Named

@Module
object TestPrefsModule {

    @PAApplicationScope
    @Provides
    @Named(PrefsModule.ENCRYPTED_PREFS)
    @JvmStatic
    fun providePrefsEncrypted(application: Application): Prefs =
        Prefs.createUnencrypted(PrefsModule.ENCRYPTED_PREFS, application.applicationContext)

    @PAApplicationScope
    @Provides
    @Named(PrefsModule.PREFS)
    @JvmStatic
    fun providePrefsUnencrypted(application: Application): Prefs =
        Prefs.createUnencrypted(PrefsModule.PREFS, application.applicationContext)
}