package com.liyang.mytaxi;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by TodayFu Lee on 2019/11/8.
 */
public class TextOkhttp {

    public void textGet(){
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
}
