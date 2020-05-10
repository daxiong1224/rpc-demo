package com.hx.rpc.example;

import com.hx.rpc.server.RpcServer;
import com.hx.rpc.server.RpcServerConfig;

public class Server {
    public static void main(String[] args) {
        RpcServer server = new RpcServer(new RpcServerConfig());
        server.register(CalcService.class, new CalcServiceImpl());
        server.start();
    }
}
