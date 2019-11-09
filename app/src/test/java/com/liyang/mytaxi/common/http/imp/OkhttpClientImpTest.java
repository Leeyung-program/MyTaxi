package com.liyang.mytaxi.common.http.imp;

import com.liyang.mytaxi.common.http.IHttpClient;
import com.liyang.mytaxi.common.http.IRequest;
import com.liyang.mytaxi.common.http.api.API;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class OkhttpClientImpTest {

    IHttpClient iHttpClient;
    @Before
    public void setUp() throws Exception {
        iHttpClient=new OkhttpClientImp();
        API.Config.setDug(true);
    }

    @Test
    public void get() {
        String url= API.Config.getDomain()+ API.TEXT_GET;
        IRequest iRequest=new BaseRequest(url);
        iRequest.setHeader("testHeader", "test header");
        iRequest.setBody("body","body");
        iHttpClient.get(iRequest,false);
    }

    @Test
    public void post() {
        String url=API.Config.getDomain()+API.TEXT_POST;
        System.out.println(url);
        IRequest iRequest=new BaseRequest(url);
        iRequest.setBody("post","post");
        iRequest.setHeader("header","Header");
        iHttpClient.post(iRequest,false);
    }
}