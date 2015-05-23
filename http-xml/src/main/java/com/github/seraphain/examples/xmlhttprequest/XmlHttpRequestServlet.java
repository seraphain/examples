package com.github.seraphain.examples.xmlhttprequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * XML HTTP Request Servlet。
 * 
 * @author
 */
@SuppressWarnings("serial")
public class XmlHttpRequestServlet extends HttpServlet {

    /** Logger */
    private static final Logger logger = LoggerFactory.getLogger(XmlHttpRequestServlet.class);

    /**
     * Process request.
     * 
     * @param request
     *            the ServletRequest object that contains the client's request
     * @param response
     *            the ServletResponse object that contains the servlet's
     *            response
     * @throws ServletException
     *             if an exception occurs that interferes with the servlet's
     *             normal operation
     * @throws IOException
     *             if an input or output exception occurs
     */
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) {
        String xmlRequest = readXMLFromRequest(request);
        if (logger.isInfoEnabled()) {
            logger.info("Receive XML Request: " + xmlRequest);
        }
        String xmlResponse = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><response>0</response>";
        if (logger.isInfoEnabled()) {
            logger.info("Send XML Response: " + xmlResponse);
        }
        writeXMLToResponse(response, xmlResponse);
    }

    /**
     * Read XML from request.
     * 
     * @param request
     * @return XML sent by client
     */
    private String readXMLFromRequest(HttpServletRequest request) {
        StringBuilder xml = new StringBuilder();
        try (BufferedReader reader = request.getReader()) {
            String line = null;
            while ((line = reader.readLine()) != null) {
                xml.append(line);
            }
        } catch (IOException e) {
            if (logger.isErrorEnabled()) {
                logger.error("Error occurs while reading XML.", e);
            }
        }
        return xml.toString();
    }

    /**
     * Write XML to response.
     * 
     * @param response
     * @param xml
     *            XML sent to client
     */
    private void writeXMLToResponse(HttpServletResponse response, String xml) {
        response.setContentType("text/xml");
        try (PrintWriter out = response.getWriter()) {
            out.println(xml);
        } catch (IOException e) {
            if (logger.isErrorEnabled()) {
                logger.error("Error occurs while writing XML.", e);
            }
        }
    }

}
