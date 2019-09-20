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

fun ByteArray.getObject(): Object {

    val byteStream = ByteArrayInputStream(this)
    val input: ObjectInput
    input = ObjectInputStream(byteStream)
    val obj = input.readObject() as Object
    input.close()
    return obj
}

fun emptyString(): String = ""