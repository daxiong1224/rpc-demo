package com.hx.rpc.server.codec;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 表示网络传输一个端点
 */
@Data
@AllArgsConstructor
public class Peer {
    private String host;
    private int port;
}
