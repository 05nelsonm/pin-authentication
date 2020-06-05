package io.matthewnelson.pin_authentication.ui

import android.content.Intent
import android.os.Bundle
import android.view.HapticFeedbackConstants
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import io.matthewnelson.pin_authentication.R
import io.matthewnelson.pin_authentication.databinding.ActivityPinAuthenticationBinding
import io.matthewnelson.pin_authentication.service.PinAuthentication
import io.matthewnelson.pin_authentication.util.definitions.PAPinEntryState
import io.matthewnelson.pin_authentication.viewmodel.PAActivityViewModelFactory
import javax.inject.Inject

/**
 * @suppress
 * */
internal class PinAuthenticationActivity : AppCompatActivity() {

    private fun injectPinAuthenticationActivity() {
        PinAuthentication.PinAuthenticationActivityInjection(this).inject()
    }

    @Inject
    lateinit var paActivityViewModelFactory: PAActivityViewModelFactory
    private lateinit var binding: ActivityPinAuthenticationBinding
    private lateinit var viewModel: PinAuthenticationActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectPinAuthenticationActivity()
        viewModel = ViewModelProvider(this, paActivityViewModelFactory)
            .get(PinAuthenticationActivityViewModel::class.java)

        viewModel.cancelProtectUserDataJobIfNotComplete()

        supportActionBar?.hide()

        if (!viewModel.getBuildConfigDebug()) {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE
            )
        }

        viewModel.getHapticFeedback().observe(this, Observer {
            when (it) {
                null -> {}
                else -> {
                    window.decorView.performHapticFeedback(
                        HapticFeedbackConstants.LONG_PRESS,
                        HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING
                    )
                }
            }
        })

        viewModel.getPinEntryState().observe(this, Observer {
            when (it) {
                PAPinEntryState.CONFIRM_PIN -> {
                    viewModel.setPinEntryStateConfirmPin()
                }
                PAPinEntryState.ENABLE_PIN_SECURITY -> {
                    viewModel.setPinEntryStateSetPin(it)
                }
                PAPinEntryState.LOGIN -> {
                    viewModel.setPinEntryStateLogin()
                }
                PAPinEntryState.RESET_PIN -> {
                    viewModel.setPinEntryStateResetPin()
                }
                PAPinEntryState.SET_PIN -> {
                    viewModel.setPinEntryStateSetPin(it)
                }
                PAPinEntryState.SET_PIN_FIRST_TIME -> {
                    viewModel.setPinEntryStateSetPin(it)
                }
                else -> {
                    viewModel.setPinEntryStateIdle()
                }
            }
        })

        binding = DataBindingUtil.setContentView(this, R.layout.activity_pin_authentication)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
    }

    override fun onBackPressed() {
        when (viewModel.getCurrentPinEntryState()) {
            PAPinEntryState.LOGIN, PAPinEntryState.SET_PIN_FIRST_TIME -> {
                val intent = Intent(Intent.ACTION_MAIN)
                intent.addCategory(Intent.CATEGORY_HOME)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(intent)
            }
            else -> {
                viewModel.onBackPressed()
                super.onBackPressed()
            }
        }
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        viewModel.launchProtectUserDataJobIfInactive()
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
    }

    override fun onRestart() {
        super.onRestart()
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}
