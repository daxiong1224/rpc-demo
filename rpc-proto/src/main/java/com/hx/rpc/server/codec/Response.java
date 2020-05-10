package com.hx.rpc.server.codec;

import lombok.Data;

/**
 * 表示RPC的一个返回
 */
@Data
public class Response {
    private int code = 0;//服务返回的编码 0=成功 非0=失败
    private String message = "ok";//具体的错误信息
    private Object data;//返回的数据
}
