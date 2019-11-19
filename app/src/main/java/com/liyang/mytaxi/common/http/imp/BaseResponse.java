package com.liyang.mytaxi.common.http.imp;

import com.liyang.mytaxi.common.http.IResponse;

public class BaseResponse implements IResponse {
    public static final int STATE_UNKNOS_ERROR=100001;
    public static final int STATE_OK = 200;
    private int code;
    private String data;

    public void setCode(int code) {
        this.code = code;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getData() {
        return data;
    }
}
