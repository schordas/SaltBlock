package io.mjolnir.saltblock

import androidx.core.util.Pair
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.io.File
import java.io.Serializable
import java.security.Key
import javax.crypto.Cipher
import javax.crypto.SecretKey

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

fun processEncryptionRequest(keyAlias: String, file: File) : File {
    return runBlocking {
        withContext(Dispatchers.Default) {
            val plainBytes = file.readBytes()
            val fileStr = threadedEncryptionRequest(keyAlias, plainBytes)
            file.writeBytes(fileStr.toByteArray())
            file
        }
    }
}

fun processSharedFileEncryptionRequest(publicKey: String, file: File) : Pair<String, File> {
    return runBlocking {
        withContext(Dispatchers.Default) {
            val plainBytes = file.readBytes()
            val keyToWrap = threadedKeyRequest()
            val fileBytes = threadedShareFileEncryptionRequest(keyToWrap, plainBytes)
            file.writeBytes(fileBytes)

            val wrappedKey = threadedKeyWrapRequest(publicKey, keyToWrap)
            Pair(wrappedKey, file)
        }
    }
}

private fun threadedShareFileEncryptionRequest(key: SecretKey, plainBytes: ByteArray) : ByteArray {
    return try {
        AES.encrypt(key, plainBytes)
    } catch (e : Exception) {
        e.printStackTrace()
        emptyByteArray()
    }
}

private fun threadedKeyRequest() : SecretKey {
    return AES.getKeyToWrap()
}

private fun threadedKeyWrapRequest(publicKey: String, key: Key) : String {
    return RSA.wrapKey(publicKey, key)
}

private fun threadedEncryptionRequest(keyAlias: String,
                                      plainBytes: ByteArray) : String {
    return try {
            AES.encrypt(keyAlias, plainBytes)
        } catch (e : Exception) {
        e.printStackTrace()
        emptyString()
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
                // Check bytes are not over 245
                isOver(plainBytes)
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
                // Check bytes are not over 245
                isOver(plainBytes)
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