package com.liyang.mytaxi;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkLoginState();
    }

    private void checkLoginState() {
        boolean tokenValid=false;
        if (!tokenValid){
            showPhoneInputDialog();
        }
    }

    private void showPhoneInputDialog() {
//        PhoneInputDialog dialog = new

    }
}
