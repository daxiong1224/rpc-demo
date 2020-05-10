package com.hx.rpc.server;

import com.hx.rpc.server.codec.Request;
import com.hx.rpc.server.codec.common.utils.ReflectionUtils;

/**
 * 调用具体服务
 */
public class ServiceInvoker {
    public Object invoker(ServiceInstance service, Request request) {
        return ReflectionUtils.invoke(service.getTarget(), service.getMethod()
                , request.getParameters());
    }
}
