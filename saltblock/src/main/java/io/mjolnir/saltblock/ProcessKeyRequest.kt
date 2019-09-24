package io.mjolnir.saltblock

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.security.PublicKey

fun processPublicKeyRequest(keyAlias: String) : String {
    return runBlocking {
        withContext(Dispatchers.Default) {
            val key = threadedPublicKeyRequest(keyAlias)
            val keyBytes = key.encoded
            Encoder.encodeToString(keyBytes)
        }
    }
}

fun threadedPublicKeyRequest(keyAlias: String): PublicKey {
    return RSA.getPublicKey(keyAlias)
}
