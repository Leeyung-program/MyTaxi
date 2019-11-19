package com.liyang.mytaxi.common.http.imp;

import com.liyang.mytaxi.common.http.IHttpClient;
import com.liyang.mytaxi.common.http.IRequest;
import com.liyang.mytaxi.common.http.IResponse;

import java.io.IOException;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OkHttpClientImp implements IHttpClient {
    OkHttpClient client=new OkHttpClient();
    public OkHttpClientImp() {

    }

    @Override
    public IResponse get(IRequest request, boolean forceCache) {
        request.setMethod(IRequest.GET);
        Request.Builder builder=new Request.Builder();
        for (Map.Entry<String, String> entry : request.getHeader().entrySet()) {
            builder.addHeader(entry.getKey(),entry.getValue());
        }
        builder.url(request.getUrl()).get();
        Request req=builder.build();
        return execute(req);
    }

    @Override
    public IResponse post(IRequest request, boolean forceCache) {
        request.setMethod(IRequest.POST);
        Request.Builder builder=new Request.Builder();
        for (Map.Entry<String, String> entry : request.getHeader().entrySet()) {
            builder.addHeader(entry.getKey(),entry.getValue());
        }
        MediaType mediaType=MediaType.parse("application/json,charset=utf-8");
        RequestBody body=RequestBody.create(mediaType,request.getBody().toString());
        builder.post(body).url(request.getUrl());
        Request req=builder.build();
        return execute(req);
    }

    private IResponse execute(Request req) {
        BaseResponse response=new BaseResponse();
        try {
            Response resp=client.newCall(req).execute();
            System.out.println("======text"+resp.body().toString());
            response.setCode(resp.code());
            response.setData(resp.body().string());
        } catch (IOException e) {
             response.setData(e.getMessage());
             response.setCode(BaseResponse.STATE_UNKNOS_ERROR);
        }
        return response;
    }
}
