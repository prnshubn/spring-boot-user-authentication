package com.wipro.newsapp.user.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Before("execution(* com.wipro.newsapp.user.controller..*(..)))")
    public void beforeEachMethod1(JoinPoint joinPoint) {
        logger.debug(joinPoint.getSignature().getName());
    }

    @After("execution(* com.wipro.newsapp.user.controller..*(..)))")
    public void afterEachMethod1(JoinPoint joinPoint) {
        logger.debug(joinPoint.getSignature().getName());
    }

    @Before("execution(* com.wipro.newsapp.user.service..*(..)))")
    public void beforeEachMethod2(JoinPoint joinPoint) {
        logger.debug(joinPoint.getSignature().getName());
    }

    @After("execution(* com.wipro.newsapp.user.service..*(..)))")
    public void afterEachMethod2(JoinPoint joinPoint) {
        logger.debug(joinPoint.getSignature().getName());
    }
}
