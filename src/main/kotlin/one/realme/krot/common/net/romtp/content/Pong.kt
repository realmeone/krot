package one.realme.krot.common.net.romtp.content

import one.realme.krot.common.lang.toByteArray
import one.realme.krot.common.lang.toLong
import kotlin.random.Random

class Pong {
    val nonce: Long

    constructor(nonce: Long = Random.nextLong()) {
        this.nonce = nonce
    }

    constructor(raw: ByteArray) {
        nonce = raw.toLong()
    }

    fun toByteArray(): ByteArray = nonce.toByteArray()
}