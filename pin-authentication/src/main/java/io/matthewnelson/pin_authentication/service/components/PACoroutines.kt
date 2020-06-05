package io.matthewnelson.pin_authentication.service.components

import kotlinx.coroutines.*

/**
 * @suppress
 * */
internal class PACoroutines(
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