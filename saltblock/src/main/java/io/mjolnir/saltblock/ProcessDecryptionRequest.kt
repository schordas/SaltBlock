package io.mjolnir.saltblock

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.io.File
import java.lang.Exception
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
