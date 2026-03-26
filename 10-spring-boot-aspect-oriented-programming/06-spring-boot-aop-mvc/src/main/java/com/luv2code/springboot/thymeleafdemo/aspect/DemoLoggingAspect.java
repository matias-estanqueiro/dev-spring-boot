package com.luv2code.springboot.thymeleafdemo.aspect;

import java.util.logging.Logger;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class DemoLoggingAspect {
    private Logger myLogger = Logger.getLogger(getClass().getName());

    @Pointcut("execution(* com.luv2code.springboot.thymeleafdemo.controller.*.*(..))")
    private void forControllerPackage() {

    }

    @Pointcut("execution(* com.luv2code.springboot.thymeleafdemo.service.*.*(..))")
    private void forServicePackage() {

    }

    @Pointcut("execution(* com.luv2code.springboot.thymeleafdemo.dao.*.*(..))")
    private void forDaoPackage() {

    }

    @Pointcut("forControllerPackage() || forServicePackage() || forDaoPackage()")
    private void forAppFlow() {
    }

    @Before("forAppFlow()")
    public void before(JoinPoint joinpoint) {
        String method = joinpoint.getSignature().toShortString();
        myLogger.info("====>> in @Before: calling method: " + method);

        Object[] args = joinpoint.getArgs();

        for (Object tempArg : args) {
            myLogger.info("====>> argument: " + tempArg);
        }
    }

    @AfterReturning(pointcut = "forAppFlow()", returning = "result")
    public void afterReturning(JoinPoint joinpoint, Object result) {
        String method = joinpoint.getSignature().toShortString();
        myLogger.info("====>> in @AfterReturning: from method: " + method);

        myLogger.info("====>> result: " + result);
    }
}
