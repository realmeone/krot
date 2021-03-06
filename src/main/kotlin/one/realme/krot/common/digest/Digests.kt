@file:kotlin.jvm.JvmName("DigestsKt")

package one.realme.krot.common.digest

import java.security.MessageDigest

fun ByteArray.sha256() = Digests.sha256(this)
fun ByteArray.sha256Twice() = Digests.sha256Twice(this)
fun ByteArray.ripemd160() = Digests.ripemd160(this)

object Digests {
    private const val SHA_256 = "SHA-256"
    
    fun ripemd160(input: ByteArray): ByteArray = Ripemd160().digest(input)
    fun sha256(input: ByteArray): ByteArray = newSha256Digest().digest(input)
    fun sha256Twice(b1: ByteArray, b2: ByteArray): ByteArray = sha256Twice(b1 + b2)
    fun sha256Twice(input: ByteArray): ByteArray {
        val digest = newSha256Digest()
        digest.update(input)
        return digest.digest(digest.digest())
    }

    private fun newSha256Digest() = MessageDigest.getInstance(SHA_256)
}