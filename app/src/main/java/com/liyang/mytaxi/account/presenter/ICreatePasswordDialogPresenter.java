package com.liyang.mytaxi.account.presenter;

/**
 * Created by TodayFu Lee on 2019/11/21.
 */

public interface ICreatePasswordDialogPresenter {

    /**
     * 检查密码的正确性
     * @param pwd
     * @param pwd1
     */
    boolean checkPwd(String pwd, String pwd1);

    /**
     * 提交注册
     * @param phone
     * @param pwd
     */
    void requestRegiser(String phone,String pwd);

    /**
     * 登录
     * @param phone
     * @param pwd
     */
    void requestLogin(String phone,String pwd);
}
