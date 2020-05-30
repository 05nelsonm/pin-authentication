package io.matthewnelson.pin_authentication.service.components

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.matthewnelson.pin_authentication.util.annotations.NotForPublicConsumption

/**
 * @suppress
 * */
@NotForPublicConsumption
class PAInitialAppLogin {

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