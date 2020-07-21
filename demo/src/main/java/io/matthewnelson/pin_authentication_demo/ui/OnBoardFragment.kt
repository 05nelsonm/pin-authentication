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
