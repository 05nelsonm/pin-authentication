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
package io.matthewnelson.pin_authentication_demo.ui

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import io.matthewnelson.pin_authentication_demo.util.dpToPx
import io.matthewnelson.pin_authentication.service.PinAuthentication
import io.matthewnelson.pin_authentication_demo.App
import io.matthewnelson.pin_authentication_demo.R
import io.matthewnelson.pin_authentication_demo.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment() {

    private companion object {
        var dragHandleIsOut = false
    }

    private val paSettings by lazy { PinAuthentication.Settings() }
    private lateinit var binding: FragmentSettingsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil
            .inflate(inflater, R.layout.fragment_settings, container, false)

        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            val params = binding.scrollViewSettingsFeatures.layoutParams
            params.apply {
                this.width = (resources.displayMetrics.widthPixels / 2) - 24.dpToPx
            }
            binding.scrollViewSettingsFeatures.layoutParams = params
        }

        if (dragHandleIsOut) {
            binding.layoutMotionSettings.transitionToState(R.id.motion_scene_settings_drag_handle_is_out)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // PIN Security
        observePinSecurity()
        initializePinSecuritySwitch()

        // Scrambled PIN
        setScrambledPinSwitchPosition()
        initializeScrambledPinSwitch()

        // Haptic Feedback
        setHapticFeedbackSwitchPosition()
        initializeHapticFeedbackSwitch()

        // Reset PIN
        initializeResetPinButton()

        // Custom Colors
        initializeSetCustomColorsButton()
        initializeResetColorsButton()
    }

    private fun observePinSecurity() {
        paSettings.isPinSecurityEnabled()?.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                binding.switchSettingsPinSecurity.isChecked = it
                binding.buttonSettingsPinReset.isEnabled = it
            }
        })
    }

    private fun initializePinSecuritySwitch() {
        binding.switchSettingsPinSecurity.setOnClickListener {
            paSettings.enablePinSecurity(binding.switchSettingsPinSecurity.isChecked)
        }
    }

    private fun setScrambledPinSwitchPosition() {
        binding.switchSettingsScrambledPin.isChecked = paSettings.isScrambledPinEnabled()
    }

    private fun initializeScrambledPinSwitch() {
        binding.switchSettingsScrambledPin.setOnCheckedChangeListener { _, isChecked ->
            paSettings.enableScrambledPin(isChecked)
        }
    }

    private fun setHapticFeedbackSwitchPosition() {
        binding.switchSettingsHapticFeedback.isChecked = paSettings.isHapticFeedbackEnabled()
    }

    private fun initializeHapticFeedbackSwitch() {
        binding.switchSettingsHapticFeedback.setOnCheckedChangeListener { _, isChecked ->
            paSettings.enableHapticFeedback(isChecked)
        }
    }

    private fun initializeResetPinButton() {
        binding.buttonSettingsPinReset.setOnClickListener {
            paSettings.resetPin()
        }
    }

    private fun initializeSetCustomColorsButton() {
        binding.buttonSettingsSetCustomColors.setOnClickListener {
            paSettings.setCustomColors()
                .set2_ConfirmButtonBackgroundColor(R.color.pa_super_green)
                .set4_PinHintContainerColor(R.color.pa_sea_blue)
                .set6_PinPadButtonBackgroundColor(R.color.pa_sea_blue)
                .set8_ScreenBackgroundColor(R.color.pa_deep_sea_blue)
                .applyColors()
            App.showToast(getString(R.string.toast_set_custom_colors))
        }
    }

    private fun initializeResetColorsButton() {
        binding.buttonSettingsResetColors.setOnClickListener {
            paSettings.resetColorsToApplicationDefaults()
            App.showToast(getString(R.string.toast_reset_colors))
        }
    }

    override fun onResume() {
        super.onResume()
        binding.layoutMotionSettings.setTransitionListener(SettingsMotionLayoutListener())
    }

    private inner class SettingsMotionLayoutListener : MotionLayout.TransitionListener {
        override fun onTransitionTrigger(motionLayout: MotionLayout?, triggerId: Int, positive: Boolean, progress: Float) {
        }

        override fun onTransitionStarted(motionLayout: MotionLayout?, startId: Int, endId: Int) {
        }

        override fun onTransitionChange(motionLayout: MotionLayout?, startId: Int, endId: Int, progress: Float) {
        }

        override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int) {
            dragHandleIsOut = currentId == R.id.motion_scene_settings_drag_handle_is_out
        }

    }
}
