package com.liyang.mytaxi.account.presenter;

import android.os.Handler;
import android.os.Message;

import com.liyang.mytaxi.account.model.IAccountManger;
import com.liyang.mytaxi.account.view.IMainView;

import java.lang.ref.WeakReference;

/**
 * Created by TodayFu Lee on 2019/11/22.
 */

public class MainPresenterImp implements IMainPresenter{
    private IMainView view;
    private IAccountManger accountManager;
    /**
     * 接收子线程消息的 Handler
     */
    static class MyHandler extends Handler {

        // 弱引用
        WeakReference<MainPresenterImp> dialogRef;
        public MyHandler(MainPresenterImp presenter)
        {
            dialogRef = new WeakReference<MainPresenterImp>(presenter);
        }
        @Override
        public void handleMessage(Message msg) {
            MainPresenterImp presenter = dialogRef.get();
            if (presenter == null) {
                return;
            }
            // 处理UI 变化
            switch (msg.what) {


                case IAccountManger.LOGIN_SUC:
                    // 登录成功
                    presenter.view.showLoginSuc();
                    break;
                case IAccountManger.TOKEN_INVALID:
                    // 登录过期
                    presenter.view.showError(IAccountManger.TOKEN_INVALID, "");
                    break;
                case IAccountManger.SERVER_FAIL:
                    // 服务器错误
                    presenter.view.showError(IAccountManger.SERVER_FAIL, "");
                    break;
            }

        }
    }

    public MainPresenterImp(IMainView view, IAccountManger accountManager) {
        this.view = view;
        this.accountManager = accountManager;
        accountManager.setHandler(new MyHandler(this));
    }

    @Override
    public void loginByToken() {
        accountManager.loginByToken();
    }
}
