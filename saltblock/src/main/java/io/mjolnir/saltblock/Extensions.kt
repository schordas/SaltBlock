package io.mjolnir.saltblock

import java.io.*

fun Serializable.getByteArray(): ByteArray {
    val byteStream = ByteArrayOutputStream()
    var bytes: ByteArray
    try {
        val output = ObjectOutputStream(byteStream)
        output.writeObject(this)
        output.flush()
        bytes = byteStream.toByteArray()
    } finally {
        try {
            byteStream.close()
        } catch (e : IOException) {
            e.printStackTrace()
        }
    }
    return bytes
}

fun ByteArray.getObject(): Any {

    val byteStream = ByteArrayInputStream(this)
    val input: ObjectInput
    input = ObjectInputStream(byteStream)
    val obj = input.readObject() as Any
    input.close()
    return obj
}

fun emptyByteArray(): ByteArray = ByteArray(0)

fun emptyString(): String = ""

fun isOver(bytes: ByteArray) {
    require(bytes.size < 245) { "Error: Cannot use RSA to encrypt more than 245 bytes" }
}