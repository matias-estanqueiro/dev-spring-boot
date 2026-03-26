package com.matiasae.aopdemo.aspect;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class CommonPointcuts {
    @Pointcut("execution(* com.matiasae.aopdemo.dao.*.*(..))")
    public void forDaoPackage() {}

    @Pointcut("execution(* com.matiasae.aopdemo.dao.*.get*(..))")
    public void getter() {}

    @Pointcut("execution(* com.matiasae.aopdemo.dao.*.set*(..))")
    public void setter() {}

    @Pointcut("forDaoPackage() && !(getter() || setter())")
    public void forDaoPackageNoGetterSetter() {}
}
