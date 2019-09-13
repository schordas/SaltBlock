//package io.mjolnir.saltblock
//
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.GlobalScope
//import kotlinx.coroutines.launch
//import kotlinx.coroutines.withContext
//
//class SaltBlockK(val keyAlias: String, val encryptionAlgorithm: EncryptionAlgorithm = EncryptionAlgorithm.AES) {
//
//    fun encrypt(plainTexts: List<String>): List<String> {
//        GlobalScope.launch(Dispatchers.Main) {
//           awaitEncrypt(plainTexts)
//        }
//    }
//
//    private suspend fun awaitEncrypt(plainTexts : List<String>): List<String> {
//        return withContext(Dispatchers.Default) {
//            AES.encrypt(keyAlias, plainTexts)
//        }
//    }
//}
