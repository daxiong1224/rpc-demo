package com.hx.rpc.server.codec;

/**
 * 序列化
 */
public interface Encoder {
    byte[] encode(Object obj);
}
