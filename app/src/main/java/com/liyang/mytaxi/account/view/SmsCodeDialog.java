package com.liyang.mytaxi.account.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.dalimao.corelibrary.VerificationCodeInput;
import com.liyang.mytaxi.R;
import com.liyang.mytaxi.common.http.IHttpClient;
import com.liyang.mytaxi.common.http.imp.OkhttpClientImp;

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
        mHttpClient = new OkhttpClientImp();
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
    }

    private void initListener() {
    }

    private void showVerifyState(boolean b) {
    }

    private void showSendState(boolean b) {
    }


}
