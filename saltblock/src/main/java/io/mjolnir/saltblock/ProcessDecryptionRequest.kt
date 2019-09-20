package io.mjolnir.saltblock

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.lang.Exception
import javax.crypto.Cipher

fun processDecyptionRequest(encryptionAlgorithm: EncryptionAlgorithm, keyAlias: String,
                            cipherText: String) : String {
    return runBlocking {
        withContext(Dispatchers.Default) {
            val bytes = threadedDecryptionRequest(encryptionAlgorithm, keyAlias, cipherText)
            Encoder.decodeToString(bytes)
        }
    }
}

fun processDecryptionToObjectRequest(encryptionAlgorithm: EncryptionAlgorithm, keyAlias: String,
                             cipherText: String) : Any {
    return runBlocking {
        withContext(Dispatchers.Default) {
            val bytes = threadedDecryptionRequest(encryptionAlgorithm, keyAlias, cipherText)
            bytes.getObject()
        }
    }
}

fun processDecryptionRequest(encryptionAlgorithm: EncryptionAlgorithm, keyAlias: String,
                             plainTexts: List<String>) : List<String> {
    return runBlocking {
        withContext(Dispatchers.Default) {
            threadedDecryptionRequest(encryptionAlgorithm, keyAlias, plainTexts)
        }
    }
}

private fun threadedDecryptionRequest(encryptionAlgorithm: EncryptionAlgorithm, keyAlias: String,
                                      cipherText: String) : ByteArray {
    val cipherBytes = Encoder.decode(cipherText)
    return try {
        when(encryptionAlgorithm) {
            EncryptionAlgorithm.AES -> {
                // TODO: temporary. change this once AES has been restructured
                emptyByteArray()
            }

            EncryptionAlgorithm.RSA -> {
                RSA.doFinal(Cipher.DECRYPT_MODE, keyAlias, cipherBytes)
            }
        }
    } catch (e : Exception) {
        e.printStackTrace()
        emptyByteArray()
    }
}

private fun threadedDecryptionRequest(encryptionAlgorithm: EncryptionAlgorithm, keyAlias: String,
                                      cipherTexts: List<String>) : List<String> {
    return try {
        when(encryptionAlgorithm) {
            EncryptionAlgorithm.AES -> {
                AES.encrypt(keyAlias, cipherTexts)
            }

            EncryptionAlgorithm.RSA -> {
                RSA.decrypt(keyAlias, cipherTexts)
            }
        }
    } catch (e : Exception) {
        e.printStackTrace()
        emptyList()
    }
}
