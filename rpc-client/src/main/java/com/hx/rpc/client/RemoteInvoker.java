package com.hx.rpc.client;

import com.hx.rpc.server.codec.*;
import com.hx.rpc.server.transport.TransportClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * 调用远程服务的代理类
 */
@Slf4j
public class RemoteInvoker implements InvocationHandler {
    private Class clazz;
    private Encoder encoder;
    private Decoder decoder;
    private TransportSelector selector;

    public RemoteInvoker(Class clazz, Encoder encoder, Decoder decoder
            , TransportSelector selector) {
        this.clazz = clazz;
        this.encoder = encoder;
        this.decoder = decoder;
        this.selector = selector;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Request request = new Request();
        request.setService(ServiceDescriptor.from(clazz, method));
        request.setParameters(args);

        //调用远程方法 获得响应结果
        Response resp = invokeRemote(request);
        if (resp == null || resp.getCode() != 0) {
            throw new IllegalStateException("fail to invoke remote:" + resp);
        }

        //返回响应数据
        return resp.getData();
    }

    /**
     * 通过网络传输 调用远程服务
     * @param request
     * @return
     */
    private Response invokeRemote(Request request) {
        Response resp = null;
        TransportClient client = null;
        client = selector.select();//选择一个客户端连接
        byte[] outBytes = encoder.encode(request);//将请求序列化
        InputStream revice = client.write(new ByteArrayInputStream(outBytes));//发送数据 并取得响应

        try {
            byte[] inBytes = IOUtils.readFully(revice, revice.available());//读取响应
            resp = decoder.decode(inBytes, Response.class);//将响应结果反序列化成一个响应对象
        } catch (IOException e) {
            log.warn(e.getMessage(), e);
            resp = new Response();
            resp.setCode(1);
            resp.setMessage("RpcClient got error:" + e.getClass() + " : " + e.getMessage());
        } finally {
            if (client != null) {
                selector.release(client);
            }
        }
        return resp;
    }
}
