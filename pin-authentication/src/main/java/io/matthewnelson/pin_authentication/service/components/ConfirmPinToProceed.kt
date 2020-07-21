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

import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.matthewnelson.pin_authentication.util.definitions.ScreenTypes.ScreenType
import kotlinx.coroutines.*

/**
 * @suppress
 * */
internal class ConfirmPinToProceed(
    private val appLifecycleWatcher: AppLifecycleWatcher,
    private val coroutines: Coroutines
) {

    /////////////////////////////
    // Registering Request Key //
    /////////////////////////////
    private val mapRequestKeys = mutableMapOf<String, RequestKeyData>()
    private val blacklistedRequestKeys = mutableListOf<String>()

    fun isRequestKeyBlacklisted(requestKey: String): Boolean =
        blacklistedRequestKeys.contains(requestKey)

    fun isRequestKeyRegistered(requestKey: String): Boolean =
        mapRequestKeys.containsKey(requestKey)

    fun registerRequestKey(
        requestKey: String,
        value: Boolean,
        screenType: @ScreenType Int = ScreenType.NONE,
        fragmentClassName: String? = null,
        @IdRes fragmentID: Int? = null,
        addToBlacklist: Boolean = false
    ): LiveData<Boolean>? {

        if (!mapRequestKeys.containsKey(requestKey)) {
            mapRequestKeys[requestKey] =
                RequestKeyData(
                    screenType,
                    appLifecycleWatcher.getCurrentActivityName(),
                    fragmentClassName,
                    fragmentID,
                    MutableLiveData(value)
                )
            if (addToBlacklist) {
                blacklistedRequestKeys.add(requestKey)
            }
        }
        return mapRequestKeys[requestKey]?.liveData
    }

    private inner class RequestKeyData(
        val screenType: @ScreenType Int,
        val activityClassName: String,
        val fragmentClassName: String?,
        @IdRes val fragmentID: Int?,
        val liveData: MutableLiveData<Boolean>
    )


    /////////////////////////
    // Modify Request Keys //
    /////////////////////////
    private lateinit var setAllRequestKeysToJob: Job
    private var currentRequestKey = ""

    fun getCurrentRequestKey(): String =
        currentRequestKey

    fun setCurrentRequestKey(requestKey: String) {
        currentRequestKey = requestKey
    }

    fun getValueOfRequestKey(requestKey: String): LiveData<Boolean>? =
        mapRequestKeys[requestKey]?.liveData

    fun setAllRequestKeysTo(value: Boolean, excludeBlacklisted: Boolean = true) {
        if (!::setAllRequestKeysToJob.isInitialized) {
            launchSetAllRequestKeysToJob(value, excludeBlacklisted)
        } else if (setAllRequestKeysToJob.isActive) {
            setAllRequestKeysToJob.cancel()
            launchSetAllRequestKeysToJob(value, excludeBlacklisted)
        } else {
            launchSetAllRequestKeysToJob(value, excludeBlacklisted)
        }
    }

    private fun launchSetAllRequestKeysToJob(value: Boolean, excludeBlacklisted: Boolean) {
        setAllRequestKeysToJob = coroutines.getScopeUI().launch {
            setAllRequestKeys(value, excludeBlacklisted)
        }
    }

    private fun setAllRequestKeys(value: Boolean, excludeBlacklisted: Boolean) {
        val mapCopy = mapRequestKeys
        mapCopy.forEach {
            if (excludeBlacklisted) {
                if (!isRequestKeyBlacklisted(it.key)) {
                    mapRequestKeys[it.key]?.liveData?.value = value
                }
            } else {
                mapRequestKeys[it.key]?.liveData?.value = value
            }
        }
    }

    fun setRequestKeyValueOfCurrentRequestKeyTo(value: Boolean) {
        mapRequestKeys[currentRequestKey]?.liveData?.value = value
    }

    fun setRequestKeyValueTo(requestKey: String, value: Boolean) {
        mapRequestKeys[requestKey]?.liveData?.value = value
    }


    ////////////////////////////////
    // Unregistering Request Keys //
    ////////////////////////////////
    private lateinit var requestKeyRemovalJob: Job
    private val requestKeysToBeUnregistered = mutableListOf<String>()

    fun unregisterRequestKey(requestKey: String) {
        if (mapRequestKeys[requestKey]?.screenType == ScreenType.NONE) {
            requestKeysToBeUnregistered.add(requestKey)
            launchRequestKeyRemovalJobIfInactive()
        } else

            if (!appLifecycleWatcher.isScreenRotationOccurring() &&
                !appLifecycleWatcher.isCurrentActivityPinAuthenticationActivity()
            ) {
                requestKeysToBeUnregistered.add(requestKey)
                launchRequestKeyRemovalJobIfInactive()
            }

    }

    private fun launchRequestKeyRemovalJobIfInactive() {
        if (!::requestKeyRemovalJob.isInitialized || !requestKeyRemovalJob.isActive) {

            requestKeyRemovalJob = coroutines.getScopeDefault().launch {
                removeRequestKeys()
            }
        }
    }

    private fun removeRequestKeys() {
        while (requestKeysToBeUnregistered.size > 0) {

            val requestKey = requestKeysToBeUnregistered[0]
            var remove = false

            when (mapRequestKeys[requestKey]?.screenType) {
                ScreenType.ACTIVITY -> {
                    val activityClassName = mapRequestKeys[requestKey]?.activityClassName

                    if (activityClassName != appLifecycleWatcher.getCurrentActivityName()) {
                        remove = true
                    }
                }
                ScreenType.FRAGMENT -> {
                    val activityClassName = mapRequestKeys[requestKey]?.activityClassName

                    if (activityClassName != appLifecycleWatcher.getCurrentActivityName()) {
                        remove = true
                    } else {
                        val fragmentId = mapRequestKeys[requestKey]?.fragmentID
                        val fragmentClassName = mapRequestKeys[requestKey]?.fragmentClassName

                        if (!isFragmentActive(fragmentId, fragmentClassName)) {
                            remove = true
                        }
                    }
                }
                else -> {
                    remove = true
                }
            }

            if (remove) {
                mapRequestKeys.remove(requestKey)
                blacklistedRequestKeys.remove(requestKey)
            }

            requestKeysToBeUnregistered.removeAt(0)
        }
    }

    private fun isFragmentActive(@IdRes fragmentID: Int?, fragmentClassName: String?): Boolean {
        if (fragmentID != null && fragmentClassName != null) {
            appLifecycleWatcher.getCurrentActivity()?.apply {
                (this as AppCompatActivity).supportFragmentManager.findFragmentById(fragmentID)
                    ?.childFragmentManager?.fragments?.forEach { fragment ->
                    if (fragment.javaClass.name == fragmentClassName && !fragment.isDetached) {
                        return true
                    }
                }
            }
        }
        return false
    }

}