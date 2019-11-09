package com.liyang.mytaxi.common.http;

public interface IResponse {

    /**
     * 获取状态码
     * @return
     */
    int getCode();

    /**
     * 获取返回数据
     * @return
     */
    String getData();
}
