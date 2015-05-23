package com.github.seraphain.examples.httptunnel.client;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.net.URLConnection;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implement of HTTP tunnel client.
 * 
 * @author
 * @see com.github.seraphain.examples.httptunnel.client.HttpTunnelClient
 */
public class HttpTunnelClientImpl implements HttpTunnelClient {

    /** Log */
    private static final Logger log = LoggerFactory.getLogger(HttpTunnelClientImpl.class);

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
        if (log.isInfoEnabled()) {
            log.info("Sending object: " + request);
        }
        URL url = null;
        ObjectInputStream objectInputStream = null;
        ObjectOutputStream objectOutputStream = null;
        DataOutputStream dataOutputStream = null;
        ByteArrayOutputStream byteArrayOutputStream = null;
        try {
            url = new URL(httpTunnelServer);
            URLConnection urlConnection = url.openConnection();
            urlConnection.setUseCaches(false);
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);

            byteArrayOutputStream = new ByteArrayOutputStream();
            objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(request);
            objectOutputStream.flush();
            byte[] buffer = byteArrayOutputStream.toByteArray();

            urlConnection.setRequestProperty("content-type", "application/octet-stream");
            urlConnection.setRequestProperty("content-length", Integer.toString(buffer.length));

            dataOutputStream = new DataOutputStream(urlConnection.getOutputStream());
            dataOutputStream.write(buffer);
            dataOutputStream.flush();

            objectInputStream = new ObjectInputStream(urlConnection.getInputStream());
            Object response = objectInputStream.readObject();

            if (log.isInfoEnabled()) {
                log.info("Receiving object: " + response);
            }

            return response;
        } catch (EOFException eof) {
            if (log.isInfoEnabled()) {
                log.info("Object receive complete.", eof);
            }
            return null;
        } catch (IOException e) {
            if (log.isErrorEnabled()) {
                log.error("IOException occurs.", e);
            }
            return null;
        } catch (ClassNotFoundException e) {
            if (log.isErrorEnabled()) {
                log.error("ClassNotFoundException occurs.", e);
            }
            return null;
        } finally {
            IOUtils.closeQuietly(objectInputStream);
            IOUtils.closeQuietly(dataOutputStream);
            IOUtils.closeQuietly(objectOutputStream);
            IOUtils.closeQuietly(byteArrayOutputStream);
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
