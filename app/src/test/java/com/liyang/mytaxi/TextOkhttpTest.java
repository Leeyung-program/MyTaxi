package com.liyang.mytaxi;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by TodayFu Lee on 2019/11/8.
 */
public class TextOkhttpTest {
    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void textGet() throws Exception {
        OkHttpClient okHttpClient=new OkHttpClient();
        Request request=new Request.Builder()
                .url("http://httpbin.org/get?id=id")
                .build();
        try {
            Response response=okHttpClient.newCall(request).execute();
            System.out.print(response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void textPost(){
        OkHttpClient client=new OkHttpClient();
        MediaType mediaType=MediaType.parse("application/json; charset=utf-8");
        RequestBody body=RequestBody.create(mediaType,"{\"name\",\"liyang\"}");
        Request request=new Request.Builder()
                .url("http://httpbin.org/post")
                .post(body)
                .build();
        try {
            Response response=client.newCall(request).execute();
            System.out.print(response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}