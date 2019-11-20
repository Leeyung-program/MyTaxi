package com.liyang.mytaxi;

import android.app.Application;

/**
 * Created by TodayFu Lee on 2019/11/20.
 */

public class MyTaxiApplication extends Application {
    public static MyTaxiApplication instance;
    @Override
    public void onCreate() {
        super.onCreate();
        instance=this;
    }
    public static MyTaxiApplication getInstance(){
        return instance;
    }
}
