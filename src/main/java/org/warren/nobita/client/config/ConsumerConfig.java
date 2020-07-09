package org.warren.nobita.client.config;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.listener.NamingEvent;
import com.alibaba.nacos.api.naming.pojo.Instance;
import org.warren.nobita.register.ServiceDiscovery;

import java.util.*;

public  class ConsumerConfig <T>{

    private Class<T> clazz;

    private ServiceDiscovery serviceDiscovery;

    private List<Instance> instances;

    public ConsumerConfig(Class<T> clazz, ServiceDiscovery serviceDiscovery) {
        this.serviceDiscovery = serviceDiscovery;
        this.clazz = clazz;
    }

    public void initInstances(){
        try {
           instances  = serviceDiscovery.getAllInstances(clazz.getName());
        }catch (NacosException e){
            e.printStackTrace();
        }

    }

    public String selectOneServerAddr(String serviceName){
        try {
            return Optional.ofNullable(this.serviceDiscovery.selectOneHealthyInstance(serviceName))
                    .map(instance -> instance.getIp()+":"+instance.getPort())
                    .orElse(null);
        }catch (NacosException e){
            e.printStackTrace();
            return null;
        }
    }
}
