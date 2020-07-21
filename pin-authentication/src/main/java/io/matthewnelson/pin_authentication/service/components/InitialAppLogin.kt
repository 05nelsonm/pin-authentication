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
package io.matthewnelson.pin_authentication.service.components

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

/**
 * @suppress
 * */
internal class InitialAppLogin {

    ///////////////////////////////
    // Initial Application Login //
    ///////////////////////////////
    private val initialApplicationLoginSatisfied = MutableLiveData<Boolean>(false)

    fun hasInitialAppLoginBeenSatisfied(): LiveData<Boolean> =
        initialApplicationLoginSatisfied

    fun initialAppLoginIsSatisfied() {
        if (initialApplicationLoginSatisfied.value != true) {
            initialApplicationLoginSatisfied.value = true
        }
    }


    ////////////////////////
    // Post Login Process //
    ////////////////////////
    private var postLoginProcessStarted = false

    fun hasPostLoginProcessBeenStarted(): Boolean =
        postLoginProcessStarted

    fun postLoginProcessStarted() {
        postLoginProcessStarted = true
    }

}