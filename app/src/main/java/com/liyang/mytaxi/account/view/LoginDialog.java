package com.liyang.mytaxi.account.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.liyang.mytaxi.MyTaxiApplication;
import com.liyang.mytaxi.R;
import com.liyang.mytaxi.account.bean.Account;
import com.liyang.mytaxi.account.response.LoginResponse;
import com.liyang.mytaxi.common.http.IHttpClient;
import com.liyang.mytaxi.common.http.IRequest;
import com.liyang.mytaxi.common.http.IResponse;
import com.liyang.mytaxi.common.http.api.API;
import com.liyang.mytaxi.common.http.biz.BaseBizResponse;
import com.liyang.mytaxi.common.http.imp.BaseRequest;
import com.liyang.mytaxi.common.http.imp.BaseResponse;
import com.liyang.mytaxi.common.http.imp.OkHttpClientImp;
import com.liyang.mytaxi.util.SharedPreferencesDao;
import com.liyang.mytaxi.util.ToastUtil;

import java.lang.ref.SoftReference;

/**
 * Created by TodayFu Lee on 2019/11/18.
 */

public class LoginDialog extends Dialog {
    private static final String TAG = "LoginDialog";
    private static final int LOGIN_SUC = 1;
    private static final int SERVER_FAIL = 2;
    private static final int PW_ERR = 4;
    private TextView mPhone;
    private EditText mPw;
    private Button mBtnConfirm;
    private View mLoading;
    private TextView mTips;
    private String mPhoneStr;
    private IHttpClient mHttpClient;
    private MyHandler mHandler;

    static class MyHandler extends Handler {
        SoftReference<LoginDialog> softReference;

        public MyHandler(LoginDialog loginDialog) {
            softReference = new SoftReference<LoginDialog>(loginDialog);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            LoginDialog loginDialog = softReference.get();
            if (loginDialog != null) {
                return;
            }
            switch (msg.what) {
                case LOGIN_SUC:
                    loginDialog.showLoginSuc();
                    break;
                case SERVER_FAIL:
                    loginDialog.showServerError();
                    break;
                case PW_ERR:
                    loginDialog.showPasswordError();

            }
        }
    }

    public LoginDialog(Context context, String phone) {
        this(context, R.style.Dialog);
        mPhoneStr = phone;
        mHttpClient = new OkHttpClientImp();
        mHandler = new MyHandler(this);
    }

    public LoginDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        View root = inflater.inflate(R.layout.dialog_login_input, null);
        setContentView(root);
        initListener();
    }

    private void initListener() {
        mPhone = findViewById(R.id.phone);
        mPw = findViewById(R.id.password);
        mBtnConfirm = findViewById(R.id.btn_confirm);
        mLoading = findViewById(R.id.loading);
        mTips = findViewById(R.id.tips);
        // 按钮注册监听
        mBtnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
                //                dismiss();
                //                String phone = mPhone.getText().toString();
                //                SmsCodeDialog dialog = new SmsCodeDialog(getContext(), phone);
                //                dialog.show();

            }
        });
        mPhone.setText(mPhoneStr);
        // 关闭按钮注册监听事件

        findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    private void submit() {
        new Thread(){
            @Override
            public void run() {
                super.run();
                String url= API.Config.getDomain()+API.LOGIN;
                IRequest request = new BaseRequest(url);
                request.setBody("phone", mPhoneStr);
                String password = mPw.getText().toString();
                request.setBody("password", password);


                IResponse response = mHttpClient.post(request, false);
                Log.d(TAG, response.getData());
                if (response.getCode() == BaseResponse.STATE_OK) {
                    LoginResponse bizRes =
                            new Gson().fromJson(response.getData(), LoginResponse.class);
                    if (bizRes.getCode() == BaseBizResponse.STATE_OK) {
                        // 保存登录信息
                        Account account = bizRes.getAccount();
                        // todo: 加密存储
                        SharedPreferencesDao dao =
                                new SharedPreferencesDao(MyTaxiApplication.getInstance(),
                                        SharedPreferencesDao.FILE_ACCOUNT);
                        dao.save(SharedPreferencesDao.KEY_ACCOUNT, account);

                        // 通知 UI
                        mHandler.sendEmptyMessage(LOGIN_SUC);
                    } if(bizRes.getCode() == BaseBizResponse.STATE_PW_ERR) {
                        mHandler.sendEmptyMessage(PW_ERR);
                    } else  {
                        mHandler.sendEmptyMessage(SERVER_FAIL);
                    }
                } else {
                    mHandler.sendEmptyMessage(SERVER_FAIL);
                }
            }
        }.start();
    }

    public void showOrHideLoading(boolean show) {
        if (show) {
            mLoading.setVisibility(View.VISIBLE);
            mBtnConfirm.setVisibility(View.GONE);
        } else {
            mLoading.setVisibility(View.GONE);
            mBtnConfirm.setVisibility(View.VISIBLE);
        }
    }

    public void showLoginSuc() {
        mLoading.setVisibility(View.GONE);
        mBtnConfirm.setVisibility(View.GONE);
        mTips.setVisibility(View.VISIBLE);
        mTips.setTextColor(getContext().getResources().getColor(R.color.color_text_normal));
        mTips.setText(getContext().getString(R.string.login_suc));
        ToastUtil.show(getContext(), getContext().getString(R.string.login_suc));
        dismiss();

    }

    /**
     *  显示服服务器出错
     */

    public void showServerError() {
        showOrHideLoading(false);
        mTips.setVisibility(View.VISIBLE);
        mTips.setTextColor(getContext().getResources().getColor(R.color.error_red));
        mTips.setText(getContext().getString(R.string.error_server));
    }


    /**
     * 密码错误
     */
    public void showPasswordError() {
        showOrHideLoading(false);
        mTips.setVisibility(View.VISIBLE);
        mTips.setTextColor(getContext().getResources().getColor(R.color.error_red));
        mTips.setText(getContext().getString(R.string.password_error));
    }


}
