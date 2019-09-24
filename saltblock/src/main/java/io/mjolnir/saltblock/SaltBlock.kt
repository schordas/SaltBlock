package io.mjolnir.saltblock

import androidx.core.util.Pair
import java.io.File
import java.io.Serializable

class SaltBlock(private val encryptionAlgorithm: EncryptionAlgorithm = EncryptionAlgorithm.AES) {

    fun encrypt(keyAlias: String, plainText: String) : String {
        return processEncryptionRequest(encryptionAlgorithm, keyAlias, plainText)
    }

    fun encrypt(keyAlias: String, obj: Serializable) : String {
        return processEncryptionRequest(encryptionAlgorithm, keyAlias, obj)
    }

    fun encrypt(keyAlias: String, plainTexts: List<String>) : List<String> {
        return processEncryptionRequest(encryptionAlgorithm, keyAlias, plainTexts)
    }

    fun encrypt(keyAlias: String, file: File) : File {
        return processEncryptionRequest(keyAlias, file)
    }

    fun encryptFileToShare(temporaryAesAlias: String, publicKey: String, file: File) :
            Pair<String, File> {
        return processSharedFileEncryptionRequest(publicKey, file)
    }

    fun decrypt(keyAlias: String, cipherText: String) : String {
        return processDecryptionRequest(encryptionAlgorithm, keyAlias, cipherText)
    }

    fun decryptToObj(keyAlias: String, cipherText: String) : Any {
        return processDecryptionToObjectRequest(encryptionAlgorithm, keyAlias, cipherText)
    }
    
    fun decrypt(keyAlias: String, cipherTexts: List<String>) : List<String> {
        return processDecryptionRequest(encryptionAlgorithm, keyAlias, cipherTexts)
    }

    fun decrypt(keyAlias: String, file: File) : File {
        return processDecryptionRequest(keyAlias, file)
    }

    fun decryptSharedFile(rsaKeyAlias: String, wrappedKey: String, file: File) : File {
        return processDecryptionRequest(rsaKeyAlias, wrappedKey, file)
    }

    fun getPublicKey(keyAlias: String) : String {
        return processPublicKeyRequest(encryptionAlgorithm, keyAlias)
    }
}
