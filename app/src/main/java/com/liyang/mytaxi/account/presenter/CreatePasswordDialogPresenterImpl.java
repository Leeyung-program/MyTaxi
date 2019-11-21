package com.liyang.mytaxi.account.presenter;

import android.os.Handler;
import android.os.Message;

import com.liyang.mytaxi.account.model.IAccountManger;
import com.liyang.mytaxi.account.view.ICreatePasswordDialogView;

import java.lang.ref.WeakReference;

/**
 * Created by TodayFu Lee on 2019/11/21.
 */

public class CreatePasswordDialogPresenterImpl implements ICreatePasswordDialogPresenter {
    private ICreatePasswordDialogView view;
    private IAccountManger accountManger;

    public CreatePasswordDialogPresenterImpl(ICreatePasswordDialogView view, IAccountManger accountManger) {
        this.view = view;
        this.accountManger = accountManger;
        accountManger.setHandler(new MyHandle(this));
    }

    static class MyHandle extends Handler{
        WeakReference<CreatePasswordDialogPresenterImpl > presenter;
        public MyHandle(CreatePasswordDialogPresenterImpl createPasswordDialogPresenter){
            presenter=
                    new WeakReference<CreatePasswordDialogPresenterImpl>
                    (createPasswordDialogPresenter);
        }

        @Override
        public void handleMessage(Message msg) {
            CreatePasswordDialogPresenterImpl dialogPresenter=presenter.get();
            if (dialogPresenter==null){
                return;
            }
            switch (msg.what){
                case IAccountManger.REGISTER_SUC:
                    dialogPresenter.view.showRegiterSuc();
                    break;
                case IAccountManger.LOGIN_SUC:
                    dialogPresenter.view.showLoginSuc();
                    break;
                case IAccountManger.SERVER_FAIL:
                    dialogPresenter.view.showError(IAccountManger.PW_ERROR, "");
                    break;
            }
        }
    }

    @Override
    public boolean checkPwd(String pwd, String pwd1) {
        if (pwd == null || pwd.equals("")) {

            view.showPasswordNull();
            return false;
        }
        if (!pwd.equals(pwd1)) {

            view.showPasswordNotEqual();
            return false;
        }
        return true;

    }

    @Override
    public void requestRegiser(String phone, String pwd) {
        accountManger.register(phone,pwd);
    }

    @Override
    public void requestLogin(String phone, String pwd) {
        accountManger.login(phone,pwd);
    }
}
