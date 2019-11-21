package com.liyang.mytaxi.account.model;

import android.os.Handler;
import android.util.Log;

import com.google.gson.Gson;
import com.liyang.mytaxi.MyTaxiApplication;
import com.liyang.mytaxi.account.bean.Account;
import com.liyang.mytaxi.account.response.LoginResponse;
import com.liyang.mytaxi.common.http.IHttpClient;
import com.liyang.mytaxi.common.http.IRequest;
import com.liyang.mytaxi.common.http.IResponse;
import com.liyang.mytaxi.common.http.api.API;
import com.liyang.mytaxi.common.http.biz.BaseBizResponse;
import com.liyang.mytaxi.common.http.imp.BaseRequest;
import com.liyang.mytaxi.common.http.imp.BaseResponse;
import com.liyang.mytaxi.util.DevUtil;
import com.liyang.mytaxi.util.SharedPreferencesDao;

import static android.content.ContentValues.TAG;

/**
 * Created by TodayFu Lee on 2019/11/21.
 */

public class AccountMangerImp implements IAccountManger {

    private IHttpClient httpClient;
    private Handler handler;
    private SharedPreferencesDao dao;

    public AccountMangerImp(IHttpClient client, SharedPreferencesDao dao) {
        this.httpClient = client;
        this.dao = dao;
    }

    @Override
    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    @Override
    public void fetchSmsCode(final String phone) {
        new Thread() {
            @Override
            public void run() {
                String url = API.Config.getDomain() + API.GET_SMS_CODE;
                IRequest request = new BaseRequest(url);
                request.setBody("phone", phone);
                IResponse response = httpClient.post(request, false);
                if (response.getCode() == BaseResponse.STATE_OK) {
                    BaseBizResponse bizResponse =
                            new Gson().fromJson(response.getData(), BaseBizResponse.class);
                    if (bizResponse.getCode() == BaseBizResponse.STATE_OK) {
                        handler.sendEmptyMessage(SMS_SEND_SUC);
                    } else {
                        handler.sendEmptyMessage(SMS_SEND_FAIL);
                    }
                } else {
                    handler.sendEmptyMessage(SMS_SEND_FAIL);
                }

            }
        }.start();
    }

    @Override
    public void checkSmsCode(final String phone, final String smsCode) {
        new Thread() {
            @Override
            public void run() {
                String url = API.Config.getDomain() + API.CHECK_SMS_CODE;
                IRequest request = new BaseRequest(url);
                request.setBody("phone", phone);
                request.setBody("smsCode", smsCode);
                IResponse response = httpClient.post(request, false);
                if (response.getCode() == BaseResponse.STATE_OK) {
                    BaseBizResponse bizResponse = new Gson()
                            .fromJson(response.getData(), BaseBizResponse.class);
                    if (bizResponse.getCode() == BaseBizResponse.STATE_OK) {
                        handler.sendEmptyMessage(SMS_CHECK_SUC);
                    } else {
                        handler.sendEmptyMessage(SMS_CHECK_FAIL);
                    }
                } else {
                    handler.sendEmptyMessage(SMS_CHECK_FAIL);
                }
            }
        }.start();
    }

    @Override
    public void checkUserExist(final String phone) {
        new Thread() {
            @Override
            public void run() {
                String url = API.Config.getDomain() + API.CHECK_USER_EXIST;
                IRequest request = new BaseRequest(url);
                request.setBody("phone", phone);
                IResponse response = httpClient.get(request, false);
                Log.d(TAG, response.getData());
                if (response.getCode() == BaseResponse.STATE_OK) {
                    BaseBizResponse bizRes =
                            new Gson().fromJson(response.getData(),
                                    BaseBizResponse.class);
                    if (bizRes.getCode() == BaseBizResponse.STATE_USER_EXIST) {
                        handler.sendEmptyMessage(USER_EXIST);
                    } else if (bizRes.getCode() ==
                            BaseBizResponse.STATE_USER_NOT_EXIST) {
                        handler.sendEmptyMessage(USER_NOT_EXIST);
                    }
                } else {
                    handler.sendEmptyMessage(SERVER_FAIL);
                }

            }
        }.start();
    }

    @Override
    public void register(final String phone, final String password) {
        new Thread() {
            @Override
            public void run() {
                String url = API.Config.getDomain() + API.REGISTER;
                IRequest request = new BaseRequest(url);
                request.setBody("phone", phone);
                request.setBody("password", password);
                request.setBody("uid", DevUtil.UUID(MyTaxiApplication.getInstance()));

                IResponse response = httpClient.post(request, false);
                Log.d(TAG, response.getData());
                if (response.getCode() == BaseResponse.STATE_OK) {
                    BaseBizResponse bizRes =
                            new Gson().fromJson(response.getData(),
                                    BaseBizResponse.class);
                    if (bizRes.getCode() == BaseBizResponse.STATE_OK) {
                        handler.sendEmptyMessage(REGISTER_SUC);
                    } else {
                        handler.sendEmptyMessage(SERVER_FAIL);
                    }
                } else {
                    handler.sendEmptyMessage(SERVER_FAIL);
                }

            }
        }.start();
    }

    @Override
    public void login(final String phone, final String password) {
        new Thread() {
            @Override
            public void run() {
                String url = API.Config.getDomain() + API.LOGIN;
                IRequest request = new BaseRequest(url);
                request.setBody("phone", phone);
                request.setBody("password", password);


                IResponse response = httpClient.post(request, false);
                Log.d(TAG, response.getData());
                if (response.getCode() == BaseResponse.STATE_OK) {
                    LoginResponse bizRes =
                            new Gson().fromJson(response.getData(), LoginResponse.class);
                    if (bizRes.getCode() == BaseBizResponse.STATE_OK) {
                        // 保存登录信息
                        Account account = bizRes.getAccount();
                        dao.save(SharedPreferencesDao.KEY_ACCOUNT, account);

                        // 通知 UI
                        handler.sendEmptyMessage(LOGIN_SUC);
                    } else if (bizRes.getCode() == BaseBizResponse.STATE_PW_ERR) {
                        handler.sendEmptyMessage(PW_ERROR);
                    } else {
                        handler.sendEmptyMessage(SERVER_FAIL);
                    }
                } else {
                    handler.sendEmptyMessage(SERVER_FAIL);
                }

            }
        }.start();
    }

    @Override
    public void loginByToken() {
        // 获取本地登录信息
        final Account account =
                (Account) dao.get(SharedPreferencesDao.KEY_ACCOUNT,
                        Account.class);


        // 登录是否过期
        boolean tokenValid = false;

        // 检查token是否过期

        if (account != null) {
            if (account.getExpired() > System.currentTimeMillis()) {
                // token 有效
                tokenValid = true;
            }
        }
        if (!tokenValid) {
            handler.sendEmptyMessage(TOKEN_INVALID);
        } else {
            // 请求网络，完成自动登录
            new Thread() {
                @Override
                public void run() {
                    String url = API.Config.getDomain() + API.LOGIN_BY_TOKEN;
                    IRequest request = new BaseRequest(url);
                    request.setBody("token", account.getToken());
                    IResponse response = httpClient.post(request, false);
                    Log.d(TAG, response.getData());
                    if (response.getCode() == BaseResponse.STATE_OK) {
                        LoginResponse bizRes =
                                new Gson().fromJson(response.getData(), LoginResponse.class);
                        if (bizRes.getCode() == BaseBizResponse.STATE_OK) {
                            // 保存登录信息
                            Account account = bizRes.getAccount();
                            // todo: 加密存储
                            dao.save(SharedPreferencesDao.KEY_ACCOUNT, account);
                            handler.sendEmptyMessage(LOGIN_SUC);
                        }
                        if (bizRes.getCode() == BaseBizResponse.STATE_TOKEN_INVALID) {
                            handler.sendEmptyMessage(TOKEN_INVALID);
                        }
                    } else {
                        handler.sendEmptyMessage(SERVER_FAIL);
                    }

                }
            }.start();
        }
    }
}
