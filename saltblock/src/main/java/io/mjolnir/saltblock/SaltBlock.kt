package io.mjolnir.saltblock

import androidx.core.util.Pair
import java.io.File
import java.io.Serializable

class SaltBlock(private val encryptionAlgorithm: EncryptionAlgorithm = EncryptionAlgorithm.AES) {

    fun encryptAES(keyAlias: String, plainText: String) : String {
        return processEncryptionRequest(keyAlias, plainText)
    }

    fun encryptAES(keyAlias: String, obj: Serializable) : String {
        return processEncryptionRequest(keyAlias, obj)
    }

    fun encryptAES(keyAlias: String, plainTexts: List<String>) : List<String> {
        return processEncryptionRequest(keyAlias, plainTexts)
    }

    fun encryptRSA(plainText: String, publicKey: String) : String {
        return processRSAEncryptionRequest(plainText, publicKey)
    }

    fun encryptRSA(obj: Serializable, publicKey: String) : String {
        return processRSAEncryptionRequest(obj, publicKey)
    }

    fun encryptRSA(plainTexts: List<String>, publicKey: String) :
            List<String> {
        return processRSAEncryptionRequest(plainTexts, publicKey)
    }

    fun encryptPrivateFile(keyAlias: String, file: File) : File {
        return processEncryptionRequest(keyAlias, file)
    }

    fun encryptSharedFile(publicKey: String, file: File) :
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
        return processPublicKeyRequest(keyAlias)
    }
}
