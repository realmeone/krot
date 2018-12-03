package one.realme.krot.common.net.romtp.content

import one.realme.krot.common.lang.toByteArray
import one.realme.krot.common.primitive.Hash

/**
 * when a peer client connect to a server, send this message to the server,
 * and will receive this message from the server. No further communication
 * is possible until both peers have exchanged this message.
 *
 * The peer client check the server's handshake message, get the last block height,
 * if the server's height is higher than the client's height, send GetBlocks message
 * to continue.
 */
class HandShake(
        val version: Int, // 4bytes
        val timestamp: Int, // 4 bytes
        val nodeId: Hash, // 32 bytes, The node identity
        val addr: NetAddr, // 26 bytes, The network address from this message sender
        val height: Long, // 	8 bytes, The last block received by the emitting node
        val os: String, // runtime os
        val agent: String // runtime instance name
) {
    fun toByteArray(): ByteArray = version.toByteArray() +
            timestamp.toByteArray() +
            nodeId.toByteArray() +
            addr.toByteArray() +
            height.toByteArray() +
            os.toByteArray() +
            agent.toByteArray()
}