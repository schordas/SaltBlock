package io.mjolnir.saltblock

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.io.Serializable
import javax.crypto.Cipher

fun processRSAEncryptionRequest(plainTexts: List<String>, publicKey: String):
        List<String> {
    return runBlocking {
        withContext(Dispatchers.Default) {
            threadedEncryptionRequest(plainTexts, publicKey)
        }
    }
}

fun processRSAEncryptionRequest(plainText: String, publicKey: String) : String {
    return runBlocking {
        withContext(Dispatchers.Default) {
            val plainBytes = plainText.toByteArray()
            threadedEncryptionRequest(plainBytes, publicKey)
        }
    }
}

fun processRSAEncryptionRequest(obj: Serializable, publicKey: String) : String {
    return runBlocking {
        withContext(Dispatchers.Default) {
            val plainBytes = obj.getByteArray()
            threadedEncryptionRequest(plainBytes, publicKey)
        }
    }
}

private fun threadedEncryptionRequest(plainBytes: ByteArray, publicKey: String) : String {
    return try {
            encryptRSA(plainBytes, publicKey)
    } catch (e : Exception) {
        e.printStackTrace()
        emptyString()
    }

}

private fun threadedEncryptionRequest(plainTexts: List<String>, publicKey: String): List<String> {
    return try {
        RSA.encrypt(plainTexts, publicKey)
    } catch (e : Exception) {
        e.printStackTrace()
        emptyList()
    }
}

private fun encryptRSA(plainBytes: ByteArray, publicKey: String) : String {
    require(plainBytes.size < 245) { "Error: Cannot use RSA to encrypt more than 245 bytes" }
    val cipherBytes = RSA.doFinal(Cipher.ENCRYPT_MODE, emptyString(), plainBytes, publicKey)
    return Encoder.encodeToString(cipherBytes)
}