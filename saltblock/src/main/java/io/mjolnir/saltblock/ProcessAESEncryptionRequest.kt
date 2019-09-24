package io.mjolnir.saltblock

import androidx.core.util.Pair
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.io.File
import java.io.Serializable
import java.security.Key
import javax.crypto.SecretKey

fun processEncryptionRequest(keyAlias: String,
                             plainTexts: List<String>):
        List<String> {
    return runBlocking {
        withContext(Dispatchers.Default) {
            threadedEncryptionRequest(keyAlias, plainTexts)
        }
    }
}

fun processEncryptionRequest(keyAlias: String,
                             plainText: String) : String {
    return runBlocking {
        withContext(Dispatchers.Default) {
            threadedEncryptionRequest(keyAlias, plainText)
        }
    }
}

fun processEncryptionRequest(keyAlias: String,
                             obj: Serializable) : String {
    return runBlocking {
        withContext(Dispatchers.Default) {
            threadedEncryptionRequest(keyAlias, obj)
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

private fun threadedEncryptionRequest(keyAlias: String,
                                      plainBytes: ByteArray) : String {
    return try {
            AES.encrypt(keyAlias, plainBytes)
        } catch (e : Exception) {
        e.printStackTrace()
        emptyString()
    }
}

private fun threadedEncryptionRequest(keyAlias: String,
                              plainText: String) : String {
    val plainBytes = plainText.toByteArray()
    return try {
        AES.encrypt(keyAlias, plainBytes)
    } catch (e : Exception) {
        e.printStackTrace()
        emptyString()
    }
}

private fun threadedEncryptionRequest(keyAlias: String,
                              obj: Serializable) : String {
    val plainBytes = obj.getByteArray()
    return try {
        AES.encrypt(keyAlias, plainBytes)
    } catch (e : Exception) {
        e.printStackTrace()
        emptyString()
    }

}

private fun threadedEncryptionRequest(keyAlias: String,
                              plainTexts: List<String>) : List<String> {
    return try {
        AES.encrypt(keyAlias, plainTexts)
    } catch (e : Exception) {
        e.printStackTrace()
        emptyList()
    }
}

private fun threadedKeyRequest() : SecretKey {
    return AES.getKeyToWrap()
}

private fun threadedKeyWrapRequest(publicKey: String, key: Key) : String {
    return RSA.wrapKey(publicKey, key)
}