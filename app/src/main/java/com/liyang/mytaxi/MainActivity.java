package com.liyang.mytaxi;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.liyang.mytaxi.account.model.AccountMangerImp;
import com.liyang.mytaxi.account.model.IAccountManger;
import com.liyang.mytaxi.account.presenter.IMainPresenter;
import com.liyang.mytaxi.account.presenter.MainPresenterImp;
import com.liyang.mytaxi.account.view.IMainView;
import com.liyang.mytaxi.account.view.PhoneInputDialog;
import com.liyang.mytaxi.common.http.IHttpClient;
import com.liyang.mytaxi.common.http.imp.OkHttpClientImp;
import com.liyang.mytaxi.util.SharedPreferencesDao;
import com.liyang.mytaxi.util.ToastUtil;

public class MainActivity extends AppCompatActivity implements IMainView {

    private static final String TAG = "MainActivity";
    private IHttpClient httpClient;
    private IMainPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        httpClient = new OkHttpClientImp();
        SharedPreferencesDao dao =
                new SharedPreferencesDao(MyTaxiApplication.getInstance(),
                        SharedPreferencesDao.FILE_ACCOUNT);
        IAccountManger manager = new AccountMangerImp(httpClient, dao);
        mPresenter=new MainPresenterImp(this,manager);
        mPresenter.loginByToken();
//        checkLoginState();
    }

//    private void checkLoginState() {
//        final SharedPreferencesDao dao = new SharedPreferencesDao(
//                MyTaxiApplication.getInstance(), SharedPreferencesDao.FILE_ACCOUNT);
//        final Account account = (Account) dao.get(
//                SharedPreferencesDao.KEY_ACCOUNT, Account.class);
//
//        boolean tokenValid = false;
//        if (account != null) {
//            if (account.getExpired() > System.currentTimeMillis()) {
//                tokenValid = true;
//            }
//        }
//        if (!tokenValid) {
//            showPhoneInputDialog();
//        } else {
//            new Thread() {
//                @Override
//                public void run() {
//                    super.run();
//                    String url = API.Config.getDomain() + API.LOGIN_BY_TOKEN;
//                    IRequest request = new BaseRequest(url);
//                    request.setBody("token", account.getToken());
//                    IResponse response = httpClient.post(request, false);
//                    if (response.getCode() == BaseResponse.STATE_OK) {
//                        LoginResponse loginResponse =
//                                new Gson().fromJson(response.getData(), LoginResponse.class);
//                        if (loginResponse.getCode() == BaseBizResponse.STATE_OK) {
//                            Account account1 = loginResponse.getAccount();
//                            dao.save(SharedPreferencesDao.KEY_ACCOUNT, account1);
//                            MainActivity.this.runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    ToastUtil.show(MainActivity.this,
//                                            getString(R.string.login_suc));
//                                }
//                            });
//                        }
//                        if (loginResponse.getCode() == BaseBizResponse.STATE_TOKEN_INVALID) {
//                            MainActivity.this.runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    showPhoneInputDialog();
//                                }
//                            });
//                        }
//                    }else{
//                        MainActivity.this.runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                ToastUtil.show(MainActivity.this,
//                                        getString(R.string.error_server));
//                            }
//                        });
//                    }
//                }
//            }.start();
//        }
//    }
    /**
     *  自动登录成功
     */

    @Override
    public void showLoginSuc() {
        ToastUtil.show(this, getString(R.string.login_suc));
    }

    /**
     * 显示 loading
     */
    @Override
    public void showLoading() {
        // TODO: 17/5/14   显示加载框
    }
    /**
     * 错误处理
     * @param code
     * @param msg
     */

    @Override
    public void showError(int code, String msg) {
        switch (code) {
            case IAccountManger.TOKEN_INVALID:
                // 登录过期
                ToastUtil.show(this, getString(R.string.token_invalid));
                showPhoneInputDialog();
                break;
            case IAccountManger.SERVER_FAIL:
                // 服务器错误
                showPhoneInputDialog();
                break;

        }
    }
    private void showPhoneInputDialog() {
        PhoneInputDialog dialog = new PhoneInputDialog(this);
        dialog.show();
    }
}
