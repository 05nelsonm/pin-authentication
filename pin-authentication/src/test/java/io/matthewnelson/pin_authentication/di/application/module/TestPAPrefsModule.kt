package io.matthewnelson.pin_authentication.di.application.module

import android.app.Application
import dagger.Module
import dagger.Provides
import io.matthewnelson.encrypted_storage.EncryptedStorage
import io.matthewnelson.pin_authentication.di.application.PAApplicationScope
import io.matthewnelson.pin_authentication.util.annotations.NotForPublicConsumption
import javax.inject.Named

@OptIn(NotForPublicConsumption::class)
@Module
object TestPAPrefsModule {

    @PAApplicationScope
    @Provides
    @Named(PAPrefsModule.ENCRYPTED_PREFS)
    @JvmStatic
    fun provideDualPrefsEncrypted(application: Application): EncryptedStorage.Prefs =
        EncryptedStorage.Prefs.createUnencrypted(PAPrefsModule.ENCRYPTED_PREFS, application.applicationContext)

    @PAApplicationScope
    @Provides
    @Named(PAPrefsModule.PREFS)
    @JvmStatic
    fun provideDualPrefs(application: Application): EncryptedStorage.Prefs =
        EncryptedStorage.Prefs.createUnencrypted(PAPrefsModule.PREFS, application.applicationContext)
}