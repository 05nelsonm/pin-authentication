package io.matthewnelson.pin_authentication.service.components

import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.matthewnelson.pin_authentication.util.annotations.NotForPublicConsumption
import io.matthewnelson.pin_authentication.util.definitions.PAScreenType
import kotlinx.coroutines.*

/**
 * @suppress
 * */
@NotForPublicConsumption
class PAConfirmPinToProceed(
    private val paAppLifecycleWatcher: PAAppLifecycleWatcher,
    private val paCoroutines: PACoroutines
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
        screenType: @PAScreenType.ScreenType Int = PAScreenType.NONE,
        fragmentClassName: String? = null,
        @IdRes fragmentID: Int? = null,
        addToBlacklist: Boolean = false
    ): LiveData<Boolean>? {

        if (!mapRequestKeys.containsKey(requestKey)) {
            mapRequestKeys[requestKey] =
                RequestKeyData(
                    screenType,
                    paAppLifecycleWatcher.getCurrentActivityName(),
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
        val screenType: @PAScreenType.ScreenType Int,
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
        setAllRequestKeysToJob = paCoroutines.getScopeUI().launch {
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
        if (mapRequestKeys[requestKey]?.screenType == PAScreenType.NONE) {
            requestKeysToBeUnregistered.add(requestKey)
            launchRequestKeyRemovalJobIfInactive()
        } else

            if (!paAppLifecycleWatcher.isScreenRotationOccurring() &&
                !paAppLifecycleWatcher.isCurrentActivityPinAuthenticationActivity()
            ) {
                requestKeysToBeUnregistered.add(requestKey)
                launchRequestKeyRemovalJobIfInactive()
            }

    }

    private fun launchRequestKeyRemovalJobIfInactive() {
        if (!::requestKeyRemovalJob.isInitialized || !requestKeyRemovalJob.isActive) {

            requestKeyRemovalJob = paCoroutines.getScopeDefault().launch {
                removeRequestKeys()
            }
        }
    }

    private fun removeRequestKeys() {
        while (requestKeysToBeUnregistered.size > 0) {

            val requestKey = requestKeysToBeUnregistered[0]
            var remove = false

            when (mapRequestKeys[requestKey]?.screenType) {
                PAScreenType.ACTIVITY -> {
                    val activityClassName = mapRequestKeys[requestKey]?.activityClassName

                    if (activityClassName != paAppLifecycleWatcher.getCurrentActivityName()) {
                        remove = true
                    }
                }
                PAScreenType.FRAGMENT -> {
                    val activityClassName = mapRequestKeys[requestKey]?.activityClassName

                    if (activityClassName != paAppLifecycleWatcher.getCurrentActivityName()) {
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
            paAppLifecycleWatcher.getCurrentActivity()?.apply {
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