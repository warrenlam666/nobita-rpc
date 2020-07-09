package org.warren.nobita.transport.handler;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.warren.nobita.codec.NobitaDefaultCodec;
import org.warren.nobita.common.utils.ReflectionUtils;
import org.warren.nobita.protocol.NobitaRequest;
import org.warren.nobita.protocol.NobitaResponse;
import org.warren.nobita.server.container.NobitaServerContext;
import org.warren.nobita.server.invocation.ServiceInvocationHandler;

@Slf4j
public class NobitaServerHandler extends SimpleChannelInboundHandler<NobitaRequest> {

    private static NobitaDefaultCodec codec = new NobitaDefaultCodec();

    private ServiceInvocationHandler invokeHandler;

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("a client is successfully connect to server, client info: {}", ctx);
    }

    public NobitaServerHandler(ServiceInvocationHandler invokeHandler){
        this.invokeHandler = invokeHandler;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, NobitaRequest nobitaRequest) {
        NobitaResponse response = invokeHandler.invoke(nobitaRequest);
        log.info("the request invocation result: {}", response);
        byte[] data = codec.encoder(response);
        channelHandlerContext.write(Unpooled.copyLong(8L+data.length));
        channelHandlerContext.write(Unpooled.copyLong(1));
        channelHandlerContext.writeAndFlush(Unpooled.copiedBuffer(data));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.info("caught a exception: {}", cause.getMessage());
    }
}
