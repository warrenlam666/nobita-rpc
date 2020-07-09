package org.warren.nobita.transport.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.warren.nobita.codec.NobitaDefaultCodec;
import org.warren.nobita.protocol.NobitaRequest;

import java.util.List;

public class NobitaServerDecoder extends ByteToMessageDecoder {

    private static NobitaDefaultCodec codec = new NobitaDefaultCodec();

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        long type =  byteBuf.readLong();
        if (type != 1L)
            return;
        int len = byteBuf.readableBytes();
        byte[] data = new byte[len];
        byteBuf.readBytes(data);
        list.add(codec.decoder(NobitaRequest.class, data));
    }

}
