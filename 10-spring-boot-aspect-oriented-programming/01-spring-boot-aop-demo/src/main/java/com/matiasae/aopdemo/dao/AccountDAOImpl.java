package com.matiasae.aopdemo.dao;

import org.springframework.stereotype.Repository;

import com.matiasae.aopdemo.entity.Account;

@Repository
public class AccountDAOImpl implements AccountDAO {
    @Override
    public void addAccount(Account account, boolean vipFlag) {
        System.out.println(getClass() + ": Doing my DB work: Adding an ACCOUNT \n");
    }

    @Override
    public boolean doWork() {
        System.out.println(getClass() + ": Doing my DB work: Doing something \n");
        return false;
    }
}
