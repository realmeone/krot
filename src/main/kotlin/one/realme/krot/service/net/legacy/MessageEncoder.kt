package one.realme.krot.service.net.legacy

import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.MessageToByteEncoder
import one.realme.krot.common.net.legacy.Message

internal class MessageEncoder : MessageToByteEncoder<Message>() {
    override fun encode(ctx: ChannelHandlerContext, msg: Message, out: ByteBuf) {
        out.writeBytes(msg.toByteArray())
    }
}