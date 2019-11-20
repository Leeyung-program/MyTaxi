package com.liyang.mytaxi.account.response;


import com.liyang.mytaxi.account.bean.Account;
import com.liyang.mytaxi.common.http.biz.BaseBizResponse;

/**
 * Created by TodayFu Lee on 2019/11/20.
 */

public class LoginResponse extends BaseBizResponse {
    private Account account;

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
}
