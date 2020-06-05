package io.matthewnelson.pin_authentication.service

import android.app.Application
import androidx.test.core.app.ApplicationProvider
import io.matthewnelson.pin_authentication.BuildConfig
import io.matthewnelson.pin_authentication.di.application.DaggerTestPAApplicationComponent
import io.matthewnelson.pin_authentication.di.PAInjection
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
    private lateinit var paInjection: PAInjection
    private lateinit var paBuilder: PinAuthentication.Builder.PABuilder

    @Before
    fun setup() {

        val component = DaggerTestPAApplicationComponent.builder()
            .bindApplication(app)
            .build()

        paInjection = PAInjection(component)
        paBuilder = PinAuthentication.Builder().testing(component, paInjection, BuildConfig.DEBUG)
    }

    @Test
    fun `Testing if DaggerTestPAApplicationComponent is working because IDE is complaining`() {
        paBuilder.enablePinSecurityByDefault()
            .build()
        assertEquals(paSettings.isPinSecurityEnabled()?.value, true) // Passes (expected)
//        assertEquals(amSettings.isPinSecurityEnabled()?.value, false) // Fails (expected)
    }

}