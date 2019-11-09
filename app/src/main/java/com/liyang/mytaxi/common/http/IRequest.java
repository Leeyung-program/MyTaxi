package com.liyang.mytaxi.common.http;

import java.util.Map;

public interface IRequest {
    public static final String POST = "POST";
    public static final String GET = "GET";

    /**
     * 指定请求方式
     *
     * @param method
     */
    void setMethod(String method);

    /**
     * 设置头部
     *
     * @param key
     * @param value
     */
    void setHeader(String key, String value);

    /**
     * 设置请求参数
     *
     * @param key
     * @param value
     */
    void setBody(String key, String value);

    /**
     * 提供给执行库请求行URL
     */
    String getUrl();

    /**
     * 获取头部信息
     * @return
     */
    Map<String,String> getHeader();

    /**
     * 提供给执行库请求参数
     * @return
     */
    Object getBody();
}
