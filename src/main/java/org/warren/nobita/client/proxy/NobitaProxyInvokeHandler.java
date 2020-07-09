package org.warren.nobita.client.proxy;

import org.warren.nobita.client.NobitaClientContext;
import org.warren.nobita.client.config.ConsumerConfig;
import org.warren.nobita.client.invocation.RemotingInvocation;
import org.warren.nobita.protocol.NobitaRequest;
import org.warren.nobita.protocol.NobitaResponse;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.UUID;

public class NobitaProxyInvokeHandler implements InvocationHandler {

    private Class<?> interfaceClass;

    private ConsumerConfig<?> consumerConfig;

    public NobitaProxyInvokeHandler(Class<?> interfaceClass, ConsumerConfig<?> consumerConfig) {
        this.interfaceClass = interfaceClass;
        this.consumerConfig = consumerConfig;
    }

    @Override
    public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
        String[] params = null;
        if (objects != null)
             params = Arrays.stream(objects).map(param->param.getClass().getName()).toArray(String[]::new);

        NobitaRequest request = NobitaRequest.builder()
                .requestId(UUID.randomUUID().toString())
                .interfaceName(interfaceClass.getName())
                .methodName(method.getName())
                .parametersClassName(params)
                .parametersValue(objects)
                .build();
        NobitaResponse response = RemotingInvocation.invoke(request, consumerConfig).get();    //异步转同步
        if (response.isSuccess() || response.getException() == null)
            return response.getRes();
        else
            throw response.getException();
    }

}
