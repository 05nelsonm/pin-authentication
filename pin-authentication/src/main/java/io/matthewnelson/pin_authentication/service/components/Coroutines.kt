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

import kotlinx.coroutines.*

internal class Coroutines(
    private val dispatcherDefault: CoroutineDispatcher,
    private val dispatcherMain: CoroutineDispatcher
) {

    private var supervisorJob = SupervisorJob()
    private var scopeDefault = CoroutineScope(dispatcherDefault + supervisorJob)
    private var scopeUI = CoroutineScope(dispatcherMain + supervisorJob)

    fun getDispatcherDefault(): CoroutineDispatcher =
        dispatcherDefault

    fun getDispatcherMain(): CoroutineDispatcher =
        dispatcherMain

    fun getSupervisorJob(): Job =
        supervisorJob

    fun getScopeDefault(): CoroutineScope =
        scopeDefault

    fun getScopeUI(): CoroutineScope =
        scopeUI

}