package org.warren.nobita.server.test;

import org.warren.nobita.client.NobitaClientContext;
import org.warren.nobita.client.proxy.NobitaProxyFactory;

public class Main {

    public static void main(String[] args) {

        NobitaClientContext clientContext = new NobitaClientContext(Main.class.getClassLoader().getResource("nobita-client-config.json").getPath());
        NobitaProxyFactory proxyFactory = clientContext.getProxyFactory();
        HelloService service = proxyFactory.createService(HelloService.class);
        System.out.println(service.sayHi("warren"));
        System.out.println(service.sayHi("jacky"));
        System.out.println(service.sayHi("mary"));
        System.out.println(service.sayHi("walton"));
    }

}
