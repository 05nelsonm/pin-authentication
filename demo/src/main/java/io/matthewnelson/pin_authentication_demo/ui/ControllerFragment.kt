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
import io.matthewnelson.pin_authentication_demo.databinding.FragmentControllerBinding

class ControllerFragment : Fragment() {

    private companion object {
        const val PIN_CONFIRM_REQUEST_KEY_1 = "PIN_CONFIRM_REQUEST_KEY_1"
        const val PIN_CONFIRM_REQUEST_KEY_2 = "PIN_CONFIRM_REQUEST_KEY_2"

        var dragHandleIsOut = false
    }

    private val paController by lazy { PinAuthentication.Controller() }
    private lateinit var binding: FragmentControllerBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil
            .inflate(inflater, R.layout.fragment_controller, container, false)

        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            val params = binding.scrollViewControllerFeatures.layoutParams
            params.apply {
                this.width = (resources.displayMetrics.widthPixels / 2) - 24.dpToPx
            }
            binding.scrollViewControllerFeatures.layoutParams = params
        }

        if (dragHandleIsOut) {
            binding.layoutMotionController.transitionToState(R.id.motion_scene_controller_drag_handle_is_out)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observePinConfirmationRequestKey1()
        observePinConfirmationRequestKey2()
    }

    private fun observePinConfirmationRequestKey1() {
        // This button modifies itself based on the status of PIN confirmation for the
        // particular request.

        // If the user changes their PIN Security setting, all values associated with registered
        // RequestKeys will be switched to `false` (at the time PIN Security is enabled), or
        // `true` (at the time PIN Security is disabled).

        // At time of registering new RequestKeys, their values will be set to opposite of
        // PIN Security's state.

        // STEP 1: Register the RequestKey & observe the returned LiveData
        paController.registerPinConfirmationToProceedRequestKey(this, PIN_CONFIRM_REQUEST_KEY_1)
            ?.observe(viewLifecycleOwner, Observer {
                if (it) { // TRUE: User has confirmed their PIN

                    // Set button text & tint
                    binding.buttonControllerConfirmPinEx1.text = getString(R.string.button_controller_send_something)
                    binding.buttonControllerConfirmPinEx1.background
                        .setTint(binding.root.context.getColor(R.color.secondaryLightColor))

                    binding.buttonControllerConfirmPinEx1.setOnClickListener {
                        doSomething(getString(R.string.toast_append_ex1))

                        // STEP 3: Reset the value associated with the RequestKey
                        paController.resetPinConfirmationToProceedRequestKey(PIN_CONFIRM_REQUEST_KEY_1)
                    }
                } else { // FALSE: User has yet to confirm their PIN

                    // Set button text & tint
                    binding.buttonControllerConfirmPinEx1.text = getString(R.string.button_controller_confirm_pin_example_1)
                    binding.buttonControllerConfirmPinEx1.background
                        .setTint(binding.root.context.getColor(R.color.button_gray))

                    // STEP 2: Request Pin Confirmation To Proceed
                    binding.buttonControllerConfirmPinEx1.setOnClickListener {
                        paController.requestPinConfirmationToProceed(PIN_CONFIRM_REQUEST_KEY_1)
                    }
                }
            })

        // STEP 4: Unregister the RequestKey in the Activity's onDestroy(), or a
        //         Fragment's onDestroyView() _after_ the super call.
    }

    private fun observePinConfirmationRequestKey2() {
        // Alternative implementation of Pin Confirmation To Proceed which will go right
        // into doing something upon confirmation, or if PIN Security is DISABLED, will
        // simply require a click to proceed w/o confirming a PIN.

        paController.registerPinConfirmationToProceedRequestKey(this, PIN_CONFIRM_REQUEST_KEY_2)
            ?.observe(viewLifecycleOwner, Observer {
                if (it) { // TRUE: User has confirmed their PIN

                    // Set button text & tint
                    binding.buttonControllerConfirmPinEx2.text =
                        getString(R.string.button_controller_send_something)
                    binding.buttonControllerConfirmPinEx2.background
                        .setTint(binding.root.context.getColor(R.color.secondaryLightColor))

                    // If Pin Security is ENABLED, we want it to go right into doing something after
                    // the user confirms their PIN.
                    if (paController.isPinSecurityEnabled()) {
                        doSomething(getString(R.string.toast_append_ex2))

                        // Reset the value associated with the RequestKey to switch the button
                        // back to `unconfirmed` status.
                        paController.resetPinConfirmationToProceedRequestKey(
                            PIN_CONFIRM_REQUEST_KEY_2
                        )
                    } else {

                        // If PIN Security is DISABLED, we want to require a click so it doesn't
                        // just execute something automatically when the screen is created.
                        binding.buttonControllerConfirmPinEx2.setOnClickListener {
                            doSomething(getString(R.string.toast_append_ex2))

                            // Reset the value associated with the RequestKey to switch the button
                            // back to `unconfirmed` status.
                            paController.resetPinConfirmationToProceedRequestKey(
                                PIN_CONFIRM_REQUEST_KEY_2
                            )
                        }

                    }

                } else { // FALSE: User has yet to confirm their PIN

                    // Set button text & tint
                    binding.buttonControllerConfirmPinEx2.text =
                        getString(R.string.button_controller_confirm_pin_example_2)
                    binding.buttonControllerConfirmPinEx2.background
                        .setTint(binding.root.context.getColor(R.color.button_gray))

                    binding.buttonControllerConfirmPinEx2.setOnClickListener {

                        // Make request to confirm PIN for this given request key.
                        paController.requestPinConfirmationToProceed(PIN_CONFIRM_REQUEST_KEY_2)
                    }
                }
            })
    }

    override fun onResume() {
        super.onResume()
        binding.layoutMotionController.setTransitionListener(ControllerMotionLayoutListener())
    }

    override fun onDestroyView() {
        super.onDestroyView()

        // Method accepts a single string, or an array of strings.
        paController.unregisterPinConfirmationToProceedRequestKey(
            arrayOf(PIN_CONFIRM_REQUEST_KEY_1, PIN_CONFIRM_REQUEST_KEY_2)
        )
    }

    private fun doSomething(appendToMessage: String) {
        val message =
            if (paController.isPinSecurityEnabled()) {
                getString(R.string.toast_pin_enabled)
            } else {
                getString(R.string.toast_pin_disabled)
            }

        App.showToast("$message $appendToMessage")
    }

    private inner class ControllerMotionLayoutListener : MotionLayout.TransitionListener {
        override fun onTransitionTrigger(motionLayout: MotionLayout?, triggerId: Int, positive: Boolean, progress: Float) {
        }

        override fun onTransitionStarted(motionLayout: MotionLayout?, startId: Int, endId: Int) {
        }

        override fun onTransitionChange(motionLayout: MotionLayout?, startId: Int, endId: Int, progress: Float) {
        }

        override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int) {
            dragHandleIsOut = currentId == R.id.motion_scene_controller_drag_handle_is_out
        }

    }
}
