package com.liyang.mytaxi.account.view;

/**
 * Created by TodayFu Lee on 2019/11/21.
 */

public interface ISmsCodeDialogView extends IView {

    void showCountDownTimer();

//    void close();

    void showUserExist(boolean b);

    void showSmsCodeCheckState(boolean suc);
}
