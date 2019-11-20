package com.liyang.mytaxi;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.gson.Gson;
import com.liyang.mytaxi.account.bean.Account;
import com.liyang.mytaxi.account.response.LoginResponse;
import com.liyang.mytaxi.account.view.PhoneInputDialog;
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

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private IHttpClient httpClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        httpClient = new OkHttpClientImp();
        checkLoginState();
    }

    private void checkLoginState() {
        final SharedPreferencesDao dao = new SharedPreferencesDao(
                MyTaxiApplication.getInstance(), SharedPreferencesDao.FILE_ACCOUNT);
        final Account account = (Account) dao.get(
                SharedPreferencesDao.KEY_ACCOUNT, Account.class);

        boolean tokenValid = false;
        if (account != null) {
            if (account.getExpired() > System.currentTimeMillis()) {
                tokenValid = true;
            }
        }
        if (!tokenValid) {
            showPhoneInputDialog();
        } else {
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    String url = API.Config.getDomain() + API.LOGIN_BY_TOKEN;
                    IRequest request = new BaseRequest(url);
                    request.setBody("token", account.getToken());
                    IResponse response = httpClient.post(request, false);
                    if (response.getCode() == BaseResponse.STATE_OK) {
                        LoginResponse loginResponse =
                                new Gson().fromJson(response.getData(), LoginResponse.class);
                        if (loginResponse.getCode() == BaseBizResponse.STATE_OK) {
                            Account account1 = loginResponse.getAccount();
                            dao.save(SharedPreferencesDao.KEY_ACCOUNT, account1);
                            MainActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ToastUtil.show(MainActivity.this,
                                            getString(R.string.login_suc));
                                }
                            });
                        }
                        if (loginResponse.getCode() == BaseBizResponse.STATE_TOKEN_INVALID) {
                            MainActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    showPhoneInputDialog();
                                }
                            });
                        }
                    }else{
                        MainActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtil.show(MainActivity.this,
                                        getString(R.string.error_server));
                            }
                        });
                    }
                }
            }.start();
        }
    }

    private void showPhoneInputDialog() {
        PhoneInputDialog dialog = new PhoneInputDialog(this);
        dialog.show();
    }
}
