package com.matiasae.aopdemo;

// import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.matiasae.aopdemo.dao.AccountDAO;
import com.matiasae.aopdemo.dao.MembershipDAO;
// import com.matiasae.aopdemo.entity.Account;
import com.matiasae.aopdemo.service.TrafficFortuneService;

@SpringBootApplication
public class AopdemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(AopdemoApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(AccountDAO accountDAO, MembershipDAO membershipDAO, TrafficFortuneService trafficFortuneService) {
        return runner -> {
            // BEFORE ADVICE
            // -------------
            // demoTheBeforeAdvice(accountDAO, membershipDAO);
            // AFTERS ADVICES
            // --------------
            // demoAfterReturningAdvice(accountDAO);
            // demoAfterThrowingAdvice(accountDAO);
            // demoTheAfterAdvice(accountDAO);
            // AROUND ADVICE
            // -------------
            // demoTheAroundAdvice(trafficFortuneService);
            demoTheAroundAdviceHandleException(trafficFortuneService);
            // demoTheAroundAdviceRethrowException(trafficFortuneService);
        };
    }

    // private void demoTheBeforeAdvice(AccountDAO accountDAO, MembershipDAO membershipDAO) {
    //     System.out.println("Calling addAccount() method...");
    //     Account account = new Account();
    //     account.setName("John");
    //     account.setLevel("Gold");
    //     accountDAO.addAccount(account, true);
    //     accountDAO.doWork();

    //     accountDAO.setName("foobar");
    //     accountDAO.setServiceCode("silver");

    //     accountDAO.getName();
    //     accountDAO.getServiceCode();

    //     System.out.println("Calling addSillyMember() method...");
    //     membershipDAO.addSillyMember();
    //     membershipDAO.goToSleep();
    // }

    // private void demoAfterReturningAdvice(AccountDAO accountDAO) {
    //     List<Account> accounts = accountDAO.findAccounts();
    //     System.out.println("\n\nMain Program: AfterReturningDemoApp");
    //     System.out.println("----");

    //     System.out.println(accounts);
    // }

    // private void demoAfterThrowingAdvice(AccountDAO accountDAO) {
    //     List<Account> accounts = null;

    //     try {
    //         boolean tripWire = true;
    //         accounts = accountDAO.findAccounts(tripWire);
    //     } catch (Exception e) {
    //         System.out.println("\n\nMain Program: AfterThrowingDemoApp");
    //         System.out.println("----");
    //         System.out.println("Exception caught in main program: " + e);
    //     }
    // }

    // private void demoTheAfterAdvice(AccountDAO accountDAO) {
    //     List<Account> accounts = null;

    //     try {
    //         boolean tripWire = false;
    //         accounts = accountDAO.findAccounts(tripWire);
    //     } catch (Exception e) {
    //         System.out.println("\n\nMain Program: AfterDemoApp");
    //         System.out.println("----");
    //         System.out.println("Exception caught in main program: " + e);
    //     }
    // }

    // private void demoTheAroundAdvice(TrafficFortuneService trafficFortuneService) {
    //     System.out.println("\n\nMain Program: AOP Demo Application");
    //     System.out.println("----");

    //     System.out.println("Calling getFortune() method...");
    //     String data = trafficFortuneService.getFortune();
    //     System.out.println("\nMy fortune is: " + data);
    // }

    private void demoTheAroundAdviceHandleException(TrafficFortuneService trafficFortuneService) {
        System.out.println("\n\nMain Program: AOP Demo Application");
        System.out.println("----");

        System.out.println("Calling getFortune() method...");
        boolean tripWire = true;
        String data = trafficFortuneService.getFortune(tripWire);
        System.out.println("\nMy fortune is: " + data);
    }

    // private void demoTheAroundAdviceRethrowException(TrafficFortuneService trafficFortuneService) {
    //     System.out.println("\n\nMain Program: AOP Demo Application");
    //     System.out.println("----");

    //     System.out.println("Calling getFortune() method...");
    //     boolean tripWire = true;
    //     try {
    //         String data = trafficFortuneService.getFortune(tripWire);
    //         System.out.println("\nMy fortune is: " + data);
    //     } catch (Exception e) {
    //         System.out.println("\nMain Program: Caught exception: " + e);
    //     }
    // }

}
