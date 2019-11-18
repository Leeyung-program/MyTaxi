package com.liyang.mytaxi.account.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.liyang.mytaxi.R;
import com.liyang.mytaxi.util.FormatUtil;

/**
 * Created by TodayFu Lee on 2019/11/18.
 */

public class LoginDialog extends Dialog {
    private View mRoot;
    private EditText mPhone;
    private Button mButton;

    public LoginDialog(Context context){
        this(context, R.style.Dialog);
    }

    public LoginDialog(  Context context,  int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater= (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View root=inflater.inflate(R.layout.dialog_login_input,null);
        setContentView(root);
        initListener();
    }

    private void initListener() {
        mButton = (Button) findViewById(R.id.btn_next);
        mButton.setEnabled(false);
        mPhone = (EditText) findViewById(R.id.phone);
        //  手机号输入框组册监听检查手机号输入是否合法
        mPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                check();
            }
        });
        // 按钮注册监听
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                String phone =  mPhone.getText().toString();
//                SmsCodeDialog dialog = new SmsCodeDialog(getContext(), phone);
//                dialog.show();

            }
        });


        // 关闭按钮注册监听事件

        findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                PhoneInputDialog.this.dismiss();
            }
        });
    }

    private void check() {
        String phone =  mPhone.getText().toString();
        boolean legal = FormatUtil.checkMobile(phone);
        mButton.setEnabled(legal);
    }

}
