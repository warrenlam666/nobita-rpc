package org.warren.nobita.server.test;

import lombok.extern.slf4j.Slf4j;
import org.warren.nobita.server.config.ServerConfig;
import org.warren.nobita.server.container.NobitaServerContext;
import org.warren.nobita.server.invocation.ServiceInvocationHandler;
import org.warren.nobita.server.reader.JsonServerConfigReader;
import org.warren.nobita.transport.handler.NobitaNettyServer;

@Slf4j
public class NobitaServer {

    public static void main(String[] args) throws InterruptedException {
        ServerConfig config = new JsonServerConfigReader(NobitaServer.class.getResource("/nobita-config.json").getPath()).load();
        NobitaServerContext context = new NobitaServerContext(config);
        ServiceInvocationHandler handler = new ServiceInvocationHandler(context);
        NobitaNettyServer nettyServer = new NobitaNettyServer(handler);


    }

}
