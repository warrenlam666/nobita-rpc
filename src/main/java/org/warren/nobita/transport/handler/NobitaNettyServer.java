package org.warren.nobita.transport.handler;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import org.apache.http.impl.nio.codecs.LengthDelimitedDecoder;
import org.warren.nobita.server.invocation.ServiceInvocationHandler;

import java.net.InetSocketAddress;

public class NobitaNettyServer {

    private ServiceInvocationHandler invocationHandler;

    public NobitaNettyServer(ServiceInvocationHandler invocationHandler) throws InterruptedException {
        this.invocationHandler = invocationHandler;
        start();
    }

    public void start() throws InterruptedException {
        NioEventLoopGroup nioEventLoopGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(nioEventLoopGroup)
                    .channel(NioServerSocketChannel.class)
                    .localAddress(new InetSocketAddress(7412))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new LengthFieldBasedFrameDecoder(1024*1024*1024, 0, 8, 0, 8));
                            socketChannel.pipeline().addLast(new ServerIdleHandler());
                            socketChannel.pipeline().addLast(new NobitaServerDecoder());
                            socketChannel.pipeline().addLast(new NobitaServerHandler(invocationHandler));
                        }
                    });
            ChannelFuture future = bootstrap.bind().sync();
            future.channel().closeFuture().sync();
            future.channel();
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        finally {
            nioEventLoopGroup.shutdownGracefully().sync();
        }
    }


    public static void main(String[] args) {

    }

}
