package io.mjolnir.saltblock

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.io.Serializable
import javax.crypto.Cipher

fun processEncryptionRequest(encryptionAlgorithm: EncryptionAlgorithm, keyAlias: String,
                             plainTexts: List<String>): List<String> {
    return runBlocking {
        withContext(Dispatchers.Default) {
            threadedEncryptionRequest(encryptionAlgorithm, keyAlias, plainTexts)
        }
    }
}

fun processEncryptionRequest(encryptionAlgorithm: EncryptionAlgorithm, keyAlias: String,
                             plainText: String) : String {
    return runBlocking {
        withContext(Dispatchers.Default) {
            threadedEncryptionRequest(encryptionAlgorithm, keyAlias, plainText)
        }
    }
}

fun processEncryptionRequest(encryptionAlgorithm: EncryptionAlgorithm, keyAlias: String,
                             obj: Serializable) : String {
    return runBlocking {
        withContext(Dispatchers.Default) {
            threadedEncryptionRequest(encryptionAlgorithm, keyAlias, obj)
        }
    }
}

private fun threadedEncryptionRequest(encryptionAlgorithm: EncryptionAlgorithm, keyAlias: String,
                              plainText: String) : String {
    val plainBytes = plainText.toByteArray()
    return try {
        when(encryptionAlgorithm) {
            EncryptionAlgorithm.AES -> {
                AES.encrypt(keyAlias, plainBytes)
            }
            EncryptionAlgorithm.RSA -> {
                encryptRSA(keyAlias, plainBytes)
            }
        }
    } catch (e : Exception) {
        e.printStackTrace()
        emptyString()
    }
}

private fun threadedEncryptionRequest(encryptionAlgorithm: EncryptionAlgorithm, keyAlias: String,
                              obj: Serializable) : String {
    val plainBytes = obj.getByteArray()
    return try {
        when(encryptionAlgorithm) {
            EncryptionAlgorithm.AES -> {
                AES.encrypt(keyAlias, plainBytes)
            }
            EncryptionAlgorithm.RSA -> {
                encryptRSA(keyAlias, plainBytes)
            }
        }
    } catch (e : Exception) {
        e.printStackTrace()
        emptyString()
    }

}

private fun threadedEncryptionRequest(encryptionAlgorithm: EncryptionAlgorithm, keyAlias: String,
                              plainTexts: List<String>): List<String> {
    return try {
        when(encryptionAlgorithm) {
            EncryptionAlgorithm.AES -> {
                AES.encrypt(keyAlias, plainTexts)
            }
            EncryptionAlgorithm.RSA -> {
                RSA.encrypt(keyAlias, plainTexts)
            }
        }
    } catch (e : Exception) {
        e.printStackTrace()
        emptyList()
    }
}

private fun encryptRSA(keyAlias: String, plainBytes: ByteArray) : String {
    val cipherBytes = RSA.doFinal(Cipher.ENCRYPT_MODE, keyAlias, plainBytes)
    return Encoder.encodeToString(cipherBytes)
}