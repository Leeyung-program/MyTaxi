package com.liyang.mytaxi.util;

import android.app.Activity;
import android.content.Context;
import android.telephony.TelephonyManager;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by TodayFu Lee on 2019/11/20.
 */

public class DevUtil {
    public static String UUID(Context context){
        TelephonyManager telephonyManager= (TelephonyManager)
                context.getSystemService(Context.TELECOM_SERVICE);
        String devId=telephonyManager.getDeviceId();
        return  devId+ System.currentTimeMillis();
    }
    public static void closeInputMethods(Activity context){
        InputMethodManager inputMethod= (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethod.hideSoftInputFromWindow(context.getCurrentFocus().getWindowToken(),InputMethodManager
                .HIDE_NOT_ALWAYS);
    }
}
