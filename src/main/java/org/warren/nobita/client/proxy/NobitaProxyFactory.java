package org.warren.nobita.client.proxy;

import lombok.Data;
import org.warren.nobita.client.NobitaClientContext;
import org.warren.nobita.client.config.ConsumerConfig;
import org.warren.nobita.client.invocation.RemotingInvocation;
import org.warren.nobita.register.ServiceDiscovery;

import java.lang.reflect.Proxy;

@Data
public class NobitaProxyFactory {

    private ServiceDiscovery serviceDiscovery;

    public NobitaProxyFactory(ServiceDiscovery serviceDiscovery) {
        this.serviceDiscovery = serviceDiscovery;
    }

    @SuppressWarnings("unchecked")
    public  <T> T createService(Class<T> interfaceClass){
        ConsumerConfig<T> consumerConfig = new ConsumerConfig<T>(interfaceClass, serviceDiscovery);
        return (T) Proxy.newProxyInstance(
                NobitaProxyFactory.class.getClassLoader(),
                new Class[]{interfaceClass},
                new NobitaProxyInvokeHandler(interfaceClass, consumerConfig));
    }

}
