package com.liyang.mytaxi;

import android.graphics.drawable.AnimatedVectorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
            AnimatedVectorDrawable animatedVectorDrawable= (AnimatedVectorDrawable)
                    getResources().getDrawable(R.drawable.anim);
            ImageView logo= (ImageView) findViewById(R.id.logo);
            logo.setImageDrawable(animatedVectorDrawable);
            animatedVectorDrawable.start();
        }
    }
}
