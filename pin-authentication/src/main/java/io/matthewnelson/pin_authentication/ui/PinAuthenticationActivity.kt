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
import io.matthewnelson.pin_authentication.util.definitions.PinEntryStates.PinEntryState
import io.matthewnelson.pin_authentication.viewmodel.ActivityViewModelFactory
import javax.inject.Inject

class PinAuthenticationActivity internal constructor() : AppCompatActivity() {

    private fun injectPinAuthenticationActivity() {
        PinAuthentication.PinAuthenticationActivityInjection(this).inject()
    }

    @Inject
    internal lateinit var activityViewModelFactory: ActivityViewModelFactory
    private lateinit var binding: ActivityPinAuthenticationBinding
    private lateinit var viewModel: PinAuthenticationActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectPinAuthenticationActivity()
        viewModel = ViewModelProvider(this, activityViewModelFactory)
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
                PinEntryState.CONFIRM_PIN -> {
                    viewModel.setPinEntryStateConfirmPin()
                }
                PinEntryState.ENABLE_PIN_SECURITY -> {
                    viewModel.setPinEntryStateSetPin(it)
                }
                PinEntryState.LOGIN -> {
                    viewModel.setPinEntryStateLogin()
                }
                PinEntryState.RESET_PIN -> {
                    viewModel.setPinEntryStateResetPin()
                }
                PinEntryState.SET_PIN -> {
                    viewModel.setPinEntryStateSetPin(it)
                }
                PinEntryState.SET_PIN_FIRST_TIME -> {
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
            PinEntryState.LOGIN, PinEntryState.SET_PIN_FIRST_TIME -> {
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
