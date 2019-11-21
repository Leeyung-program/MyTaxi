package com.liyang.mytaxi.account.view;

/**
 * Created by TodayFu Lee on 2019/11/21.
 */

public interface IView {

    void showLoading();

    void showError(int pwError, String s);
}
