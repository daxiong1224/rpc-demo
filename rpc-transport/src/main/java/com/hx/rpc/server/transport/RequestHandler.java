package com.hx.rpc.server.transport;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * 处理网络请求的handler
 */
public interface RequestHandler {
    void onRequest(InputStream recive, OutputStream toResp);
}
