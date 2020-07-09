package org.warren.nobita.transport.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import io.netty.handler.codec.MessageToByteEncoder;
import org.warren.nobita.codec.NobitaDefaultCodec;
import org.warren.nobita.protocol.NobitaRequest;
import org.warren.nobita.protocol.NobitaResponse;

import java.util.List;

public class NobitaServerCodec extends ByteToMessageCodec<Object> {

    private static NobitaDefaultCodec codec = new NobitaDefaultCodec();

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object o, ByteBuf byteBuf) throws Exception {
        byte[] data = codec.encoder(o);
        byteBuf.writeBytes(data);
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        int len = byteBuf.readableBytes();
        byte[] data = new byte[len];
        byteBuf.readBytes(data);
        list.add(codec.decoder(NobitaRequest.class, data));
    }
}
