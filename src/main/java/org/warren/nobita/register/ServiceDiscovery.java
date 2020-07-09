package org.warren.nobita.register;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.listener.EventListener;
import com.alibaba.nacos.api.naming.pojo.Instance;

import java.util.List;

public interface ServiceDiscovery {

    void register(String serviceName, String host, int port) throws NacosException;

    List<Instance> getAllInstances (String serviceName) throws NacosException;

    Instance selectOneHealthyInstance(String s) throws NacosException;



}
