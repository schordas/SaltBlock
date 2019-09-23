package io.mjolnir.saltblock

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.security.PublicKey

fun processPublicKeyRequest(encryptionAlgorithm: EncryptionAlgorithm,
                            keyAlias: String) : String {
   require(encryptionAlgorithm == EncryptionAlgorithm.RSA) {
       "You can only share an RSA public key, please set your encryption algorithm accordingly"
   }
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
