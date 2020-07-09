package org.warren.nobita.transport.handler;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;
import org.warren.nobita.codec.NobitaDefaultCodec;
import org.warren.nobita.protocol.NobitaRequest;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;


@Slf4j
public class NobitaClientNettyManager {

    private static int MAX_RECONNECTION_COUNT = 5;

    private static Bootstrap bootstrap = initBootstrap();

    private static NobitaDefaultCodec codec = new NobitaDefaultCodec();

    private static Map<String, Channel> channels = new ConcurrentHashMap<>();

    private static Bootstrap initBootstrap(){
        Bootstrap bootstrap = new Bootstrap();
        EventLoopGroup group = new NioEventLoopGroup();
        bootstrap
                .group(group)
                .channel(NobitaNioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) {
                        socketChannel.pipeline().addLast(new IdleStateHandler(5, 18, 18));
                        socketChannel.pipeline().addLast(new LengthFieldBasedFrameDecoder(1024*1024*1024, 0, 8, 0, 8));
                        socketChannel.pipeline().addLast(new NobitaClientIdleEventHandler());
                        socketChannel.pipeline().addLast(new NobitaClientInvokeHandler());
                    }
                });
        return bootstrap;
    }

    public static Channel getNettyChannel(String addr) {
        if (channels.get(addr) == null)
            channels.put(addr, connectionWithSync(addr));
        return channels.get(addr);
    }

    public static void connectionWithAsync(String addr, int timeCount){
        log.info("try to reconnect to the host:{}, try count:{}", addr, timeCount);
        InetSocketAddress address = new InetSocketAddress(addr.split(":")[0], Integer.parseInt(addr.split(":")[1]));
        bootstrap.connect(address).addListener(future -> {
            if (future.isSuccess()){
                channels.put(addr, ((ChannelFuture)future).channel());
                log.info("successfully connect to the host: {}", addr);
            }
            else if (timeCount > MAX_RECONNECTION_COUNT)
                log.info("fail to connection to the host: {}, count is {}", addr, timeCount);
            else
                bootstrap.config().group().schedule(()-> connectionWithAsync(addr, timeCount+1), 1<<timeCount, TimeUnit.SECONDS);
        });
    }

    public static void reconnect(NobitaNioSocketChannel channel, String addr){
        channels.remove(addr);
        channel.close().syncUninterruptibly();
        connectionWithAsync(addr, 0);
    }

        public static Channel connectionWithSync(String addr) {
            if (channels.containsKey(addr))
                return channels.get(addr);
            InetSocketAddress address = new InetSocketAddress(addr.split(":")[0], Integer.parseInt(addr.split(":")[1]));
            int count = 1;
            while (count++ <= MAX_RECONNECTION_COUNT){
                ChannelFuture future = NobitaClientNettyManager.bootstrap.connect(address).syncUninterruptibly();
                if (future.isSuccess()){
                    channels.put(addr, future.channel());
                    return future.channel();
                }
            }
            return null;
    }

    public static void sendRequest(NobitaRequest request, String addr) {
        log.info("Nobita远程调用：{}", request);
        Channel channel = getNettyChannel(addr);
        if (channel == null)
            throw  new IllegalStateException("fail to invoke the remoting service, can't connect to the target host: "+addr);
        byte[] data = codec.encoder(request);
        channel.write(Unpooled.copyLong(8+data.length));
        channel.write(Unpooled.copyLong(1));
        channel.writeAndFlush(Unpooled.copiedBuffer(data));
    }

}
