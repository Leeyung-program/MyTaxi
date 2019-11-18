package com.liyang.mytaxi.account.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.liyang.mytaxi.R;

/**
 * Created by TodayFu Lee on 2019/11/18.
 */

public class CreatePasswordDialog extends Dialog {
    private TextView mTitle;
    private TextView mPhone;
    private EditText mPw;
    private EditText mPw1;
    private Button mBtnConfirm;
    private View mLoading;
    private TextView mTips;
    private String mPhoneStr;

    public CreatePasswordDialog(Context context, String mPhoneStr){
        this(context, R.style.Dialog);
        this.mPhoneStr=mPhoneStr;

    }

    public CreatePasswordDialog(Context context,  int themeResId) {
        super(context, themeResId);
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater= (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View root=inflater.inflate(R.layout.dialog_create_pw,null);
        setContentView(root);
        intView();
        checkUser();
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
    if (checkPwd()){
        String password = mPw.getText().toString();
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

    private void checkUser() {
        mLoading.setVisibility(View.VISIBLE);
        mTips.setText(getContext().getString(R.string.check_user));
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

    }


    public void showLoginSuc() {
        dismiss();
//        ToastUtil.show(getContext(), getContext().getString(R.string.login_suc));
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


}
