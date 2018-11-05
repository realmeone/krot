package one.realme.krot.chain

import one.realme.krot.common.UnixTime
import one.realme.krot.common.toBytesLE
import one.realme.krot.crypto.BCSecp256k1
import one.realme.krot.crypto.sha256Twice
import java.nio.ByteBuffer

/**
 * a transaction in a normal block chain.
 */
class Transaction(
        val from: Address,
        val to: Address,
        val amount: Coin,
        val payload: ByteArray = ByteArray(0),
        val timestamp: UnixTime = UnixTime.now()
) {
    var signature = ""

    companion object {
        fun coinbase(to: Address): Transaction = Transaction(to, to, Coin.BASE_REWARD)
    }

    fun sign(privateKey: String) {
        signature = BCSecp256k1.sign(hash().toString(), privateKey)
    }

    fun hash(): Hash {
        val buffer = ByteBuffer.allocate(62 + payload.size)
                .put(from.toBytes()) // 25 bytes
                .put(to.toBytes()) // 25 bytes
                .put(amount.toBytes()) // 8 bytes
                .put(timestamp.toBytes()) // 4 bytes
                .put(payload)
        return Hash.fromBytes(buffer.array().sha256Twice())
    }

    override fun toString(): String {
        return "Transaction(from=$from, to=$to, amount=$amount, timestamp=$timestamp)"
    }


}