package com.hx.rpc.server;

import com.hx.rpc.server.codec.Request;
import com.hx.rpc.server.codec.ServiceDescriptor;
import com.hx.rpc.server.codec.common.utils.ReflectionUtils;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Method;

import static org.junit.Assert.*;

public class ServiceManagerTest {
    ServiceManager sm;

    @Before
    public void init(){
        sm = new ServiceManager();
        TestInterface bean = new TestClass();
        sm.register(TestInterface.class, bean);
    }

    @Test
    public void register() {
        TestInterface bean = new TestClass();
        sm.register(TestInterface.class, bean);
    }

    @Test
    public void lookup() {
        Method[] method = ReflectionUtils.getPublicMethods(TestInterface.class);
        ServiceDescriptor sdp = ServiceDescriptor.from(TestInterface.class, method[0]);

        Request request = new Request();
        request.setService(sdp);
        ServiceInstance sis = sm.lookup(request);
        assertNotNull(sis);
    }
}