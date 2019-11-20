package com.liyang.mytaxi.account.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
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
import com.liyang.mytaxi.util.DevUtil;
import com.liyang.mytaxi.util.SharedPreferencesDao;
import com.liyang.mytaxi.util.ToastUtil;

import java.lang.ref.SoftReference;

/**
 * Created by TodayFu Lee on 2019/11/18.
 */

public class CreatePasswordDialog extends Dialog {
    private static final String TAG = "CreatePasswordDialog";
    private static final int REGISTER_SUC = 1;
    private static final int SERVER_FAIL = 100;
    private static final int LOGIN_SUC = 2;
    private TextView mTitle;
    private TextView mPhone;
    private EditText mPw;
    private EditText mPw1;
    private Button mBtnConfirm;
    private View mLoading;
    private TextView mTips;
    private String mPhoneStr;
    private IHttpClient mHttpClient;
    private MyHandler mHandler;

    public CreatePasswordDialog(Context context, String mPhoneStr) {
        this(context, R.style.Dialog);
        this.mPhoneStr = mPhoneStr;
        mHttpClient = new OkHttpClientImp();
        mHandler = new MyHandler(this);

    }

    public CreatePasswordDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View root = inflater.inflate(R.layout.dialog_create_pw, null);
        setContentView(root);
        intView();
    }


    private void intView() {
        mPhone = (TextView) findViewById(R.id.phone);
        mPw = (EditText) findViewById(R.id.pw);
        mPw1 = (EditText) findViewById(R.id.pw1);
        mBtnConfirm = (Button) findViewById(R.id.btn_confirm);
        mLoading = findViewById(R.id.loading);
        mTips = (TextView) findViewById(R.id.tips);
        mTitle = (TextView) findViewById(R.id.dialog_title);
        findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        mBtnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });
        mPhone.setText(mPhoneStr);
    }

    private void submit() {
        if (checkPwd()) {
            final String password = mPw.getText().toString();
            final String phonePhone = mPhoneStr;
            new Thread() {
                @Override
                public void run() {
                    String url = API.Config.getDomain() + API.REGISTER;
                    IRequest request = new BaseRequest(url);
                    request.setBody("password", password);
                    request.setBody("phone", phonePhone);
                    request.setBody("uid", DevUtil.UUID(getContext()));
                    IResponse response = mHttpClient.post(request, false);
                    Log.d(TAG, response.getData());
                    if (response.getCode() == BaseBizResponse.STATE_OK) {
                        BaseBizResponse bizResponse = new Gson().fromJson(response.getData(), BaseBizResponse.class);
                        if (bizResponse.getCode() == BaseResponse.STATE_OK) {
                            mHandler.sendEmptyMessage(REGISTER_SUC);
                        } else {
                            mHandler.sendEmptyMessage(SERVER_FAIL);
                        }
                    } else {
                        mHandler.sendEmptyMessage(SERVER_FAIL);
                    }
                }
            }.start();
        }
    }

    private boolean checkPwd() {
        String password = mPw.getText().toString();
        if (TextUtils.isEmpty(password)) {
            mTips.setVisibility(View.VISIBLE);
            mTips.setText(getContext().getString(R.string.password_is_null));
            mTips.setTextColor(getContext().getResources().getColor(R.color.error_red));
            return false;
        }
        if (!password.equals(mPw1.getText().toString())) {
            mTips.setVisibility(View.VISIBLE);
            mTips.setText(getContext().getString(R.string.password_is_not_equal));
            mTips.setTextColor(getContext().getResources().getColor(R.color.error_red));
            return false;
        }
        return true;
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

    public void showRegisterSuc() {
        // 自动登录
        mLoading.setVisibility(View.VISIBLE);
        mBtnConfirm.setVisibility(View.GONE);
        mTips.setVisibility(View.VISIBLE);
        mTips.setTextColor(getContext().getResources().getColor(R.color.color_text_normal));
        mTips.setText(getContext().getString(R.string.register_suc_and_loging));
        // TODO: 请求网络，完成自动登录
        new Thread() {
            @Override
            public void run() {
                super.run();
                String url = API.Config.getDomain() + API.LOGIN;
                IRequest request = new BaseRequest(url);
                request.setBody("phone", mPhoneStr);
                String pwd = mPw.getText().toString();
                request.setBody("password", pwd);
                IResponse response = mHttpClient.post(request, false);
                if (response.getCode() == BaseResponse.STATE_OK) {
                    LoginResponse loginResponse =
                            new Gson().fromJson(response.getData(), LoginResponse.class);
                    if (loginResponse.getCode() == BaseBizResponse.STATE_OK) {
                        Account account = loginResponse.getAccount();
                        SharedPreferencesDao sharedPreferencesDao =
                                new SharedPreferencesDao(MyTaxiApplication
                                .getInstance(), SharedPreferencesDao.FILE_ACCOUNT);
                        sharedPreferencesDao.save(SharedPreferencesDao.KEY_ACCOUNT, account);
                        mHandler.sendEmptyMessage(LOGIN_SUC);
                    } else {
                        mHandler.sendEmptyMessage(SERVER_FAIL);
                    }
                } else {
                    mHandler.sendEmptyMessage(SERVER_FAIL);
                }
            }
        }.start();

    }


    public void showLoginSuc() {
        dismiss();
        ToastUtil.show(getContext(), getContext().getString(R.string.login_suc));

    }

    public void showUserExist(boolean exist) {
        if (exist) {
            mTitle.setText(getContext().getString(R.string.modify_pw));
        } else {
            mTitle.setText(getContext().getString(R.string.create_pw));
        }
        mLoading.setVisibility(View.GONE);
        mTips.setVisibility(View.GONE);
    }


    public void showServerError() {
        mTips.setTextColor(getContext().getResources().getColor(R.color.error_red));
        mTips.setText(getContext().getString(R.string.error_server));
    }


    static class MyHandler extends Handler {

        SoftReference<CreatePasswordDialog> codeDialog;

        public MyHandler(CreatePasswordDialog dialog) {
            codeDialog = new SoftReference<CreatePasswordDialog>(dialog);
        }

        @Override
        public void handleMessage(Message msg) {
            CreatePasswordDialog dialog = codeDialog.get();
            if (dialog != null) {
                return;
            }
            switch (msg.what) {
                case REGISTER_SUC:
                    dialog.showRegisterSuc();
                    break;
                case SERVER_FAIL:
                    dialog.showServerError();
                    break;
                case LOGIN_SUC:
                    dialog.showLoginSuc();
                    break;
            }
        }
    }
}
