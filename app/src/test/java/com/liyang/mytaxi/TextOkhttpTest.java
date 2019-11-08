package com.liyang.mytaxi;

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
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

    @Test
    public void textInterceptor(){
        Interceptor interceptor=new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                long startTime=System.currentTimeMillis();
                Request request=chain.request();
                Response response=chain.proceed(request);
                long endTime=System.currentTimeMillis();
                System.out.print("time========="+(startTime-endTime));
                return response;
            }
        };
        OkHttpClient client =new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build();
        MediaType mt=MediaType.parse("application/json;charset=utf-8");
        RequestBody rebody=RequestBody.create(mt,"{\"name\",\"liyang\"}");
        Request request=new Request.Builder()
                .url("http://httpbin.org/post")
                .post(rebody)
                .build();
        try {
            Response response=client.newCall(request).execute();
            System.out.print(response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void textCache(){
        Cache cache=new Cache(new File("cache.cache"),1024*1024*10);
        OkHttpClient client=new OkHttpClient.Builder()
                .cache(cache)
                .build();
        Request request=new Request.Builder()
                .url("http://httpbin.org/get?id=id")
                .cacheControl(CacheControl.FORCE_CACHE)
                .build();
        try {
            Response response =client.newCall(request).execute();
            Response responseCache=response.cacheResponse();
            Response responseNet=response.networkResponse();
            if (responseCache != null) {
                // 从缓存响应
                System.out.println("response from cache");
            }
            if (responseNet != null) {
                // 从缓存响应
                System.out.println("response from net");
            }

            System.out.println("response:" + response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}