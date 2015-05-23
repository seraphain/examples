package com.github.seraphain.examples.xmlhttprequest.client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation of XML HTTP request client。
 * 
 * @author
 * @see com.github.seraphain.examples.xmlhttprequest.client.XmlHttpRequestClient
 */
public class XmlHttpRequestClientImpl implements XmlHttpRequestClient {

    /** Logger */
    private static final Logger logger = LoggerFactory.getLogger(XmlHttpRequestClientImpl.class);

    /**
     * XML HTTP request server address
     */
    private String xmlHttpRequestServer;

    /**
     * Post XML HTTP request。
     * 
     * @param xmlRequest
     * @return
     * @see com.github.seraphain.examples.xmlhttprequest.client.XmlHttpRequestClient#post(java.lang.String)
     */
    public String post(String xmlRequest) {
        if (logger.isInfoEnabled()) {
            logger.info("Send xml: " + xmlRequest);
        }
        URLConnection urlConnection = null;
        try {
            URL url = new URL(xmlHttpRequestServer);
            urlConnection = url.openConnection();
            urlConnection.setUseCaches(false);
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            urlConnection.setRequestProperty("content-type", "text/xml");
        } catch (IOException e) {
            if (logger.isErrorEnabled()) {
                logger.error("IOException occurs while creating URL connection.", e);
            }
            return null;
        }

        try (OutputStreamWriter osw = new OutputStreamWriter(urlConnection.getOutputStream());
                BufferedWriter writer = new BufferedWriter(osw)) {
            writer.write(xmlRequest);
            writer.flush();
        } catch (IOException e) {
            if (logger.isErrorEnabled()) {
                logger.error("IOException occurs while sending XML.", e);
            }
            return null;
        }

        try (InputStreamReader isr = new InputStreamReader(urlConnection.getInputStream());
                BufferedReader reader = new BufferedReader(isr)) {
            StringBuilder xmlResponse = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                xmlResponse.append(line);
            }

            if (logger.isInfoEnabled()) {
                logger.info("Receive xml: " + xmlResponse.toString());
            }

            return xmlResponse.toString();
        } catch (IOException e) {
            if (logger.isErrorEnabled()) {
                logger.error("IOException occurs while receiving XML.", e);
            }
            return null;
        }
    }

    /**
     * @param xmlHttpRequestServer
     *            the xmlHttpRequestServer to set
     */
    public void setXmlHttpRequestServer(String xmlHttpRequestServer) {
        this.xmlHttpRequestServer = xmlHttpRequestServer;
    }

}
