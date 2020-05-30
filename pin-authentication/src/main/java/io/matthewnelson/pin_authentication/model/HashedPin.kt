package io.matthewnelson.pin_authentication.model

import io.matthewnelson.pin_authentication.util.annotations.NotForPublicConsumption
import io.matthewnelson.pin_authentication.util.annotations.UnsafePinHash

/**
 * @suppress
 * */
@NotForPublicConsumption
inline class HashedPin(
    @property:UnsafePinHash
    val hashedPin: String
)