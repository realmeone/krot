package one.realme.chain

import com.google.common.primitives.Ints
import one.realme.common.Digests
import one.realme.common.toHexString
import one.realme.crypto.encoding.Hex
import java.util.*

/**
 * maybe hash32 is the right name?
 * restrict bytes length to 32?
 *
 * bytes will store in big-endian
 */
class Hash private constructor(private val bytes: ByteArray) {
    fun bits(): Int = bytes.size * 8
    fun asBytes(): ByteArray = bytes.clone()
    fun asBytesLE(): ByteArray = bytes.reversedArray()
    fun asInt(): Int = Ints.fromByteArray(bytes)
    fun asIntLE(): Int = Ints.fromByteArray(bytes.reversedArray())

    override fun toString(): String = asBytes().toHexString()
    override fun hashCode(): Int = asInt()
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as Hash
        if (!bytes.contentEquals(other.bytes)) return false
        return true
    }

    fun isEmpty(): Boolean = ByteArray(32).contentEquals(bytes)

    companion object {
        fun empty() = fromBytes(ByteArray(32))
        fun fromBytes(bytes: ByteArray): Hash = Hash(bytes)
        fun fromString(hex: String): Hash = Hash(Hex.decode(hex))
    }
}