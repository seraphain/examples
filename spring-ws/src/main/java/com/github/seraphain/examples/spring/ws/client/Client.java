package com.github.seraphain.examples.spring.ws.client;

import java.net.MalformedURLException;
import java.net.URL;

import com.github.seraphain.examples.spring.ws.beans.TestReq;
import com.github.seraphain.examples.spring.ws.beans.WsException_Exception;

public class Client {

    public static void main(String[] args) throws MalformedURLException, WsException_Exception {
        ExampleServiceApiService exampleServiceApiService = new ExampleServiceApiService(new URL(
                "http://localhost:8080/spring-ws/wsdl/exampleService.wsdl"));
        ExampleServiceApi exampleServiceApi = exampleServiceApiService.getExampleServiceApiSoap11();
        TestReq testReq = new TestReq();
        testReq.setMessage("test");
        System.out.println(exampleServiceApi.test(testReq).getMessage());
    }

}
