package com.github.seraphain.examples.httptunnel.client;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.net.URLConnection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation of HTTP tunnel client.
 * 
 * @author
 * @see com.github.seraphain.examples.httptunnel.client.HttpTunnelClient
 */
public class HttpTunnelClientImpl implements HttpTunnelClient {

    /** Logger */
    private static final Logger logger = LoggerFactory.getLogger(HttpTunnelClientImpl.class);

    /**
     * HTTP tunnel server address
     */
    private String httpTunnelServer;

    /**
     * Send object to server.
     * 
     * @param request
     * @return
     * @see com.github.seraphain.examples.httptunnel.client.HttpTunnelClient#send(java.lang.Object)
     */
    public Object send(Object request) {
        if (logger.isInfoEnabled()) {
            logger.info("Sending object: " + request);
        }

        byte[] buffer = null;
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream)) {
            objectOutputStream.writeObject(request);
            objectOutputStream.flush();
            buffer = byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            if (logger.isErrorEnabled()) {
                logger.error("IOException occurs while convert object to bytes.", e);
            }
            return null;
        }

        URLConnection urlConnection = null;
        try {
            URL url = new URL(httpTunnelServer);

            urlConnection = url.openConnection();
            urlConnection.setUseCaches(false);
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);

            urlConnection.setRequestProperty("content-type", "application/octet-stream");
            urlConnection.setRequestProperty("content-length", Integer.toString(buffer.length));
        } catch (IOException e) {
            if (logger.isErrorEnabled()) {
                logger.error("IOException occurs while creating URL connection.", e);
            }
            return null;
        }

        try (DataOutputStream dataOutputStream = new DataOutputStream(urlConnection.getOutputStream())) {
            dataOutputStream.write(buffer);
            dataOutputStream.flush();
        } catch (IOException e) {
            if (logger.isErrorEnabled()) {
                logger.error("IOException occurs while sending object.", e);
            }
            return null;
        }

        try (ObjectInputStream objectInputStream = new ObjectInputStream(urlConnection.getInputStream())) {
            Object response = objectInputStream.readObject();
            if (logger.isInfoEnabled()) {
                logger.info("Receiving object: " + response);
            }
            return response;
        } catch (EOFException eof) {
            if (logger.isInfoEnabled()) {
                logger.info("Object receive complete.", eof);
            }
            return null;
        } catch (IOException e) {
            if (logger.isErrorEnabled()) {
                logger.error("IOException occurs while receiving object.", e);
            }
            return null;
        } catch (ClassNotFoundException e) {
            if (logger.isErrorEnabled()) {
                logger.error("ClassNotFoundException occurs.", e);
            }
            return null;
        }
    }

    /**
     * @param httpTunnelServer
     *            the httpTunnelServer to set
     */
    public void setHttpTunnelServer(String httpTunnelServer) {
        this.httpTunnelServer = httpTunnelServer;
    }

}
