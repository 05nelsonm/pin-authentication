package io.matthewnelson.pin_authentication.di.application.module

import android.app.Application
import dagger.Module
import dagger.Provides
import io.matthewnelson.pin_authentication.di.application.PAApplicationScope
import io.matthewnelson.encrypted_storage.EncryptedStorage
import io.matthewnelson.pin_authentication.util.annotations.NotForPublicConsumption
import javax.inject.Named

/**
 * @suppress
 * */
@NotForPublicConsumption
@Module
object PAPrefsModule {

    private const val repoName = "io.matthewnelson.pin-authentication"
    const val ENCRYPTED_PREFS = "$repoName.ENCRYPTED_PREFS"
    const val PREFS = "$repoName.PREFS"

    @PAApplicationScope
    @Provides
    @Named(ENCRYPTED_PREFS)
    @JvmStatic
    fun provideDualPrefsEncrypted(application: Application): EncryptedStorage.Prefs =
        EncryptedStorage.Prefs.createEncrypted(ENCRYPTED_PREFS, application.applicationContext)

    @PAApplicationScope
    @Provides
    @Named(PREFS)
    @JvmStatic
    fun provideDualPrefs(application: Application): EncryptedStorage.Prefs =
        EncryptedStorage.Prefs.createUnencrypted(PREFS, application.applicationContext)
}