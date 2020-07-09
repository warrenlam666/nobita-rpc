package org.warren.nobita.transport.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ServerIdleHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf data = (ByteBuf) msg;
        data.markReaderIndex();
        long type = data.readLong();
        if (type == 0L){
            log.info("回复心跳");
            ctx.write(Unpooled.copyLong(8));    //数据长度
            ctx.write(Unpooled.copyLong(0));    //包类型
        }else{
            data.resetReaderIndex();
            ctx.fireChannelRead(msg);
            log.info("不是心跳包");
        }
    }
}
