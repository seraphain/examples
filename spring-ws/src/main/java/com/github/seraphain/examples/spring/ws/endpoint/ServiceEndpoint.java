package com.github.seraphain.examples.spring.ws.endpoint;

import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;

import com.github.seraphain.examples.spring.ws.beans.ObjectFactory;
import com.github.seraphain.examples.spring.ws.beans.TestReq;
import com.github.seraphain.examples.spring.ws.beans.TestRsp;

@Endpoint
public class ServiceEndpoint {

    private final static ObjectFactory objFactory = new ObjectFactory();

    @PayloadRoot(localPart = "testReq", namespace = "http://seraphain.github.com/service")
    public TestRsp signalWorkflow(TestReq req) {
        System.out.println(req.getMessage());
        TestRsp rsp = new TestRsp();
        rsp.setMessage(req.getMessage());
        return rsp;
    }

}
