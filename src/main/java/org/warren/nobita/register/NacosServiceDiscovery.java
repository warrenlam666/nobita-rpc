package org.warren.nobita.register;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.listener.EventListener;
import com.alibaba.nacos.api.naming.pojo.Instance;
import lombok.extern.slf4j.Slf4j;
import org.warren.nobita.server.config.ServerConfig;

import java.util.List;

@Slf4j
public class NacosServiceDiscovery implements ServiceDiscovery {

    private String serverAddr;

    private NamingService service;

    public NacosServiceDiscovery(String serverAddr) {
        this.serverAddr = serverAddr;
        init();
    }

    public void init(){
        if (serverAddr == null || serverAddr.trim().length() == 0)
            throw new IllegalArgumentException("server address of nacos register config is empty");
        try {
            this.service = NamingFactory.createNamingService(serverAddr);
        }catch (NacosException e){
            e.printStackTrace();
        }

    }


    @Override
    public void register(String serviceName, String host, int port) {
        try {
            log.info("try to register the service instance ({}, {}, {}) to nacos", serviceName, host, port);
            Instance instance = new Instance();
            instance.setIp(host);
            instance.setPort(port);
            instance.setWeight(1);
            instance.setServiceName(serviceName);
            this.service.registerInstance(serviceName, instance);
        }catch (NacosException e){
            e.printStackTrace();
        }
    }

    @Override
    public List<Instance> getAllInstances(String serviceName) throws NacosException {
        return this.service.getAllInstances(serviceName);
    }

    public void addListener(String serviceName, EventListener eventListener) throws NacosException {
        this.service.subscribe(serviceName, eventListener );
    }

    @Override
    public Instance selectOneHealthyInstance(String s) throws NacosException {
        return this.service.selectOneHealthyInstance(s);
    }
}
