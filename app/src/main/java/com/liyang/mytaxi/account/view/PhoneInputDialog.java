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

public class PhoneInputDialog extends Dialog {

    private View mRoot;
    private EditText mPhone;
    private Button mButton;
    public PhoneInputDialog(Context context){
        this(context,R.style.Dialog);
    }
    public PhoneInputDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater= (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mRoot=inflater.inflate(R.layout.dialog_phone_input,null);
        setContentView(mRoot);
        initListener();
    }

    private void initListener() {
        mPhone=findViewById(R.id.phone);
        mButton=findViewById(R.id.btn_next);
        mButton.setEnabled(true);
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
                String phone =  mPhone.getText().toString();
                SmsCodeDialog dialog = new SmsCodeDialog(getContext(), phone);
                dialog.show();
            }
        });


        // 关闭按钮注册监听事件

        findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhoneInputDialog.this.dismiss();
            }
        });
    }

    private void check() {
        String phone =  mPhone.getText().toString();
        boolean legal = FormatUtil.checkMobile(phone);
        mButton.setEnabled(legal);
    }
}
