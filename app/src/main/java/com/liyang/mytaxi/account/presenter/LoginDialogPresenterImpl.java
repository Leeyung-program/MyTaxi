package com.liyang.mytaxi.account.presenter;

import android.os.Handler;
import android.os.Message;

import com.liyang.mytaxi.account.model.IAccountManger;
import com.liyang.mytaxi.account.view.ILoginVIew;

import java.lang.ref.WeakReference;

/**
 * Created by TodayFu Lee on 2019/11/21.
 */

public class LoginDialogPresenterImpl implements ILoginDialogPresenter {
    private ILoginVIew view;

    private IAccountManger accountManager;
    /**
     * 接收子线程消息的 Handler
     */
    static class MyHandler extends Handler {
        // 弱引用
        WeakReference<LoginDialogPresenterImpl> dialogRef;
        public MyHandler(LoginDialogPresenterImpl presenter)
        {
            dialogRef = new WeakReference<LoginDialogPresenterImpl>(presenter);
        }
        @Override
        public void handleMessage(Message msg) {
            LoginDialogPresenterImpl presenter = dialogRef.get();
            if (presenter == null) {
                return;
            }
            // 处理UI 变化
            switch (msg.what) {
                case IAccountManger.LOGIN_SUC:
                    // 登录成功
                    presenter.view.showLoginSuc();
                    break;
                case IAccountManger.PW_ERROR:
                    // 密码错误
                    presenter.view.showError(IAccountManger.PW_ERROR, "");
                    break;
                case IAccountManger.SERVER_FAIL:
                    // 服务器错误
                    presenter.view.showError(IAccountManger.SERVER_FAIL, "");
                    break;
            }
        }

    }

    public LoginDialogPresenterImpl(ILoginVIew view,
                                    IAccountManger accountManager) {
        this.view = view;
        this.accountManager = accountManager;
        accountManager.setHandler(new MyHandler(this));
    }


    @Override
    public void requestLogin(String phone, String password) {
        accountManager.login(phone, password);
    }
}
