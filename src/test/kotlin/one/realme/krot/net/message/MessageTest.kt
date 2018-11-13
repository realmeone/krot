package one.realme.krot.net.message

import one.realme.krot.net.romtp.Message
import one.realme.krot.net.romtp.MessageType
import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class MessageTest {

    @Test
    fun testMessage() {
        val type = MessageType.PING
        val content = ByteArray(0)
        val length = content.size

        val msg = Message(type = type, content = content)
        assertEquals(length, msg.length)
        assertEquals(type, msg.type)
        assertArrayEquals(content, msg.content)
    }
}