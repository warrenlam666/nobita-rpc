package org.warren.nobita.transport.handler;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

@Slf4j
public class NobitaClientIdleEventHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        log.info(evt.toString());
        if (evt instanceof IdleStateEvent){
            IdleStateEvent event = (IdleStateEvent)evt;
            if (event.state() == IdleState.READER_IDLE){
                //发送心跳
                ctx.write(Unpooled.copyLong(8));    //长度
                ctx.writeAndFlush(Unpooled.copyLong(0))
                        .channel().eventLoop()
                        .schedule(()->{
                            NobitaNioSocketChannel channel = (NobitaNioSocketChannel)(ctx.channel());
                            if (System.currentTimeMillis() - channel.getLastHeartBeatResponse() >= 18*1000)
                                NobitaClientNettyManager.reconnect(channel, channel.remoteAddress().getHostString());
                }, 18, TimeUnit.SECONDS);
            }else if (event.state() == IdleState.WRITER_IDLE){
                log.info(event.toString());
            }
        }else {
            super.userEventTriggered(ctx, evt);
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
    }
}
