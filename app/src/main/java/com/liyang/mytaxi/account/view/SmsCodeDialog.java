package com.liyang.mytaxi.account.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.dalimao.corelibrary.VerificationCodeInput;
import com.google.gson.Gson;
import com.liyang.mytaxi.R;
import com.liyang.mytaxi.common.http.IHttpClient;
import com.liyang.mytaxi.common.http.IRequest;
import com.liyang.mytaxi.common.http.IResponse;
import com.liyang.mytaxi.common.http.api.API;
import com.liyang.mytaxi.common.http.biz.BaseBizResponse;
import com.liyang.mytaxi.common.http.imp.BaseRequest;
import com.liyang.mytaxi.common.http.imp.BaseResponse;
import com.liyang.mytaxi.common.http.imp.OkHttpClientImp;
import com.liyang.mytaxi.util.ToastUtil;

import java.lang.ref.SoftReference;

public class SmsCodeDialog extends Dialog {

    private static final String TAG = "SmsCodeDialog";
    private static final int SMS_SEND_SUC = 1;
    private static final int SMS_SEND_FAIL = -1 ;
    private static final int SMS_CHECK_SUC = 2;
    private static final int SMS_CHECK_FAIL = -2;
    private String mPhone;
    private Button mResentBtn;
    private VerificationCodeInput mVerificationInput;
    private View mLoading;
    private View mErrorView;
    private TextView mPhoneTv;
    private IHttpClient mHttpClient;
    private MyHandler mHandler;

    private CountDownTimer countDownTimer=
            new CountDownTimer(10000,1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            mResentBtn.setEnabled(false);
            mResentBtn.setText(String.format(getContext()
                    .getString(R.string.after_time_resend,
                            millisUntilFinished/1000)));
        }

        @Override
        public void onFinish() {
            mResentBtn.setEnabled(true);
            mResentBtn.setText(getContext().getString(R.string.resend));
            cancel();
        }
    };

    public SmsCodeDialog(Context context,String phone){
        this(context,R.style.Dialog);
        this.mPhone = phone;
        mHttpClient = new OkHttpClientImp();
        mHandler  = new MyHandler(this);
    }
    public SmsCodeDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    static  class MyHandler extends Handler {
        //软引用
        SoftReference<SmsCodeDialog> codeDialogSoftRef;
        public MyHandler(SmsCodeDialog smsCodeDialog){
            codeDialogSoftRef = new SoftReference<SmsCodeDialog>(smsCodeDialog);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            SmsCodeDialog dialog=codeDialogSoftRef.get();
            if (dialog!=null){
                return;
            }
            switch (msg.what){
                case SMS_SEND_SUC:
                    dialog.showSendState(true);break;
                case SMS_SEND_FAIL:
                    dialog.showSendState(false);break;
                case SMS_CHECK_SUC:
                    dialog.showVerifyState(true);
                    break;
                case SMS_CHECK_FAIL:
                    dialog.showVerifyState(false);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater)
                getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View mRoot=inflater.inflate(R.layout.dialog_smscode_input,null);
        setContentView(mRoot);
        mPhoneTv = (TextView) findViewById(R.id.phone);
        String template = getContext().getString(R.string.sending);
        mPhoneTv.setText(String.format(template, mPhone));
        mResentBtn = (Button) findViewById(R.id.btn_resend);
        mVerificationInput = (VerificationCodeInput)
                findViewById(R.id.verificationCodeInput);
        mLoading = findViewById(R.id.loading);
        mErrorView = findViewById(R.id.error);
        mErrorView.setVisibility(View.GONE);
        initListener();
        requestSendSmsCode();
    }

    private void requestSendSmsCode() {
        new Thread(){
            @Override
            public void run() {
                String url = API.Config.getDomain() + API.GET_SMS_CODE;
                IRequest request = new BaseRequest(url);
                request.setBody("phone", mPhone);
                IResponse response = mHttpClient.get(request, false);
                Log.d(TAG, response.getData());
                if (response.getCode() == BaseResponse.STATE_OK) {
                    BaseBizResponse bizRes =
                            new Gson().fromJson(response.getData(), BaseBizResponse.class);
                    if (bizRes.getCode() == BaseBizResponse.STATE_OK) {
                        mHandler.sendEmptyMessage(SMS_SEND_SUC);
                    } else  {
                        mHandler.sendEmptyMessage(SMS_SEND_FAIL);
                    }
                } else {
                    mHandler.sendEmptyMessage(SMS_SEND_FAIL);
                }

            }
        }.start();
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        countDownTimer.cancel();
    }
    public SmsCodeDialog(Context context,boolean cancelable, OnCancelListener cancelListener){
        super(context,cancelable,cancelListener);
    }

    private void initListener() {
        //  关闭按钮组册监听器
        findViewById(R.id.close).setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        // 重发验证码按钮注册监听器
        mResentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resend();
            }
        });

        // 验证码输入完成监听器
        mVerificationInput.setOnCompleteListener(new VerificationCodeInput.Listener() {
            @Override
            public void onComplete(String code) {

                commit(code);
            }
        });
    }

    private void commit(final String code) {
        showLoading();
        new Thread(){
            @Override
            public void run() {
                String url = API.Config.getDomain() + API.CHECK_SMS_CODE;
                IRequest request = new BaseRequest(url);
                request.setBody("phone", mPhone);
                request.setBody("code", code);
                IResponse response = mHttpClient.get(request, false);
                Log.d(TAG, response.getData());
                if (response.getCode() == BaseResponse.STATE_OK) {
                    BaseBizResponse bizRes =
                            new Gson().fromJson(response.getData(), BaseBizResponse.class);
                    if (bizRes.getCode() == BaseBizResponse.STATE_OK) {
                        mHandler.sendEmptyMessage(SMS_CHECK_SUC);
                    } else  {
                        mHandler.sendEmptyMessage(SMS_CHECK_FAIL);
                    }
                } else {
                    mHandler.sendEmptyMessage(SMS_CHECK_FAIL);
                }

            }
        }.start();

    }


    private void resend() {
        String template = getContext().getString(R.string.sending);
        mPhoneTv.setText(String.format(template, mPhone));
    }

    private void showVerifyState(boolean suc) {
        if (!suc) {

            //提示验证码错误
            mErrorView.setVisibility(View.VISIBLE);
            mVerificationInput.setEnabled(true);
            mLoading.setVisibility(View.GONE);
        } else {
            mErrorView.setVisibility(View.GONE);
            mLoading.setVisibility(View.VISIBLE);
            //todo :检查用户是否存在


        }

    }

    private void showSendState(boolean suc) {
        if (suc) {
            mPhoneTv.setText(String.format(getContext().getString(R.string.sms_code_send_phone), mPhone));
            countDownTimer.start();
        } else {
            ToastUtil.show(getContext(),
                    getContext().getString(R.string.sms_send_fail));
            mResentBtn.setEnabled(true);
            mResentBtn.setText(getContext().getString(R.string.resend));
            cancel();
        }
    }

    private void showLoading() {
        mLoading.setVisibility(View.VISIBLE);
    }

    public void showUserExist(boolean exist) {
        mLoading.setVisibility(View.GONE);
        mErrorView.setVisibility(View.GONE);
        dismiss();
        if (!exist) {
            // todo 用户不存在,进入注册



        } else {
            // todo 用户存在 ，进入登录


        }
    }

}
