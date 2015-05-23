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

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implement of HTTP tunnel.
 * 
 * @author
 */
@SuppressWarnings("serial")
public class HttpTunnelServlet extends HttpServlet {

    /** Log */
    private static final Logger log = LoggerFactory.getLogger(HttpTunnelServlet.class);

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
            if (log.isInfoEnabled()) {
                log.info("Receive object: " + receivedObject);
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
        ObjectInputStream in = null;
        Object receivedObject = null;
        try {
            in = new ObjectInputStream(req.getInputStream());
            receivedObject = in.readObject();
        } catch (EOFException eof) {
            if (log.isInfoEnabled()) {
                log.info("Object receive complete.", eof);
            }
        } catch (IOException e) {
            if (log.isErrorEnabled()) {
                log.error("IOException occurs while receiving object.", e);
            }
        } catch (ClassNotFoundException e) {
            if (log.isErrorEnabled()) {
                log.error("ClassNotFoundException occurs while receiving object.", e);
            }
        } finally {
            IOUtils.closeQuietly(in);
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
        rsp.setContentType("application/octet-stream");

        ByteArrayOutputStream byteOut = null;
        ObjectOutputStream out = null;
        ServletOutputStream servletOut = null;
        try {
            byteOut = new ByteArrayOutputStream();
            out = new ObjectOutputStream(byteOut);
            out.writeObject(object);
            out.flush();
            byte[] buffer = byteOut.toByteArray();
            rsp.setContentLength(buffer.length);
            servletOut = rsp.getOutputStream();
            servletOut.write(buffer);
        } catch (IOException e) {
            if (log.isErrorEnabled()) {
                log.error("IOException occurs while sending object.", e);
            }
        } finally {
            IOUtils.closeQuietly(servletOut);
            IOUtils.closeQuietly(out);
            IOUtils.closeQuietly(byteOut);
        }
    }

}
