package com.liyang.mytaxi.common.http.api;

public class API {
    public static final String TEXT_GET="/get?uid=${uid}";
    public static final String TEXT_POST="/post";

    /**
     * 配置域名信息
     */
    public static class Config{
        private static final String TEXT_DOAMIN="http://httpbin.org";
        private static final String RELEASE_DOAMIN="http://httpbin.org";
        private static  String domin=TEXT_DOAMIN;

        /**
         * 配置debug
         * @param bug
         */
        public static void setDug(boolean bug){
            domin=bug?TEXT_DOAMIN:RELEASE_DOAMIN;
        }

        /**
         * 获取域名
         * @return
         */
        public static String getDomain(){
            return domin;
        }
    }
}
