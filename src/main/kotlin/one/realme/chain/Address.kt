package one.realme.chain

import one.realme.crypto.encoding.Base58
import one.realme.crypto.encoding.Hex
import one.realme.crypto.ripemd160
import one.realme.crypto.sha256
import one.realme.crypto.sha256Twice
import java.nio.ByteBuffer
import java.util.*


/**
 *
 * base58(prefix + base58(ripemd160(sha256(pubkey))) + String(sha256twice(CURRENT_VERSION + data), 0, 4)
 *
 * only support p2pkh & p2sh
 */
class Address {
    private val bytes: ByteArray

    constructor(base58: String) {
        bytes = Base58.decode(base58)
    }

    constructor(rawPublicKey: String, prefix: Byte) {
        val hash160 = Hex.decode(rawPublicKey).sha256().ripemd160()
        val addr = ByteBuffer.allocate(21).put(prefix).put(hash160).array()
        val checksum = Arrays.copyOf(addr.sha256Twice(), 4)
        bytes = addr + checksum
    }

    override fun toString(): String = Base58.encode(bytes)

    fun hash160(): String = Hex.encode(bytes.copyOfRange(1, 21))

    fun isValid(): Boolean {
        val checksum = bytes.copyOfRange(21, bytes.size)
        val addr = bytes.copyOfRange(0, 21)
        val toVerify = addr.sha256Twice().copyOf(4)
        return checksum.contentEquals(toVerify)
    }
}