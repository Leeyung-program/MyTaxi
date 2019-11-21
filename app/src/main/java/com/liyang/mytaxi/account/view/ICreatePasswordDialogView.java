package com.liyang.mytaxi.account.view;

/**
 * Created by TodayFu Lee on 2019/11/21.
 */

public interface ICreatePasswordDialogView extends IView {
    void showLoginSuc();

    void showRegiterSuc();

    void showPasswordNull();

    void showPasswordNotEqual();
}
