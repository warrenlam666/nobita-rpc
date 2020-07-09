package org.warren.nobita.client;

import lombok.Data;
import org.warren.nobita.client.config.NobitaClientConfig;
import org.warren.nobita.client.invocation.RemotingInvocation;
import org.warren.nobita.client.proxy.NobitaProxyFactory;
import org.warren.nobita.client.reader.JsonClientConfigReader;
import org.warren.nobita.client.serviceinfo.ServiceInfoManager;
import org.warren.nobita.register.NacosServiceDiscovery;
import org.warren.nobita.register.ServiceDiscovery;

@Data
public class NobitaClientContext {

    private NobitaClientConfig clientConfig;

    private NobitaProxyFactory proxyFactory;

    private ServiceInfoManager serviceInfoManager;

    private ServiceDiscovery serviceDiscovery;

    public NobitaClientContext (String resource){
        initContext(resource);
    }

    public void initContext(String path){
        this.clientConfig = new JsonClientConfigReader(path).load();
        this.serviceInfoManager = new ServiceInfoManager(this, clientConfig);
        this.serviceDiscovery = new NacosServiceDiscovery(this.getClientConfig().getRegister().getAddr());
        this.proxyFactory = new NobitaProxyFactory(this.serviceDiscovery);
    }
    public NobitaProxyFactory getProxyFactory(){
        return this.proxyFactory;
    }
}
