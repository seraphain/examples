package com.github.seraphain.examples.httptunnel.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Main class of HTTP tunnel client.
 * 
 * @author
 */
public class HttpTunnelClientMain {

    /** Log */
    private static Logger log = LoggerFactory.getLogger(HttpTunnelClientMain.class);

    /**
     * Main method.
     * 
     * @param args
     */
    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext-client.xml");
        HttpTunnelClient httpTunnelClient = (HttpTunnelClient) context.getBean("httpTunnelClient");
        Object result = httpTunnelClient.send("Test Object");
        if (log.isInfoEnabled()) {
            log.info(result.toString());
        }
        context.close();
    }

}
