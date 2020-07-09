package org.warren.nobita.transport.handler;

import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.Data;

public class NobitaNioSocketChannel extends NioSocketChannel {

    private long lastHeartBeatResponse = System.currentTimeMillis();

    public long getLastHeartBeatResponse() {
        return lastHeartBeatResponse;
    }

    public void setLastHeartBeatResponse(long lastHeartBeatResponse) {
        this.lastHeartBeatResponse = lastHeartBeatResponse;
    }
}
