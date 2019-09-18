package io.mjolnir.saltblock

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class SaltBlockK(private val encryptionAlgorithm: EncryptionAlgorithm = EncryptionAlgorithm.AES) {

    fun encrypt(keyAlias: String, plainText: String) : String {
        //TODO: make this an extension
        val plainTexts = ArrayList<String>()
        plainTexts.add(plainText)

        return encrypt(keyAlias, plainTexts)[0]
    }

    fun encrypt(keyAlias: String, plainTexts: List<String>) : List<String> {
        return runBlocking {
            // Offload encryption from the UI (Main) thread to Default (CPU) thread
            withContext(Dispatchers.Default) { threadEncrypt(keyAlias, plainTexts) }
        }
    }

    private fun threadEncrypt(keyAlias: String, plainTexts: List<String>) : List<String> {
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

    fun decrypt(keyAlias: String, cipherText: String) : String {
        //TODO: make this an extension
        val cipherTexts = ArrayList<String>()
        cipherTexts.add(cipherText)

        return decrypt(keyAlias, cipherTexts)[0]
    }
    
    fun decrypt(keyAlias: String, cipherTexts: List<String>) : List<String> {
       return runBlocking {
           // Offload decryption from the UI (Main) thread to the Default (CPU) thread
           withContext(Dispatchers.Default) { threadDecrypt(keyAlias, cipherTexts) }
       }
    }
    
    private fun threadDecrypt(keyAlias: String, cipherTexts: List<String>) : List<String> {
        return try {
            when(encryptionAlgorithm) {
                EncryptionAlgorithm.AES -> {
                    AES.decrypt(keyAlias, cipherTexts)
                }

                EncryptionAlgorithm.RSA -> {
                    RSA.decrypt(keyAlias, cipherTexts)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
}
