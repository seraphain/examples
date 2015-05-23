package com.github.seraphain.examples.xmlhttprequest.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Main class of XML HTTP request client.
 * 
 * @author
 * 
 */
public class XmlHttpRequestClientMain {

    /** Logger */
    private static Logger logger = LoggerFactory.getLogger(XmlHttpRequestClientMain.class);

    /**
     * Main method.
     * 
     * @param args
     */
    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext-client.xml");
        XmlHttpRequestClient xmlHttpRequestClient = (XmlHttpRequestClient) context.getBean("xmlHttpRequestClient");
        String xmlRequest = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><request>0</request>";
        String xmlResponse = xmlHttpRequestClient.post(xmlRequest);
        if (logger.isInfoEnabled()) {
            logger.info(xmlResponse);
        }
        context.close();
    }

}
