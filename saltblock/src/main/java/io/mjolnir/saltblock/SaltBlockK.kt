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
// val x = myMethod {
//      Next two run synchronously
//      val one = async { encryption()}
//      one.await() //this what returns
// }

// suspend fun encrypt
//
//    private suspend fun awaitEncrypt(plainTexts : List<String>): List<String> {
//        return withContext(Dispatchers.Default) {
//            AES.encrypt(keyAlias, plainTexts)
//        }
//    }
//}
//if my partner drinks tap water he gets exima
