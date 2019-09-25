package io.mjolnir.saltblock

import androidx.core.util.Pair
import java.io.File
import java.io.Serializable

class SaltBlock {

    fun encryptAES(keyAlias: String, plainText: String) : String {
        return processEncryptionRequest(keyAlias, plainText)
    }

    fun encryptAES(keyAlias: String, obj: Serializable) : String {
        return processEncryptionRequest(keyAlias, obj)
    }

    fun encryptAES(keyAlias: String, plainTexts: List<String>) : List<String> {
        return processEncryptionRequest(keyAlias, plainTexts)
    }

    fun encryptAES(keyAlias: String, file: File) : File {
        return processEncryptionRequest(keyAlias, file)
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

    fun encryptSharedFile(publicKey: String, file: File) :
            Pair<String, File> {
        return processSharedFileEncryptionRequest(publicKey, file)
    }

    fun decryptAES(keyAlias: String, cipherText: String) : String {
        return processDecryptionRequest(EncryptionAlgorithm.AES, keyAlias, cipherText)
    }

    fun decryptToObjAES(keyAlias: String, cipherText: String) : Any {
        return processDecryptionToObjectRequest(EncryptionAlgorithm.AES, keyAlias, cipherText)
    }
    
    fun decryptAES(keyAlias: String, cipherTexts: List<String>) : List<String> {
        return processDecryptionRequest(EncryptionAlgorithm.AES, keyAlias, cipherTexts)
    }

    fun decryptAES(keyAlias: String, file: File) : File {
        return processDecryptionRequest(keyAlias, file)
    }

    fun decryptRSA(keyAlias: String, cipherText: String) : String {
        return processDecryptionRequest(EncryptionAlgorithm.RSA, keyAlias, cipherText)
    }

    fun decryptToObjRSA(keyAlias: String, cipherText: String) : Any {
        return processDecryptionToObjectRequest(EncryptionAlgorithm.RSA, keyAlias, cipherText)
    }

    fun decryptRSA(keyAlias: String, cipherTexts: List<String>) : List<String> {
        return processDecryptionRequest(EncryptionAlgorithm.RSA, keyAlias, cipherTexts)
    }

    fun decryptSharedFile(rsaKeyAlias: String, wrappedKey: String, file: File) : File {
        return processDecryptionRequest(rsaKeyAlias, wrappedKey, file)
    }

    fun getPublicKey(keyAlias: String) : String {
        return processPublicKeyRequest(keyAlias)
    }
}
