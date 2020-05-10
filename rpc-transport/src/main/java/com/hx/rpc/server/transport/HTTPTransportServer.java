package com.hx.rpc.server.transport;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

@Slf4j
public class HTTPTransportServer implements TransportService{
    private RequestHandler handler;
    private Server service;

    @Override
    public void init(int port, RequestHandler handler) {
        this.handler = handler;
        this.service = new Server(port);

        //server 接受请求
        ServletContextHandler ctx = new ServletContextHandler();
        service.setHandler(ctx);

        ServletHolder holder = new ServletHolder(new RequestServlet());
        ctx.addServlet(holder, "/*");
    }

    @Override
    public void start() {
        try {
            service.start();
            service.join();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            e.printStackTrace();
        }


    }

    @Override
    public void stop() {
        try {
            service.stop();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            e.printStackTrace();
        }

    }

    class RequestServlet extends HttpServlet {
        @Override
        protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
            log.info("client connect");

            InputStream in = req.getInputStream();
            OutputStream out = resp.getOutputStream();

            if (handler != null) {
                handler.onRequest(in, out);
            }

            out.flush();
        }
    }
}
