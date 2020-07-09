package org.warren.nobita.transport.handler;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;

@Slf4j
public class ClientReconnectionListener implements ChannelFutureListener {

    private int timeCount;
    private InetSocketAddress inetSocketAddress;
    private final static int MAX_RECONNECTION_COUNT = 5;

    public ClientReconnectionListener(InetSocketAddress inetSocketAddress, int timeCount) {
        this.inetSocketAddress = inetSocketAddress;
        this.timeCount = timeCount;
    }

    @Override
    public void operationComplete(ChannelFuture channelFuture) throws Exception {
        if (channelFuture.isSuccess()){
            log.info("Successfully connected to the server: {}", inetSocketAddress);
        }else {
            if (timeCount > MAX_RECONNECTION_COUNT){
                log.info("fail to reconnect to the server {}, the timeCount is {}", inetSocketAddress, timeCount);
            }else {

            }
        }
    }
}
