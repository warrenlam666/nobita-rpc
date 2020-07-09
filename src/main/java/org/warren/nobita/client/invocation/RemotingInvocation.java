package org.warren.nobita.client.invocation;


import org.warren.nobita.client.config.ConsumerConfig;
import org.warren.nobita.client.proxy.NobitaRFManager;
import org.warren.nobita.client.proxy.NobitaResponseFuture;
import org.warren.nobita.client.serviceinfo.ServiceInfoManager;
import org.warren.nobita.protocol.NobitaRequest;
import org.warren.nobita.transport.handler.NobitaClientNettyManager;


public class RemotingInvocation {


    public static NobitaResponseFuture invoke(NobitaRequest request, ConsumerConfig<?> consumerConfig) {
        String addr = consumerConfig.selectOneServerAddr(request.getInterfaceName());
        if (addr == null)
            throw new RuntimeException("can't find a available server address for "+request.getInterfaceName());
        NobitaResponseFuture future = new NobitaResponseFuture();
        NobitaRFManager.put(request.getRequestId(), future);
        NobitaClientNettyManager.sendRequest(request, addr);
        return future;
    }


}
