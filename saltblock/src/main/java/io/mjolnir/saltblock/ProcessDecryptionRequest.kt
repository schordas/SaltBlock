package io.mjolnir.saltblock

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.io.File
import java.lang.Exception
import java.security.Key
import javax.crypto.Cipher

fun processDecryptionRequest(encryptionAlgorithm: EncryptionAlgorithm, keyAlias: String,
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
                             cipherTexts: List<String>) : List<String> {
    return runBlocking {
        withContext(Dispatchers.Default) {
            threadedDecryptionRequest(encryptionAlgorithm, keyAlias, cipherTexts)
        }
    }
}

fun processDecryptionRequest(keyAlias: String, file: File) : File {
    return runBlocking {
        withContext(Dispatchers.Default) {
            val fileStr = file.readText()
            val plainBytes = threadedDecryptionRequest(keyAlias, fileStr)
            file.writeBytes(plainBytes)
            file
        }
    }
}

fun processDecryptionRequest(keyAlias: String, wrappedKey: String, file: File) : File {
    return runBlocking {
        with(Dispatchers.Default) {
            val fileStr = file.readText()
            val key = threadedKeyUnwrapRequest(keyAlias, wrappedKey)
            val plainBytes = threadedDecryptionRequest(key, fileStr)
            file.writeBytes(plainBytes)
            file
        }
    }
}

private fun threadedKeyUnwrapRequest(keyAlias: String, wrappedKey: String) : Key {
    return RSA.unwrapKey(keyAlias, wrappedKey)
}

private fun threadedDecryptionRequest(key: Key, cipherText: String) : ByteArray {
    return try {
        AES.decrypt(key, cipherText)
    } catch (e : Exception) {
        e.printStackTrace()
        emptyByteArray()
    }
}

private fun threadedDecryptionRequest(keyAlias: String, cipherText: String) : ByteArray {
   return try {
       AES.decrypt(keyAlias, cipherText)
   } catch (e : Exception) {
       e.printStackTrace()
       emptyByteArray()
   }
}

private fun threadedDecryptionRequest(encryptionAlgorithm: EncryptionAlgorithm, keyAlias: String,
                                      cipherText: String) : ByteArray {
    val cipherBytes = Encoder.decode(cipherText)
    return try {
        when(encryptionAlgorithm) {
            EncryptionAlgorithm.AES -> {
                AES.decrypt(keyAlias, cipherText)
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
                AES.decrypt(keyAlias, cipherTexts)
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
