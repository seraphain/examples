package com.github.seraphain.examples.cxf.client;

import java.net.MalformedURLException;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.seraphain.examples.cxf.CxfServer;
import com.github.seraphain.examples.cxf.CxfServerService;

/**
 * CXF client.
 * 
 * @author
 */
public class CxfClient2 {

    /** Log */
    private static Logger log = LoggerFactory.getLogger(CxfClient2.class);

    /**
     * Main method.
     * 
     * @param args
     * @throws MalformedURLException
     */
    public static void main(String[] args) throws MalformedURLException {
        URL serverUrl = new URL("http://localhost:8080/cxf-server/cxfServer?wsdl");
        CxfServerService cxfServerService = new CxfServerService(serverUrl);
        CxfServer cxfServer = cxfServerService.getCxfServerPort();
        String result = cxfServer.service("Client request.");
        if (log.isInfoEnabled()) {
            log.info("Result: " + result);
        }
    }

}
