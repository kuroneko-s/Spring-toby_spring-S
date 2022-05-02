package org.choidh.toby_project.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class AspectConfig {
    @Pointcut("execution(* org.choidh.toby_project.AppRunner.*(..))")
    public void service() {
        log.info("==============================");
        log.info("Aspect!");
        log.info("==============================");
    }

    @Before("service()")
    public void beforeAspect(JoinPoint joinPoint) {
        log.info("==============================");
        log.info("before!");
        log.info("==============================");
    }

    @After("service()")
    public void afterAspect(JoinPoint joinPoint) {
        log.info("==============================");
        log.info("Aspect!");
        log.info("==============================");
    }
}

