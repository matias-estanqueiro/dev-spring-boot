package com.matiasae.aopdemo.aspect;

import java.util.List;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.matiasae.aopdemo.entity.Account;

@Aspect
@Component
@Order(2)
public class MyDemoLoggingAspect {
    @After("execution(* com.matiasae.aopdemo.dao.AccountDAO.findAccounts(..))")
    public void afterFinallyFindAccountsAdvice(JoinPoint joinPoint) {
        String method = joinPoint.getSignature().toShortString();
        System.out.println("\n =======>> Executing @After (finally) on method: " + method);
    }

    @AfterThrowing(
        pointcut = "execution(* com.matiasae.aopdemo.dao.AccountDAO.findAccounts(..))",
        throwing = "theExc"
    )
    public void afterThrowingFindAccountsAdvice(JoinPoint joinPoint, Throwable theExc) {
        String method = joinPoint.getSignature().toShortString();
        System.out.println("\n =======>> Executing @AfterThrowing on method: " + method);
        System.out.println("\n =======>> The exception is: " + theExc);
    }

    @AfterReturning(
        pointcut = "execution(* com.matiasae.aopdemo.dao.AccountDAO.findAccounts(..))",
        returning = "result"
    )
    public void afterReturningFindAccountsAdvice(JoinPoint joinPoint, List<Account> result) {
        String method = joinPoint.getSignature().toShortString();
        System.out.println("\n =======>> Executing @AfterReturning on method: " + method);
        System.out.println("\n =======>> Result is: " + result);

        convertAccountNamesToUpperCase(result);
    }

    private void convertAccountNamesToUpperCase(List<Account> accounts) {
        for (Account account : accounts) {
            String upperName = account.getName().toUpperCase();
            account.setName(upperName);
        }
    }

    @Before("com.matiasae.aopdemo.aspect.CommonPointcuts.forDaoPackageNoGetterSetter()")
    public void beforeAddAccountAdvice(JoinPoint joinPoint) {
        System.out.println("\n =======>> Executing @Before advice on addAccount()");

        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        System.out.println("Method: " + methodSignature);

        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            System.out.println("Argument: " + arg);
            if (arg instanceof Account) {
                Account account = (Account) arg;
                System.out.println("Account name: " + account.getName());
                System.out.println("Account level: " + account.getLevel());
            }
        }
    }

    @Around("execution(* com.matiasae.aopdemo.service.TrafficFortuneService.getFortune(..))")
    public Object aroundGetFortune(ProceedingJoinPoint joinPoint) {
        String method = joinPoint.getSignature().toShortString();
        System.out.println("\n =======>> Executing @Around on method: " + method);
        Long begin = System.currentTimeMillis();
        Object result = null;
        
        try {
            result = joinPoint.proceed();
        } catch (Throwable e) {
            System.out.println(e.getMessage());
            result = "@Around advice: rising an exception!";
            // If we want to rethrow the exception, we can uncomment the following line:
            // throw e;
        }

        Long end = System.currentTimeMillis();
        Long duration = end - begin;
        System.out.println("\n =======>> Duration: " + duration / 1000.0 + " seconds");

        return result;
    }
}
