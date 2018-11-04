package one.realme.krot.net.romtp

import one.realme.krot.common.toBytes

class MessageType private constructor(val code: Int) {

    companion object {
        val HELLO = MessageType(0x00)
        val DISCONNECT = MessageType(0x01)
        val PING = MessageType(0x02)
        val PONG = MessageType(0x03)
        val GET_TIME = MessageType(0x04)
        val TIME = MessageType(0x05)

        private val MAX = MessageType(0xFF) // just for range check

        fun isValid(cmd: Int): Boolean = cmd >= HELLO.code && cmd <= MAX.code
        fun ofCode(code: Int): MessageType {
            require(isValid(code)) {
                "not valid type"
            }
            return MessageType(code)
        }
    }

    fun toBytes(): ByteArray = code.toBytes()

    override fun toString(): String {
        return "${javaClass.simpleName}(code=$code)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MessageType

        if (code != other.code) return false

        return true
    }

    override fun hashCode(): Int {
        return code
    }

}