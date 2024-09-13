package org.apache.coyote.http11;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import org.apache.catalina.servlet.HttpServlet;
import org.apache.catalina.servlet.ServletContext;
import org.apache.catalina.servlet.ServletContextFactory;
import org.apache.coyote.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Http11Processor implements Runnable, Processor {

    private static final Logger log = LoggerFactory.getLogger(Http11Processor.class);

    private final Socket connection;
    private final ServletContext servletContext = ServletContextFactory.getContext();

    public Http11Processor(Socket connection) {
        this.connection = connection;
    }

    @Override
    public void run() {
        log.info("connect host: {}, port: {}", connection.getInetAddress(), connection.getPort());
        process(connection);
    }

    @Override
    public void process(final Socket connection) {
        try (InputStream inputStream = connection.getInputStream();
             OutputStream outputStream = connection.getOutputStream()) {
            HttpRequest request = new HttpRequest(inputStream);
            HttpResponse response = new HttpResponse();
            log.info("Request: {}", request);

            HttpServlet servlet = servletContext.mapServlet(request);
            servlet.service(request, response);

            outputStream.write(HttpResponseWriter.write(response).getBytes());
            outputStream.flush();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }
}
