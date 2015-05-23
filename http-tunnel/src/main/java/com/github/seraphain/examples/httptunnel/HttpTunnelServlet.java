package com.github.seraphain.examples.httptunnel;

import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implement of HTTP tunnel.
 * 
 * @author
 */
@SuppressWarnings("serial")
public class HttpTunnelServlet extends HttpServlet {

    /** Logger */
    private static final Logger logger = LoggerFactory.getLogger(HttpTunnelServlet.class);

    /**
     * Process request.
     * 
     * @param req
     *            the ServletRequest object that contains the client's request
     * @param res
     *            the ServletResponse object that contains the servlet's
     *            response
     * @throws ServletException
     *             if an exception occurs that interferes with the servlet's
     *             normal operation
     * @throws IOException
     *             if an input or output exception occurs
     */
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse rsp) {
        Object receivedObject = this.receive(req);
        if (receivedObject != null) {
            if (logger.isInfoEnabled()) {
                logger.info("Receive object: " + receivedObject);
            }
        }
        send(rsp, receivedObject);
    }

    /**
     * Receive object from client.
     * 
     * @param req
     * @return object sent by client
     */
    private Object receive(HttpServletRequest req) {
        Object receivedObject = null;
        try (ObjectInputStream in = new ObjectInputStream(req.getInputStream())) {
            receivedObject = in.readObject();
        } catch (EOFException eof) {
            if (logger.isInfoEnabled()) {
                logger.info("Object receive complete.", eof);
            }
        } catch (IOException e) {
            if (logger.isErrorEnabled()) {
                logger.error("IOException occurs while receiving object.", e);
            }
        } catch (ClassNotFoundException e) {
            if (logger.isErrorEnabled()) {
                logger.error("ClassNotFoundException occurs while receiving object.", e);
            }
        }
        return receivedObject;
    }

    /**
     * Send object to client.
     * 
     * @param rsp
     * @param object
     *            object sent to client
     */
    private void send(HttpServletResponse rsp, Object object) {
        byte[] buffer = null;
        try (ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
                ObjectOutputStream out = new ObjectOutputStream(byteOut)) {
            out.writeObject(object);
            out.flush();
            buffer = byteOut.toByteArray();
        } catch (IOException e) {
            if (logger.isErrorEnabled()) {
                logger.error("IOException occurs while convert object to bytes.", e);
            }
        }

        if (buffer != null) {
            rsp.setContentType("application/octet-stream");
            rsp.setContentLength(buffer.length);
            try (ServletOutputStream servletOut = rsp.getOutputStream()) {
                servletOut.write(buffer);
            } catch (IOException e) {
                if (logger.isErrorEnabled()) {
                    logger.error("IOException occurs while sending object.", e);
                }
            }
        }
    }
}
