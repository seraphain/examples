package com.github.seraphain.examples.cxf.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.seraphain.examples.cxf.CxfServer;

/**
 * Implement of CXF server interface.
 * 
 * @author
 * @see com.github.seraphain.examples.cxf.CxfServer
 */
public class CxfServerImpl implements CxfServer {

    /** Logger */
    private static Logger logger = LoggerFactory.getLogger(CxfServerImpl.class);

    /**
     * Handle request.
     * 
     * @param request
     * @return
     * @see com.github.seraphain.examples.cxf.CxfServer#service(java.lang.String)
     */
    public String service(String request) {
        if (logger.isInfoEnabled()) {
            logger.info("Request received: " + request);
        }
        if (request == null || request.length() == 0) {
            return "Request is null.";
        }
        return "Request received: " + request;
    }

}
