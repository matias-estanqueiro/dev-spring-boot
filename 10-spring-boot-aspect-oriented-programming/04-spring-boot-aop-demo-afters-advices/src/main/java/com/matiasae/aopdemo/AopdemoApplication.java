package com.matiasae.aopdemo;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.matiasae.aopdemo.dao.AccountDAO;
import com.matiasae.aopdemo.dao.MembershipDAO;
import com.matiasae.aopdemo.entity.Account;

@SpringBootApplication
public class AopdemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(AopdemoApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(AccountDAO accountDAO, MembershipDAO membershipDAO) {
        return runner -> {
            // demoTheBeforeAdvice(accountDAO, membershipDAO);
            // demoAfterReturningAdvice(accountDAO);
            // demoAfterThrowingAdvice(accountDAO);
            demoTheAfterAdvice(accountDAO);
        };
    }

    private void demoTheBeforeAdvice(AccountDAO accountDAO, MembershipDAO membershipDAO) {
        System.out.println("Calling addAccount() method...");
        Account account = new Account();
        account.setName("John");
        account.setLevel("Gold");
        accountDAO.addAccount(account, true);
        accountDAO.doWork();

        accountDAO.setName("foobar");
        accountDAO.setServiceCode("silver");

        accountDAO.getName();
        accountDAO.getServiceCode();

        System.out.println("Calling addSillyMember() method...");
        membershipDAO.addSillyMember();
        membershipDAO.goToSleep();
    }

    private void demoAfterReturningAdvice(AccountDAO accountDAO) {
        List<Account> accounts = accountDAO.findAccounts();
        System.out.println("\n\nMain Program: AfterReturningDemoApp");
        System.out.println("----");

        System.out.println(accounts);
    }

    private void demoAfterThrowingAdvice(AccountDAO accountDAO) {
        List<Account> accounts = null;

        try {
            boolean tripWire = true;
            accounts = accountDAO.findAccounts(tripWire);
        } catch (Exception e) {
            System.out.println("\n\nMain Program: AfterThrowingDemoApp");
            System.out.println("----");
            System.out.println("Exception caught in main program: " + e);
        }
    }

    private void demoTheAfterAdvice(AccountDAO accountDAO) {
        List<Account> accounts = null;

        try {
            boolean tripWire = false;
            accounts = accountDAO.findAccounts(tripWire);
        } catch (Exception e) {
            System.out.println("\n\nMain Program: AfterDemoApp");
            System.out.println("----");
            System.out.println("Exception caught in main program: " + e);
        }
    }

}
