package com.liyang.mytaxi.account.presenter;

import android.os.Handler;
import android.os.Message;

import com.liyang.mytaxi.account.model.IAccountManger;
import com.liyang.mytaxi.account.view.ISmsCodeDialogView;

import java.lang.ref.WeakReference;

/**
 * Created by TodayFu Lee on 2019/11/21.
 */

public class SmsCodeDialogPresenterImpl implements ISmsCodeDialogPresenter {
    private ISmsCodeDialogView view;
    private IAccountManger accountManager;
    /**
     * 接受消息并处理
     */
    private static class MyHandler extends Handler {
        WeakReference<SmsCodeDialogPresenterImpl> refContext;
        public MyHandler(SmsCodeDialogPresenterImpl context) {
            refContext = new WeakReference(context);
        }

        @Override
        public void handleMessage(Message msg) {

            SmsCodeDialogPresenterImpl presenter = refContext.get();
            switch (msg.what) {
                case IAccountManger.SMS_SEND_SUC:
                    presenter.view.showCountDownTimer();
                    break;
                case IAccountManger.SMS_SEND_FAIL:
                    presenter.view.showError(IAccountManger.SMS_SEND_FAIL, "");
                    break;
                case IAccountManger.SMS_CHECK_SUC:
                    presenter.view.showSmsCodeCheckState(true);

                    break;
                case IAccountManger.SMS_CHECK_FAIL:
                    presenter.view.showError(IAccountManger.SMS_CHECK_FAIL, "");
                    break;
                case IAccountManger.USER_EXIST:
                    presenter.view.showUserExist(true);
                    break;
                case IAccountManger.USER_NOT_EXIST:
                    presenter.view.showUserExist(false);
                    break;
                case IAccountManger.SERVER_FAIL:
                    presenter.view.showError(IAccountManger.SERVER_FAIL, "");
                    break;

            }
        }
    }
    public SmsCodeDialogPresenterImpl(ISmsCodeDialogView view,
                                      IAccountManger accountManager) {
        this.view = view;
        this.accountManager = accountManager;
        accountManager.setHandler(new MyHandler(this));
    }

    /**
     * 获取验证码
     * @param phone
     */
    @Override
    public void requestSendSmsCode(String phone) {
        accountManager.fetchSmsCode(phone);
    }

    /**
     * 验证码这验证码
     * @param phone
     * @param smsCode
     */
    @Override
    public void requestCheckSmscode(String phone, String smsCode) {

        accountManager.checkSmsCode(phone, smsCode);
    }

    /**
     * 检查用户是否存在
     * @param phone
     */
    @Override
    public void requestCheckUserExist(String phone) {
        accountManager.checkUserExist(phone);
    }
}
