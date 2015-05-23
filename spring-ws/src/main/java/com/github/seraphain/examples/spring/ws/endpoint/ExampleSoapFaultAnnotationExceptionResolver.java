package com.github.seraphain.examples.spring.ws.endpoint;

import java.io.IOException;

import javax.xml.transform.Result;

import org.springframework.oxm.Marshaller;
import org.springframework.ws.soap.SoapFault;
import org.springframework.ws.soap.SoapFaultDetail;
import org.springframework.ws.soap.server.endpoint.SoapFaultAnnotationExceptionResolver;

import com.github.seraphain.examples.spring.ws.beans.WsException;

public class ExampleSoapFaultAnnotationExceptionResolver extends SoapFaultAnnotationExceptionResolver {

    private Marshaller marshaller;

    @Override
    protected void customizeFault(Object endpoint, Exception ex, SoapFault fault) {
        WsException exception = new WsException();
        exception.setMessage(ex.getMessage());
        exception.setErrorCode(500);
        SoapFaultDetail faultDetail = fault.addFaultDetail();
        Result result = faultDetail.getResult();
        try {
            marshaller.marshal(exception, result);
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }

    }

    public void setMarshaller(Marshaller marshaller) {
        this.marshaller = marshaller;
    }
}
