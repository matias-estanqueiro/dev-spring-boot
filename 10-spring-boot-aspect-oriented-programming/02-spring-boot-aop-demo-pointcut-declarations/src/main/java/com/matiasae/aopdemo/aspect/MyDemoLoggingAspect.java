package com.matiasae.aopdemo.aspect;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class MyDemoLoggingAspect {
    @Pointcut("execution(* com.matiasae.aopdemo.dao.*.*(..))")
    public void forDaoPackage() {}

    @Pointcut("execution(* com.matiasae.aopdemo.dao.*.get*(..))")
    public void getter() {}

    @Pointcut("execution(* com.matiasae.aopdemo.dao.*.set*(..))")
    public void setter() {}

    @Pointcut("forDaoPackage() && !(getter() || setter())")
    public void forDaoPackageNoGetterSetter() {}

    @Before("forDaoPackageNoGetterSetter()")
    public void beforeAddAccountAdvice() {
        System.out.println("\n =======>> Executing @Before advice on addAccount()");
    }

    @Before("forDaoPackageNoGetterSetter()")
    public void performApiAnalytics() {
        System.out.println("\n =======>> Performing API analytics");
    }
}
