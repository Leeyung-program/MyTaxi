package com.liyang.mytaxi.account.presenter;

/**
 * Created by TodayFu Lee on 2019/11/21.
 */

public interface ILoginDialogPresenter {
    /**
     * 登录
     * @param phone
     * @param pwd
     */
    void requestLogin(String phone,String pwd);
}
