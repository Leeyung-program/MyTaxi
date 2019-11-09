package com.liyang.mytaxi.common.http.imp;

import com.google.gson.Gson;
import com.liyang.mytaxi.common.http.IRequest;
import java.util.HashMap;
import java.util.Map;

public class BaseRequest implements IRequest {
    private String method="post";
    private Map<String,String> header;
    private Map<String,String> body;
    private String url;

    public BaseRequest(String url){
        this.url=url;
        body=new HashMap<>();
        header=new HashMap<>();
    }

    @Override
    public void setMethod(String method) {
        this.method=method;
    }

    @Override
    public void setHeader(String key, String value) {
        header.put(key,value);
    }

    @Override
    public void setBody(String key, String value) {
        body.put(key,value);
    }

    @Override
    public String getUrl() {
        if (GET.equals(method)){
            for (String key : body.keySet()) {
                url=url.replace("${"+key+"}",body.get(key).toString());
            }
        }
        return url;
    }

    @Override
    public Map<String, String> getHeader() {
        return header;
    }

    @Override
    public String getBody() {
        if (body!=null){
            return  new Gson().toJson(body,HashMap.class);
        }{

            return "{}";
        }
    }
}
