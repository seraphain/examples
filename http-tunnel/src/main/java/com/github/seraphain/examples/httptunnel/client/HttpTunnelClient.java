package com.github.seraphain.examples.httptunnel.client;

/**
 * HTTP tunnel client.
 * 
 * @author
 */
public interface HttpTunnelClient {

    /**
     * Send object to server.
     * 
     * @param request
     * @return
     */
    public Object send(Object request);

}
