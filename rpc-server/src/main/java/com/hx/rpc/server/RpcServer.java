package com.hx.rpc.server;

import com.hx.rpc.server.codec.Decoder;
import com.hx.rpc.server.codec.Encoder;
import com.hx.rpc.server.codec.Request;
import com.hx.rpc.server.codec.Response;
import com.hx.rpc.server.codec.common.utils.ReflectionUtils;
import com.hx.rpc.server.transport.RequestHandler;
import com.hx.rpc.server.transport.TransportService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

@Slf4j
public class RpcServer {
    private RpcServerConfig config;
    private TransportService net;
    private Encoder encoder;
    private Decoder decoder;
    private ServiceManager serviceManager;
    private ServiceInvoker serviceInvoker;

    public RpcServer(RpcServerConfig config) {
        this.config = config;

        //net
        this.net = ReflectionUtils.newInstance(config.getTransportClass());
        this.net.init(config.getPort(), this.handler);

        //codec
        this.encoder = ReflectionUtils.newInstance(config.getEncoderClass());
        this.decoder = ReflectionUtils.newInstance(config.getDecoderClass());

        //service
        this.serviceManager = new ServiceManager();
        this.serviceInvoker = new ServiceInvoker();
    }

    public <T> void register(Class<T> interfaceClass, T bean) {
        this.serviceManager.register(interfaceClass, bean);
    }

    public void start(){
        this.net.start();
    }

    public void stop(){
        this.net.stop();
    }

    private RequestHandler handler = new RequestHandler() {
        @Override
        public void onRequest(InputStream recive, OutputStream toResp) {
            Response resp = new Response();
            try {
                byte[] inBytes = IOUtils.readFully(recive, recive.available());
                Request request = decoder.decode(inBytes, Request.class);

                log.info("get Request:", request);

                ServiceInstance sis = serviceManager.lookup(request);
                Object ret = serviceInvoker.invoker(sis, request);
                resp.setData(ret);
            } catch (IOException e) {
                log.warn(e.getMessage(), e);
                resp.setCode(1);
                resp.setMessage("RpcServer got error:" + e.getClass().getName() + ":" + e.getMessage());
            } finally {
                try {
                    byte[] outBytes = encoder.encode(resp);
                    toResp.write(outBytes);
                    log.info("responsed client");
                } catch (IOException e) {
                    log.warn(e.getMessage(), e);
                    e.printStackTrace();
                }
            }
        }
    };
}
