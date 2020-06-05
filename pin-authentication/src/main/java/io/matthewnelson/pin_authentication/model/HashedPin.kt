package io.matthewnelson.pin_authentication.model

/**
 * @suppress
 * */
internal inline class HashedPin(
    @property:UnsafePinHash
    val hashedPin: String
)