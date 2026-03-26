package com.matiasae.aopdemo.dao;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Repository
@Component
public class MembershipDAOImpl implements MembershipDAO {
    @Override
    public void addAccount() {
        System.out.println(getClass() + ": Doing my DB work: Adding a MEMBERSHIP account");
    }

    @Override
    public boolean addSillyMember() {
        System.out.println(getClass() + ": Doing my DB work: Adding a SILLY MEMBER");
        return true;
    }

    @Override
    public void goToSleep() {
        System.out.println(getClass() + ": Doing my DB work: Going to sleep");
    }
}
