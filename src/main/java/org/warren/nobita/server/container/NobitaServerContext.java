package org.warren.nobita.server.container;

import com.alibaba.nacos.api.exception.NacosException;
import lombok.extern.slf4j.Slf4j;
import org.warren.nobita.common.utils.ReflectionUtils;
import org.warren.nobita.register.NacosServiceDiscovery;
import org.warren.nobita.register.ServiceDiscovery;
import org.warren.nobita.server.config.Register;
import org.warren.nobita.server.config.ServerConfig;
import org.warren.nobita.server.config.ServiceInfo;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class NobitaServerContext {


    private ServerConfig config;
    private Map<String, Object> imps = new ConcurrentHashMap<>();
    private Map<String, String> lazyService = new HashMap<>();
    private ServiceDiscovery serviceRegister;

    public NobitaServerContext(ServerConfig config) {
        this.config = config;
        init();
    }

    private void init() {
        initServiceRegister();
        initServicesInfo();
    }

    public void initServiceRegister(){
        String registerType = Optional
                .ofNullable(config)
                .map(ServerConfig::getRegister)
                .map(Register::getName)
                .orElseThrow(() -> new IllegalArgumentException("can't find the config of register service"));
        if (registerType.equals("nacos"))
            this.serviceRegister = new NacosServiceDiscovery(this.config.getRegister().getAddr());
        else
            throw new IllegalArgumentException("暂不支持注册中心："+registerType);
    }

    public void initServicesInfo(){
        for (ServiceInfo serviceInfo : config.getServices()){
            log.info("find the Nobita service : {}", serviceInfo);
            if (serviceInfo.isLazy()){
                lazyService.put(serviceInfo.getInterface_name(), serviceInfo.getImplement_name());
            }else {
                imps.put(serviceInfo.getInterface_name(), ReflectionUtils.loadClass(serviceInfo.getImplement_name()));
            }
            try {
                this.serviceRegister.register(serviceInfo.getInterface_name(), config.getServerHost(), config.getServerPort());
            }catch (NacosException e){
                e.printStackTrace();
            }

        }
    }


    /*
     *通过接口名获取接口实现类实例对象
     * */
    public Object  getServiceImpl(String itf){
        Object o = imps.get(itf);
        if (o == null){
            String impl_name = lazyService.get(itf);
             o = ReflectionUtils.loadClass(impl_name);
             if (o != null)
                 imps.put(itf, o);
        }
        return o;
    }

}
