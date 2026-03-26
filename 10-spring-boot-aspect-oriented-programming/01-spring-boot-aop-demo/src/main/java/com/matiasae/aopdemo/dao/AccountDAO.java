package com.matiasae.aopdemo.dao;

import com.matiasae.aopdemo.entity.Account;

public interface AccountDAO {
    void addAccount(Account account, boolean vipFlag);
    boolean doWork();
}
