package org.warren.nobita.server.invocation;

import lombok.extern.slf4j.Slf4j;
import org.warren.nobita.codec.NobitaDefaultCodec;
import org.warren.nobita.common.utils.ReflectionUtils;
import org.warren.nobita.protocol.NobitaRequest;
import org.warren.nobita.protocol.NobitaResponse;
import org.warren.nobita.server.container.NobitaServerContext;

import java.lang.reflect.Method;
import java.util.Arrays;

@Slf4j
public class ServiceInvocationHandler {

    private NobitaServerContext context;

    private static NobitaDefaultCodec codec = new NobitaDefaultCodec();

    public ServiceInvocationHandler(NobitaServerContext context) {
        this.context = context;
    }

    public NobitaResponse invoke(NobitaRequest request) {
        log.info("服务端收到调用请求: {}", request);
        Object obj = context.getServiceImpl(request.getInterfaceName());
        Method method = ReflectionUtils.getMethod(obj.getClass().getName(), request.getMethodName(), request.getParametersClassName());
        if (method == null)
            throw new IllegalArgumentException("can't found the target method, " +request.getMethodName()+" "+ Arrays.toString(request.getParametersClassName())+" in "+request.getInterfaceName()+" isn't exist");
        try {
            Object res = ReflectionUtils.invoke(method, obj, request.getParametersValue());
            return NobitaResponse.builder().isSuccess(true).res(res).requestId(request.getRequestId()).build();
        }catch (Exception e){
            return NobitaResponse.builder().isSuccess(false).exception(e).requestId(request.getRequestId()).build();
        }
    }

}
