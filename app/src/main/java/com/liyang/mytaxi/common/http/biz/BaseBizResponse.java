package com.liyang.mytaxi.common.http.biz;

/**
 * Created by TodayFu Lee on 2019/11/19.
 */

public class BaseBizResponse {

    public static final int STATE_OK = 200;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    // 状态码
    private int code;
    private String msg;


}
