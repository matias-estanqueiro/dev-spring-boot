package com.matiasae.aopdemo.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.matiasae.aopdemo.entity.Account;

@Repository
public class AccountDAOImpl implements AccountDAO {
    private String name;
    private String serviceCode;

    @Override
    public void addAccount(Account account, boolean vipFlag) {
        System.out.println(getClass() + ": Doing my DB work: Adding an ACCOUNT \n");
    }

    @Override
    public boolean doWork() {
        System.out.println(getClass() + ": Doing my DB work: Doing something \n");
        return false;
    }

    @Override
    public List<Account> findAccounts() {
        return findAccounts(false);
    }

    @Override
    public List<Account> findAccounts(boolean tripWire) {
        // simulate an exception
        if (tripWire) {
            throw new RuntimeException("ERROR in method: findAccounts");
        }
        
        List<Account> myAccounts = new ArrayList<>();

        Account temp1 = new Account("John", "Silver");
        Account temp2 = new Account("Madhu", "Platinum");
        Account temp3 = new Account("Luca", "Gold");

        myAccounts.add(temp1);
        myAccounts.add(temp2);
        myAccounts.add(temp3);

        return myAccounts;
    }


    public String getName() {
        System.out.println(getClass() + ": in getName() \n");
        return name;
    }

    public void setName(String name) {
        System.out.println(getClass() + ": in setName() \n");
        this.name = name;
    }

    public String getServiceCode() {
        System.out.println(getClass() + ": in getServiceCode() \n");
        return serviceCode;
    }

    public void setServiceCode(String serviceCode) {
        System.out.println(getClass() + ": in setServiceCode() \n");
        this.serviceCode = serviceCode;
    }
}
