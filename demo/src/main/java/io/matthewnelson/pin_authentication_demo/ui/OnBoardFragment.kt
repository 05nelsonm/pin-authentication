package io.matthewnelson.pin_authentication_demo.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.databinding.DataBindingUtil
import io.matthewnelson.pin_authentication.service.PinAuthentication
import io.matthewnelson.pin_authentication_demo.R
import io.matthewnelson.pin_authentication_demo.databinding.FragmentOnBoardBinding

class OnBoardFragment : Fragment() {

    private companion object {
        var currentConstraintSetId = -1
    }

    private lateinit var binding: FragmentOnBoardBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil
            .inflate(inflater, R.layout.fragment_on_board, container, false)

        if (binding.layoutMotionOnBoard.constraintSetIds.contains(currentConstraintSetId)) {
            binding.layoutMotionOnBoard.transitionToState(currentConstraintSetId)
        }

        initializeOnBoardFinishButton()

        return binding.root
    }

    private fun initializeOnBoardFinishButton() {
        binding.buttonOnBoardNext.setOnClickListener {
            PinAuthentication.Controller().completeOnBoardProcess()
            resetMotionLayout()
        }
    }

    private fun resetMotionLayout() {
        currentConstraintSetId = -1
    }

    override fun onResume() {
        super.onResume()
        binding.layoutMotionOnBoard.setTransitionListener(OnBoardMotionLayoutListener())
    }

    private inner class OnBoardMotionLayoutListener : MotionLayout.TransitionListener {
        override fun onTransitionTrigger(motionLayout: MotionLayout?, triggerId: Int, positive: Boolean, progress: Float) {
        }

        override fun onTransitionStarted(motionLayout: MotionLayout?, startId: Int, endId: Int) {
        }

        override fun onTransitionChange(motionLayout: MotionLayout?, startId: Int, endId: Int, progress: Float) {
        }

        override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int) {
            currentConstraintSetId = currentId
        }

    }
}
