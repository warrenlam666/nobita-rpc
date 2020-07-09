package org.warren.nobita.client.serviceinfo;

import lombok.extern.slf4j.Slf4j;
import org.warren.nobita.client.NobitaClientContext;
import org.warren.nobita.client.config.NobitaClientConfig;
import org.warren.nobita.client.config.ServiceInfo;
import org.warren.nobita.transport.handler.NobitaClientNettyManager;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class ServiceInfoManager {

    private Map<String, String[]> services = new ConcurrentHashMap<>();

    private NobitaClientConfig clientConfig;

    private NobitaClientContext clientContext;

    public ServiceInfoManager(NobitaClientContext clientContext, NobitaClientConfig clientConfig) {
        this.clientConfig = clientConfig;
        this.clientContext = clientContext;
        initConfig();
    }

    public void initConfig(){
//        if (clientConfig.getServicesInfo()  == null || clientConfig.getServicesInfo().length == 0)
//            return;
//        for (ServiceInfo info : clientConfig.getServicesInfo()){
//            log.info("serviceInfoManager load invocation info : {} - {}", info.getInterfaceName(), info.getServerAddr());
//            for (String addr : info.getServerAddr())
//                NobitaClientNettyManager.connectionWithSync(addr);
//            this.clientContext.getServiceDiscovery().
//            localServices.put(info.getInterfaceName(), info.getServerAddr());
//        }
    }

//    public String getOneServiceAddr(String interfaceName){
//        String[] addrs = localServices.get(interfaceName);
//        if (addrs == null || addrs.length == 0){
//            addrs = clientConfig.getGlobalInfo() != null ? clientConfig.getGlobalInfo().getDefaultServerAddr() : null;
//        }
//        if (addrs == null || addrs.length == 0)
//            return null;
//        return addrs[0];    //负载均衡还在写
//    }


}
