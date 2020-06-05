package io.matthewnelson.pin_authentication.di.application.module

import android.app.Application
import dagger.Module
import dagger.Provides
import io.matthewnelson.pin_authentication.di.application.PAApplicationScope
import io.matthewnelson.encrypted_storage.EncryptedStorage
import javax.inject.Named

/**
 * @suppress
 * */
@Module
internal object PrefsModule {

    private const val repoName = "io.matthewnelson.pin-authentication"
    const val ENCRYPTED_PREFS = "$repoName.ENCRYPTED_PREFS"
    const val PREFS = "$repoName.PREFS"

    @PAApplicationScope
    @Provides
    @Named(ENCRYPTED_PREFS)
    @JvmStatic
    fun providePrefsEncrypted(application: Application): EncryptedStorage.Prefs =
        EncryptedStorage.Prefs.createEncrypted(ENCRYPTED_PREFS, application.applicationContext)

    @PAApplicationScope
    @Provides
    @Named(PREFS)
    @JvmStatic
    fun providePrefsUnencrypted(application: Application): EncryptedStorage.Prefs =
        EncryptedStorage.Prefs.createUnencrypted(PREFS, application.applicationContext)
}