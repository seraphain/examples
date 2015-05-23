package com.github.seraphain.examples.cxf.client;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.stream.StreamResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * CXF client.
 * 
 * @author
 */
public class CxfClient3 {

    /** Log */
    private static Logger log = LoggerFactory.getLogger(CxfClient3.class);

    /**
     * Main method.
     * 
     * @param args
     */
    public static void main(String[] args) {
        SOAPConnection connection = null;
        try {
            // Create SOAP request
            MessageFactory messageFactory = MessageFactory.newInstance();
            SOAPMessage requestSOAPMessage = messageFactory.createMessage();

            SOAPPart requestSOAPPart = requestSOAPMessage.getSOAPPart();
            SOAPEnvelope requestSOAPEnvelope = requestSOAPPart.getEnvelope();

            // SOAP header
            // SOAPHeader requestSOAPHead = requestSOAPEnvelope.getHeader();

            // SOAP body�
            SOAPBody requestSOAPBody = requestSOAPEnvelope.getBody();
            SOAPElement bodyElement = requestSOAPBody.addChildElement(requestSOAPEnvelope.createName("service", "cxf",
                    "http://cxf.examples.seraphain.github.com/"));

            bodyElement.addChildElement("arg0", "", "").setTextContent("Client request.");

            // Attachment
            /*
            String textAttachment = "";
            AttachmentPart textAttachmentPart = requestSOAPMessage.createAttachmentPart();
            textAttachmentPart.setContent(textAttachment, "text/plain; charset=UTF-8");
            requestSOAPMessage.addAttachmentPart(textAttachmentPart);

            String urlAttachment = "";
            try {
                URL url = new URL(urlAttachment);
                DataHandler dataHandler = new DataHandler(url);
                AttachmentPart attachmentPart = requestSOAPMessage.createAttachmentPart(dataHandler);
                attachmentPart.setContentId(urlAttachment);
                requestSOAPMessage.addAttachmentPart(attachmentPart);
            } catch (MalformedURLException e) {
                if (log.isErrorEnabled()) {
                    log.error("Attachment url error.", e);
                }
            }
            */

            // Save message
            requestSOAPMessage.saveChanges();

            if (log.isInfoEnabled()) {
                // Log Web Service SOAP request�
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                StreamResult streamResult = new StreamResult();
                streamResult.setOutputStream(byteArrayOutputStream);
                Source source = requestSOAPMessage.getSOAPPart().getContent();
                Transformer transformer = TransformerFactory.newInstance().newTransformer();
                transformer.transform(source, streamResult);
                String request = byteArrayOutputStream.toString("UTF-8");
                log.info("SOAP request: " + request);
            }

            // Create SOAP connection and call service
            SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
            connection = soapConnectionFactory.createConnection();
            String serverAddress = "http://localhost:8080/cxf-server/cxfServer?wsdl";
            SOAPMessage replySOAPMessage = connection.call(requestSOAPMessage, serverAddress);

            // Handle the replay message�
            if (replySOAPMessage != null) {
                if (log.isInfoEnabled()) {
                    // Log Web Service SOAP replay�
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    StreamResult streamResult = new StreamResult();
                    streamResult.setOutputStream(byteArrayOutputStream);
                    Source source = replySOAPMessage.getSOAPPart().getContent();
                    Transformer transformer = TransformerFactory.newInstance().newTransformer();
                    transformer.transform(source, streamResult);
                    String request = byteArrayOutputStream.toString("UTF-8");
                    log.info("SOAP replay: " + request);
                }

                SOAPPart replySOAPPart = replySOAPMessage.getSOAPPart();
                SOAPEnvelope replySOAPEnvelope = replySOAPPart.getEnvelope();
                SOAPBody replySOAPBody = replySOAPEnvelope.getBody();
                if (replySOAPBody.getFault() == null) {

                } else {
                    log.info("SOAP request has been send with SOAPFault: " + replySOAPBody.getFault());
                }
            } else {
                if (log.isInfoEnabled()) {
                    log.info("SOAP request is sent with null reply.");
                }
            }
        } catch (SOAPException e) {
            if (log.isErrorEnabled()) {
                log.error("SOAPException occurs.", e);
            }
        } catch (TransformerConfigurationException e) {
            if (log.isErrorEnabled()) {
                log.error("TransformerConfigurationException occurs.", e);
            }
        } catch (TransformerFactoryConfigurationError e) {
            if (log.isErrorEnabled()) {
                log.error("TransformerFactoryConfigurationError occurs.", e);
            }
        } catch (TransformerException e) {
            if (log.isErrorEnabled()) {
                log.error("TransformerException occurs.", e);
            }
        } catch (UnsupportedEncodingException e) {
            if (log.isErrorEnabled()) {
                log.error("UnsupportedEncodingException occurs.", e);
            }
        } finally {
            // Close SOAP connection
            if (connection != null) {
                try {
                    connection.close();
                } catch (SOAPException e) {
                    // ignored exception
                }
            }
        }
    }

}
