package org.warren.nobita.transport.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import org.warren.nobita.client.proxy.NobitaRFManager;
import org.warren.nobita.client.proxy.NobitaResponseFuture;
import org.warren.nobita.codec.NobitaDefaultCodec;
import org.warren.nobita.protocol.NobitaResponse;

@Slf4j
public class NobitaClientInvokeHandler extends ChannelInboundHandlerAdapter {

    private static NobitaDefaultCodec codec = new NobitaDefaultCodec();

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf)msg;
        long type = buf.readLong();
        if (type == 0L){
            log.info("收到心跳回复");
        }
        int length = buf.readableBytes();
        byte[] array = new byte[length];
        buf.readBytes(array);
        NobitaResponse response = codec.decoder(NobitaResponse.class, array);
        NobitaResponseFuture future = NobitaRFManager.get(response.getRequestId());
        future.setRes(response);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.info("caught a exception: {}", cause.getMessage());
    }
}
