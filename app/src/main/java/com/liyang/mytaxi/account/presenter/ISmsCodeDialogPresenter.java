package com.liyang.mytaxi.account.presenter;

/**
 * Created by TodayFu Lee on 2019/11/21.
 */

public interface ISmsCodeDialogPresenter {

    /**
     * 请求下发验证码
     * @param phone
     */
    void requestSendSmsCode(String phone);

    /**
     * 校验验证码
     * @param phone
     * @param smsCode
     */
    void requestCheckSmscode(String phone,String smsCode);

    /**
     * 检查用户是否存在
     * @param phone
     */
    void requestCheckUserExist(String phone);
}
