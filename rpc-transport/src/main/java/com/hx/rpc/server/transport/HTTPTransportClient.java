package com.hx.rpc.server.transport;

import com.hx.rpc.server.codec.Peer;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HTTPTransportClient implements TransportClient {
    private String url;

    @Override
    public void connect(Peer peer) {
        this.url = "http://" + peer.getHost() + ":" + peer.getPort();
    }

    /**
     * 基于HTTP的网络传输
     * @param data
     * @return
     */
    @Override
    public InputStream write(InputStream data) {
        try {
            //建立http连接
            HttpURLConnection httpConn = (HttpURLConnection) new URL(url).openConnection();
            httpConn.setDoOutput(true);
            httpConn.setDoInput(true);
            httpConn.setUseCaches(false);
            httpConn.setRequestMethod("POST");
            httpConn.connect();

            //将data请求数据发送给该地址的服务
            IOUtils.copy(data, httpConn.getOutputStream());
            int resultCode = httpConn.getResponseCode();//获取返回的编码
            if (resultCode == HttpURLConnection.HTTP_OK) {
                return httpConn.getInputStream();//成功，从输入流中获取数据
            } else {
                return httpConn.getErrorStream();
            }
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void close() {

    }
}
