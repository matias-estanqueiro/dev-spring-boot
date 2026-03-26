package com.matiasae.aopdemo.dao;

import com.matiasae.aopdemo.entity.Account;

public interface AccountDAO {
    void addAccount(Account account, boolean vipFlag);
    boolean doWork();

    String getName();
    void setName(String name);
    String getServiceCode();
    void setServiceCode(String serviceCode);
}
