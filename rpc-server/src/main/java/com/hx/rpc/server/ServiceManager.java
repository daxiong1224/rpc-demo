package com.hx.rpc.server;

import com.hx.rpc.server.codec.Request;
import com.hx.rpc.server.codec.ServiceDescriptor;
import com.hx.rpc.server.codec.common.utils.ReflectionUtils;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 管理RPC暴露的服务
 */
@Slf4j
public class ServiceManager {
    private Map<ServiceDescriptor, ServiceInstance> services;

    public ServiceManager() {
        this.services = new ConcurrentHashMap<>();
    }

    /**
     * 注册服务
     * @param interfaceClass
     * @param bean
     * @param <T>
     */
    public <T> void register(Class<T> interfaceClass, T bean) {
        Method[] methods = ReflectionUtils.getPublicMethods(interfaceClass);
        for (Method method : methods) {
            ServiceInstance sis = new ServiceInstance(bean, method);
            ServiceDescriptor sdp = ServiceDescriptor.from(interfaceClass, method);

            //将提供的具体服务设到map中
            services.put(sdp, sis);
            log.info("register service； {} {}", sdp.getClazz(), sdp.getMethod());
        }
    }

    /**
     * 查找服务
     * @param request
     * @return
     */
    public ServiceInstance lookup(Request request) {
        //根据服务的描述从map中查具体服务
        ServiceDescriptor sdp = request.getService();
        return services.get(sdp);
    }
}
