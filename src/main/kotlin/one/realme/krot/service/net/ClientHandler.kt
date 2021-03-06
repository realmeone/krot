package one.realme.krot.service.net


import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler
import io.netty.handler.timeout.ReadTimeoutException
import one.realme.krot.net.Protocol.Message
import one.realme.krot.net.Protocol.Message.Type.*
import org.slf4j.LoggerFactory

internal class ClientHandler() : SimpleChannelInboundHandler<Message>() {
    private val log = LoggerFactory.getLogger(ClientHandler::class.java)

    override fun channelRead0(ctx: ChannelHandlerContext, msg: Message) {
        log.info("received from ${ctx.channel().remoteAddress()} : [$msg]")
        when (msg.type) {
            HANDSHAKE -> {
            }
            PONG -> {
                log.info("receive from server pong.")
            }
            DATA -> {

            }
            else -> ctx.channel().close()
        }
    }

    override fun channelActive(ctx: ChannelHandlerContext) {
        log.info("Peer Server ${ctx.channel().remoteAddress()} is connected.")
    }

    override fun channelInactive(ctx: ChannelHandlerContext) {
        log.info("Peer Server ${ctx.channel().remoteAddress()} is disconnected.")
    }

    override fun exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) {
        when (cause) {
            is ReadTimeoutException -> {
                log.info("Peer ${ctx.channel().remoteAddress()} is timeout.")
            }
        }
        ctx.close()
    }

}