package io.mjolnir.saltblock

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

    fun decrypt(keyAlias: String, cipherText: String) : String {
        return processDecryptionRequest(encryptionAlgorithm, keyAlias, cipherText)
    }

    fun decryptToObj(keyAlias: String, cipherText: String) : Any {
        return processDecryptionToObjectRequest(encryptionAlgorithm, keyAlias, cipherText)
    }
    
    fun decrypt(keyAlias: String, cipherTexts: List<String>) : List<String> {
        return processDecryptionRequest(encryptionAlgorithm, keyAlias, cipherTexts)
    }
}
