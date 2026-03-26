package com.matiasae.aopdemo.aspect;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class MyDemoLoggingAspect {
    
    // Excecute before the addAccount() method
    // @Before("execution(public void addAccount())")

    // Excecute before addAccount() method in AccountDAO class
    // @Before("execution(public void com.matiasae.aopdemo.dao.AccountDAO.addAccount())")

    // Excecute before any add*() method in any class
    // @Before("execution(public void add*())")

    // Excecute before any add*() method in any class with any return type
    // @Before("execution(public * add*())")

    // Excecute before any add*() method in any class with any return type and 1 parameter of type Account
    // @Before("execution(public * add*(com.matiasae.aopdemo.entity.Account))")

    // Excecute before any add*() method in any class with any return type and 1 parameter of type 
    // Account and any number of parameters
    // @Before("execution(public * add*(com.matiasae.aopdemo.entity.Account, ..))")

    // Excecute before any add*() method in any class with any return type and any number 
    // of parameters (including no parameters)
    // @Before("execution(public * com.matiasae.aopdemo.dao.*.add*(..))")

    // Excecute before any method in any class in the com.matiasae.aopdemo.dao package with any 
    // return type and any number of parameters (including no parameters)
    @Before("execution(public * com.matiasae.aopdemo.dao.*.*(..))")
    public void beforeAddAccountAdvice() {
        System.out.println("\n =======>> Executing @Before advice on addAccount()");
    }
}
