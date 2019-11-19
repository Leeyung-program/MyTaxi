package com.liyang.mytaxi.account.bean;

/**
 * Created by TodayFu Lee on 2019/11/19.
 */

public class Account {
    private String account;
    private String token;
    private String uid;
    private long expired;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public long getExpired() {
        return expired;
    }

    public void setExpired(long expired) {
        this.expired = expired;
    }
}
