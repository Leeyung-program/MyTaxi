package com.liyang.mytaxi;

import org.junit.Before;
import org.junit.Test;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by TodayFu Lee on 2019/11/22.
 */

public class TestRxJava {

    @Before
    public void setUp(){
        Thread.currentThread().setName("currentThread");
    }

    @Test
    public void textSubcribe(){
        Subscriber<String> subscriber=new Subscriber<String>() {
            @Override
            public void onCompleted() {
                System.out.println("onCompleted in tread:" +
                        Thread.currentThread().getName());
            }

            @Override
            public void onError(Throwable e) {
                System.out.println("onError in tread:" +
                        Thread.currentThread().getName());
                e.printStackTrace();
            }

            @Override
            public void onNext(String s) {
                System.out.println("onNext in tread:" +
                        Thread.currentThread().getName());
                System.out.print(s);
            }
        };
        Observable observable=Observable.create(new Observable.OnSubscribe<Subscriber>() {
            @Override
            public void call(Subscriber subscriber1) {
                System.out.println("call in tread:" + Thread.currentThread().getName());
                subscriber1.onStart();
                subscriber1.onError(new Exception("error"));
                subscriber1.onNext("hello world");
                subscriber1.onCompleted();
            }
        });
        observable.subscribe(subscriber);
    }
}
