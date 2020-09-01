package io.matthewnelson.pin_authentication.service

import android.app.Application
import androidx.test.core.app.ApplicationProvider
import io.matthewnelson.pin_authentication.BuildConfig
import io.matthewnelson.pin_authentication.di.application.DaggerTestApplicationComponent
import io.matthewnelson.pin_authentication.di.CompanionInjection
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import kotlin.test.assertEquals

@Config(minSdk = 23, maxSdk = 23)
@RunWith(RobolectricTestRunner::class)
class PinAuthenticationUnitTest {

    private val app: Application by lazy {
        ApplicationProvider.getApplicationContext() as Application
    }
    private val paController: PinAuthentication.Controller by lazy {
        PinAuthentication.Controller()
    }
    private val paSettings: PinAuthentication.Settings by lazy {
        PinAuthentication.Settings()
    }
    private lateinit var companionInjection: CompanionInjection
    private lateinit var optionsBuilder: PinAuthentication.Builder.OptionsBuilder

    @Before
    fun setup() {

        val component = DaggerTestApplicationComponent.builder()
            .bindApplication(app)
            .build()

        companionInjection = CompanionInjection(component)
        optionsBuilder = PinAuthentication.Builder().testing(component, companionInjection, BuildConfig.DEBUG)
    }

    @Test
    fun `Testing if DaggerTestApplicationComponent is working because IDE is complaining`() {
        optionsBuilder.enablePinSecurityByDefault()
            .build()
        assertEquals(paSettings.isPinSecurityEnabled()?.value, true) // Passes (expected)
//        assertEquals(paSettings.isPinSecurityEnabled()?.value, false) // Fails (expected)
    }

}