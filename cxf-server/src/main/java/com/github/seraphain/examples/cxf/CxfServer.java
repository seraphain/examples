package com.github.seraphain.examples.cxf;

import javax.jws.WebService;

/**
 * CXF server interface.
 * 
 * @author
 */
@WebService
public interface CxfServer {

    /**
     * Handle request.
     * 
     * @param request
     * @return
     */
    public String service(String request);

}
